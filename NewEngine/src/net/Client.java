package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import core.modules.entities.Entity;
import core.utils.BinaryWriter;
import core.utils.Constants;
import serialization.Type;
import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSField;
import serialization.containers.MSObject;
import serialization.containers.MSString;

public class Client {

	private final static byte[] PACKET_HEADER = new byte[] {0x40, 0x40};
	private final static byte PACKET_TPYE_CONNECT = 0x01;
	private final static byte PACKET_TYPE_CONFIRMATION = 0x03;
	private final int MAX_PACKET_SIZE = 1024;
	
	public enum Error {
		NONE, INVALID_HOST, SOCKET_EXCEPTION,
	}
	
	private String ipAddress;
	private int port;
	private String username;
	private Error errorCode = Error.NONE;
	
	private InetAddress serverAddress;
	private DatagramSocket socket;
	private boolean active = false;
	private double serverLastConfirmed = 0;
	private int ping;
	
	private Hashtable<InetAddress, DatagramPacket> receivedPackets = new Hashtable<InetAddress, DatagramPacket>();
	private Thread listenThread;
	private boolean listening = false;
	
	private Map<String, EntityInterface> entities = new HashMap<String, EntityInterface>();
	
	/**
	 * Format: 192.168.1.1:5000
	 * 
	 * @param host
	 * 				the host public ip
	 */
	public Client(String host) {
		String[] parts = host.split(":");
		if(parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		ipAddress = parts[0];
		try {
			port = Integer.parseInt(parts[1]);
		}catch(NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
	}
	
	public Client(String host, int port) {
		this.ipAddress = host;
		this.port = port;
	}
	
	public void update() {
		Iterator<Entry<InetAddress, DatagramPacket>> iterator = receivedPackets.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<InetAddress, DatagramPacket> client = iterator.next();
			process(client.getValue());
		}
		receivedPackets.clear();
		if((System.nanoTime() / Constants.NANOSECOND) - serverLastConfirmed > 5.0) {
			//TODO: handle leaving the server
		}
		MSDatabase playerData = new MSDatabase("PlayerUD");
		for(EntityInterface entity : entities.values()) {
			playerData.addObject(entity.serialize());
			//TODO: write serialize function
		}
		send(playerData);
	}
	
	private void process(DatagramPacket packet) {
		byte[] data = packet.getData();
		
		if(new String(data, 0, 4).equals("MSDB")) {
			MSDatabase database = MSDatabase.deserialize(data);
			process(database);
		}else if(data[0] == 0x40 && data[1] == 0x40) {
			switch(data[2]) {
			case 0x01:
				System.out.println("Connected to the server!");
				active = true;
				break;
			case 0x02:
				System.out.println("Server is active!");
				break;
			case 0x03:
				serverLastConfirmed = (System.nanoTime() / Constants.NANOSECOND);
//				System.out.println("received request");
				sendConnectionConfirmation();
				break;
			}
		}
	}
	
	private void process(MSDatabase database) {
		switch(database.getName()) {
		case "serverUD":
			updateData(database);
		break;
		}
	}
	
	private void updateData(MSDatabase database) {
		for(int i = 0; i < database.objects.size(); i++) {
			MSObject entity = database.objects.get(i);
			if(entity.getName().equalsIgnoreCase(username))
				continue;
			if(entities.containsKey(entity.getName())) {
				entities.get(entity.getName()).update(entity);
			}else {
				System.out.println("New Player Added!");
				entities.put(entity.getName(),
				new EntityInterface(entity.getName(), entity.findArray("positions").floatData,
				entity.findArray("rotations").floatData));
			}
		}
	}
	
	public boolean connect(Entity player, String username) {
		this.username = username;
		entities.put(username, new EntityInterface(username, player.getPosition().toArray(), player.getRotation().toArray()));
		
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			errorCode = Error.SOCKET_EXCEPTION;
			return false;
		}
		sendConnectionPacket();
		if(!receiveConnectionPacket())
			return false;
		serverLastConfirmed = (System.nanoTime() / Constants.NANOSECOND);
		sendPlayerID(username);
		
		if(!active) {
			try {
				throw new IOException("Could not connect to server!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		listening = true;
		listenThread = new Thread(() -> {
			listen();
		}, "Millennium Receiver");
		listenThread.start();
		System.out.println("Engine is listeninng");
		
		return true;
	}
	
	private void listen() {
		while(listening) {
			byte[] receiver = new byte[MAX_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(receiver, MAX_PACKET_SIZE);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			receivedPackets.put(packet.getAddress(), packet);
		}
	}
	
	private void sendConnectionPacket() {
		BinaryWriter writer = new BinaryWriter();
		writer.write(PACKET_HEADER);
		writer.write(PACKET_TPYE_CONNECT);
		send(writer.getBuffer());
	}
	
	private void sendConnectionConfirmation() {
		BinaryWriter writer = new BinaryWriter();
		writer.write(PACKET_HEADER);
		writer.write(PACKET_TYPE_CONFIRMATION);
		send(writer.getBuffer());
	}
	
	private boolean receiveConnectionPacket() {
		DatagramPacket packet = new DatagramPacket(new byte[4], 3);
		try {
			socket.setSoTimeout(5000);
			socket.receive(packet);
			socket.setSoTimeout(0);
		}catch(SocketTimeoutException s) {
			return false;
		}catch(IOException e) {
			e.printStackTrace();
		}
		process(packet);
		return true;
	}
	
	private void sendPlayerID(String username) {
		MSDatabase database = new MSDatabase("playerID");
		MSObject object = new MSObject("Player");
		object.addString(MSString.Create("username", username));
		object.addArray(MSArray.Float("positions", entities.get(username).getPosition().toArray()));
		object.addArray(MSArray.Float("rotations", entities.get(username).getRotation().toArray()));
		//TODO: support scaling in all three axis
		object.addField(MSField.Float("scale", entities.get(username).getWorldTransform().getScaling().getX()));
		database.addObject(object);
		send(database);
	}
	
	public void send(MSDatabase database) {
		byte[] data = new byte[database.getSize()];
		database.getBytes(data, 0);
		send(data);
	}
	
	public void send(byte[] data) {
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Error getErrorCode() {
		return errorCode;
	}
	
	public int getPing() {
		return ping;
	}
	
	private static void dump(MSDatabase database) {
		System.out.println("--------------------------------");
		System.out.println("           MSDatabase           ");
		System.out.println("--------------------------------");
		System.out.println("Name: " + database.getName());
		System.out.println("Size: " + database.getSize());
		System.out.println("Object Count: " + database.objects.size());
		for(MSObject object : database.objects) {
			System.out.println("\tObject:");
			System.out.println("\tName: " + object.getName());
			System.out.println("\tSize: " + object.getSize());
			System.out.println("\tField Count: " + object.fields.size());
			for(MSField field : object.fields) {
				System.out.println("\t\tField:");
				System.out.println("\t\t\tName: " + field.getName());
				System.out.println("\t\t\tSize: " + field.getSize());
				System.out.println("\t\t\tField Count: " + object.fields.size());
				String data = "";
				switch(field.type){
				case Type.BYTE:
					data += field.getByte();
					break;
				case Type.SHORT:
					data += field.getShort();
					break;
				case Type.CHAR:
					data += field.getChar();
					break;
				case Type.INTEGER:
					data += field.getInt();
					break;
				case Type.LONG:
					data += field.getLong();
					break;
				case Type.FLOAT:
					data += field.getFloat();
					break;
				case Type.DOUBLE:
					data += field.getDouble();
					break;
				case Type.BOOLEAN:
					data += field.getBoolean();
					break;
				}
				System.out.println("\t\t\tData: " + data);
			}
			System.out.println();
			for(MSArray array : object.arrays) {
				System.out.println("\t\tArray:");
				System.out.println("\t\t\tName: " + array.getName());
				System.out.println("\t\t\tSize: " + array.getSize());
				System.out.println("\t\t\tData: " + array.getString());
			}
			System.out.println();
			for(MSString string : object.strings) {
				System.out.println("\t\tString:");
				System.out.println("\t\t\tName: " + string.getName());
				System.out.println("\t\t\tData: " + string.getString());
			}
			System.out.println();
		}
		System.out.println("--------------------------------");
	}
	
	public void addEntity(EntityInterface entity) {
		entities.put(entity.getName(), entity);
	}
	
	public void render() {
		for(EntityInterface entity : entities.values()) {
			entity.render();
		}
	}
}


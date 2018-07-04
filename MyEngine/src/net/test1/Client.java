package net.test1;

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

import coreEngine.core.Game;
import renderEngine.rendering.MasterRenderer;
import serialization.Type;
import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSField;
import serialization.containers.MSObject;
import serialization.containers.MSString;
import util.dataStructures.BinaryWriter;
import util.time.Time;

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
	
	private Game game;
	//private List<ServerPlayer> players = new ArrayList<ServerPlayer>();
	private Map<String, ServerPlayer> players = new HashMap<String, ServerPlayer>();

	/**
	 * Format: 192.168.1.1:5000
	 * 
	 * @param host
	 *            the host public ip
	 */
	public Client(String host) {
		String[] parts = host.split(":");
		if (parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		ipAddress = parts[0];
		try {
			port = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
	}

	public Client(Game game, String host, int port) {
		this.ipAddress = host;
		this.port = port;
		this.game = game;
	}
	
	public void update(){
		Iterator<Entry<InetAddress, DatagramPacket>> iterator = receivedPackets.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<InetAddress, DatagramPacket> client = iterator.next();
			process(client.getValue());
		}				
		receivedPackets.clear();
		if((System.nanoTime() / Time.SECOND) - serverLastConfirmed > 5.0) {
			System.out.println("Error server disconnected");
			//TODO: handle leaving the server
		}
		MSDatabase playerData = new MSDatabase("playerUD");
		playerData.addObject(game.getPlayer().serialize());
		send(playerData);
	}
	
	private void process(DatagramPacket packet){
		
		byte[] data= packet.getData();
		//InetAddress address = packet.getAddress();
		
		if(new String(data, 0, 4).equals("MSDB")){
			MSDatabase database = MSDatabase.deserialize(data);
			process(database);
		}else if (data[0] == 0x40 && data[1] == 0x40){
			switch(data[2]){
			case 0x01:
				System.out.println("Connected to the server!");
				active = true;
				break;
			case 0x02:
				System.out.println("Server is active!");
				break;
			case 0x03:
				serverLastConfirmed = (System.nanoTime() / Time.SECOND);
				sendConnectionConfirmation();
				break;
			}
		}
	}
	
	private void process(MSDatabase database){
		//System.out.println("Received Database!");
		//dump(database);
		switch(database.getName()){
		case "serverUD":
			updateData(database);
			break;
		}
	}
	
	private void updateData(MSDatabase database){
		for(int i = 0; i < database.objects.size(); i++){
			MSObject entity = database.objects.get(i);
			if(entity.getName().equalsIgnoreCase(username)){
			//	System.out.println("same user");
				continue;
			}
			if(players.containsKey(entity.getName())){
				players.get(entity.getName()).update(entity);
			}else{
				System.out.println("New Player Added");
				players.put(entity.getName(), 
				new ServerPlayer(entity.getName(), entity.findArray("positions").floatData, 
				entity.findArray("rotations").floatData));								
			}
		}
	}

	public boolean connect(String username) {
		this.username = username;
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
		serverLastConfirmed = (System.nanoTime() / Time.SECOND);
		sendPlayerID(username);
		
		if(!active){
			try {
				throw new IOException("Could not connect to server");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		listening = true;						
		listenThread = new Thread(() -> {
			listen();
		}, "MilenniumEngine-Receiver");
		listenThread.start();
		System.out.println("Engine is listening...");
		
		return true;
	}
	
	private void listen(){
		while(listening){
			byte[] receiver = new byte[MAX_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(receiver, MAX_PACKET_SIZE);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println("packet received!");
			receivedPackets.put(packet.getAddress(), packet);
		}
	}
	
	private void sendConnectionPacket(){
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
	
	private boolean receiveConnectionPacket(){
		DatagramPacket packet = new DatagramPacket(new byte[4], 3);
		try {
			socket.setSoTimeout(5000);
			socket.receive(packet);
			socket.setSoTimeout(0);
		} catch (SocketTimeoutException s) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		process(packet);
		return true;
	}
	
	private void sendPlayerID(String username){
		MSDatabase database = new MSDatabase("playerID");
		MSObject object = new MSObject("Player");
		object.addString(MSString.Create("username", username));
		object.addArray(MSArray.Float("positions", game.getPlayer().getPosition().toArray()));
		object.addArray(MSArray.Float("rotations", game.getPlayer().getRotation().toArray()));
		object.addField(MSField.Float("scale", game.getPlayer().getScale()));
		database.addObject(object);
		byte[] data = new byte[database.getSize()];
		database.getBytes(data, 0);
		send(data);
	}

	public void send(byte[] data) {
		assert (socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(MSDatabase database){
		byte[] data = new byte[database.getSize()];
		database.getBytes(data, 0);
		send(data);
	}

	public Error getErrorCode() {
		return errorCode;
	}
	
	public int getPing(){
		return ping;
	}
	
	private static void dump(MSDatabase database){
		System.out.println("--------------------------------");		
		System.out.println("           MSDatabase           ");
		System.out.println("--------------------------------");
		System.out.println("Name: " + database.getName());
		System.out.println("Size: " + database.getSize());
		System.out.println("Object Count: " + database.objects.size());
		for(MSObject object : database.objects){
			System.out.println("\tObject:");
			System.out.println("\tName: " + object.getName());
			System.out.println("\tSize: " + object.getSize());
			System.out.println("\tField Count: " + object.fields.size());
			for(MSField field : object.fields){
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
			for(MSArray array : object.arrays){
				System.out.println("\t\tArray:");
				System.out.println("\t\t\tName: " + array.getName());
				System.out.println("\t\t\tSize: " + array.getSize());
				System.out.println("\t\t\tData: " + array.getString());
			}
			for(MSString string : object.strings) {
				System.out.println("\t\tString:");
				System.out.println("\t\t\tName: " + string.getName());
				System.out.println("\t\t\tData: " + string.getString());
			}
			System.out.println();
		}
		System.out.println("--------------------------------");
	}

	
	public void renderPlayers(MasterRenderer renderer){
		for(ServerPlayer player : players.values()){
			renderer.processEntity(player);
		}
	}
	
	public void addPlayer(ServerPlayer player){
		players.put(player.getName(), player);
	}
}

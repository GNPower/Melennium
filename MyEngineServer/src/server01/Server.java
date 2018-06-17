package server01;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import serialization.Type;
import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSField;
import serialization.containers.MSObject;

public class Server {

	private int port;
	private Thread listenThread;
	private boolean listening = false;
	private DatagramSocket socket;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	//private Map<InetAddress, ServerClient> clients = new HashMap<InetAddress, ServerClient>();
	private Hashtable<InetAddress, ServerClient> clients = new Hashtable<InetAddress, ServerClient>();
	
	//starts a server with the passed in port
	public Server(int port){
		this.port = port;
	}
	
	public void start(){
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Started server on port 8192...");
		
		listening = true;
		
		listenThread = new Thread(() -> {
			listen();
		}, "MilenniumServer-Receiver");
		listenThread.start();
		System.out.println("Server is listening...");
		
		runServer();
	}
	
	private void runServer(){		
		while(listening){
			MSDatabase database = new MSDatabase("serverUD");
			for(ServerClient client : clients.values()){
				if(!client.isSet)
					continue;
				database.addObject(client.getUpdate());
			}			
			//dump(database);
			if(!database.objects.isEmpty()){
				byte[] data = new byte[database.getSize()];
				database.getBytes(data, 0);
				sendToAllClients(data);
			}
		}
	}
	
	private void listen(){
		while(listening){
			DatagramPacket packet = new DatagramPacket(receivedDataBuffer, MAX_PACKET_SIZE);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			process(packet);
		}
	}
	
	private void process(DatagramPacket packet){
		byte[] data= packet.getData();
		InetAddress address = packet.getAddress();
		int port = packet.getPort();		
		
		if(new String(data, 0, 4).equals("MSDB")){
			MSDatabase database = MSDatabase.deserialize(data);
			process(address, database);
		}else if (data[0] == 0x40 && data[1] == 0x40){
			switch(data[2]){
			case 0x01:
				System.out.println("Received connection packet");
				clients.put(address, new ServerClient(address, port));
				send(new byte[] {0x40, 0x40, 0x01}, address, port);
				break;
			case 0x02:
				System.out.println("Received activity request");
				send(new byte[] {0x40, 0x40, 0x02}, address, port);
				break;
			}
		} else{
			dump(packet);
		}
	}
	
	private void process(InetAddress address, MSDatabase database){		
		System.out.println("Received Database!");
		dump(database);
		String name = database.getName();
		switch(name){
		case "playerID":
			clients.get(address).setPlayerID(database);
			break;
		case "playerUD":
			clients.get(address).update(database);
			break;
		}
	}
	
	public void send(byte[] data, InetAddress address, int port){
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendToAllClients(byte[] data){
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length);;
		
		Iterator<Entry<InetAddress, ServerClient>> iterator = clients.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<InetAddress, ServerClient> client = iterator.next();
			packet.setAddress(client.getKey());
			packet.setPort(client.getValue().getPort());
			try {
				socket.send(packet);
				//System.out.println("sending packet");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		for(ServerClient client : clients.values()){
//			
//			packet.setAddress(client.getAddress());
//			packet.setPort(client.getPort());
//			try {
//				socket.send(packet);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.out.println("sent data");
//		}
		
		
//TODO: send to all clients
	}
	
	private void dump(DatagramPacket packet){
		byte[] data = packet.getData();
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		
		System.out.println("--------------------------------");
		System.out.println("PACKET:");
		System.out.println("\t" + address.getHostAddress() + " : " + port);
		System.out.println("\tContents: ");
		System.out.print("\t\t");
		for(int i = 0; i < packet.getLength(); i++){
			System.out.printf("%x  ", data[i]);
			if((i + 1) % 8 == 0)
				System.out.print("\n\t\t");
		}
		System.out.println();
		System.out.println("\t\t" + new String(data));
		System.out.println("--------------------------------");
	}
	
	private void dump(MSDatabase database){
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
				array.toString();
			}
			System.out.println();
		}
		System.out.println("--------------------------------");
	}

	public int getPort() {
		return port;
	}
}
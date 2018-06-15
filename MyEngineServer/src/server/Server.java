package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import serialization.Type;
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
	
	private Set<ServerClient> clients = new HashSet<ServerClient>();
	
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
			process(database);
		}else if (data[0] == 0x40 && data[1] == 0x40){
			switch(data[2]){
			case 0x01:
				System.out.println("Received connection packet");
				clients.add(new ServerClient(packet.getAddress(), packet.getPort()));
				break;
			case 0x02:
				System.out.println("Received activity request");
				send(new byte[] {0x40, 0x40, 0x02}, address, port);
			}
		} else{
			dump(packet);
		}
	}
	
	private void process(MSDatabase database){		
		System.out.println("Received Database!");
		dump(database);
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
		}
		System.out.println("--------------------------------");
	}

	public int getPort() {
		return port;
	}
}

package server03;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Hashtable;

import serialization.containers.MSDatabase;

public class Receiver {
	
	private Server initHost;
	private Sender sender;
	private byte[] receivedDataBuffer = new byte[Server.MAX_PACKET_SIZE];
	private Hashtable<Address, Client> clients;
	
	private boolean listening = false;
	private Thread listenThread;

	public Receiver(Server initializingHost, Sender sender, int clientLimit){
		initHost = initializingHost;
		this.sender = sender;
		clients = new Hashtable<Address, Client>(clientLimit);
	}
	
	public void intitialize() {
		if(listening)
			return;
		listening = true;
		listenThread = new Thread(() ->  {
			listen();
		}, "MillenniumServer-Receiver");
		listenThread.start();
	}
	
	private void listen() {
		System.out.println("Receiver Thread Started Successfully!");
		while(listening) {
			DatagramPacket packet = new DatagramPacket(receivedDataBuffer, Server.MAX_PACKET_SIZE);
			try {
				initHost.getSocket().receive(packet);				
			}catch(IOException e){
				System.err.println("Error receiving packets in listen thread");
				e.printStackTrace();
			}
			process(packet);
		}
	}
	
	private void process(DatagramPacket packet) {
		byte[] data = packet.getData();
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		
		if(new String(data, 0, 4).equals("MSDB")){
			process(new Address(address, port), MSDatabase.deserialize(data));
		}else if(data[0] == 0x40 && data[1] == 0x40) {
			switch(data[2]) {
			case 0x01:
				System.out.println("Received connection packet");
				Address addressID = new Address(address, port);
				clients.put(addressID, new Client(addressID));
				sender.sendSpecifically(new byte[] {0x40, 0x40, 0x01}, addressID);
				//send(new byte[] {0x40, 0x40, 0x01}, address, port);
				break;
			case 0x02:
				System.out.println("Received activity request");
				
				//send(new byte[] {0x40, 0x40, 0x02}, address, port);
				break;
			}
		}else {
			//TODO: block user connection
		}
	}
	
	private void process(Address adddress, MSDatabase database) {
		
	}
	
	public Hashtable<Address, Client> getClientList() {
		return clients;
	}
}

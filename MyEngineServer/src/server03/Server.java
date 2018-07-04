package server03;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Random;

public class Server {

	public static final int MAX_PACKET_SIZE = 1024;
	public static final int CLIENT_LIMIT = 100;
	
	private boolean running = false;
	public volatile boolean hasClients = false;
	
	private int port;
	private DatagramSocket socket;
	
	private Sender sender;
	private Receiver receiver;
	
	private Hashtable<Address, Client> clients;
	
	public Server(int port) {
		this.port = port;
	}
	
	public Server() {
		Random rand = new Random();
		port = rand.nextInt(64000) + 1025;
	}
	
	public void start() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.err.println("Failed to initialize port, server could not start!");
			e.printStackTrace();
		}
		initThreads();
		while(running) {
			clients = receiver.getClientList();
			if(clients != null)
				hasClients = true;
			else
				hasClients = false;
			//TODO: client list maintenance
		}
	}
	
	private void initThreads() {
		sender = new Sender(this, CLIENT_LIMIT);
		sender.intitialize();
		receiver = new Receiver(this, sender, CLIENT_LIMIT);
		receiver.intitialize();
		System.out.println("Server started on port: " + port);
		running = true;
	}
	
	public int getPort() {
		return port;
	}
	
	public DatagramSocket getSocket() {
		return socket;
	}
	
	public Hashtable<Address, Client> getClientList() {
		return clients;
	}
}

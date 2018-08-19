package server02;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
	
	public static final int MAX_PACKET_SIZE = 1024;
	
	private Receiver[] receivers;
	private Sender[] senders;
	
	private int basePort;

	public Server(int basePort, int dataThreads) {
		this.basePort = basePort;
		receivers = new Receiver[dataThreads];
		senders = new Sender[dataThreads];
	}
	
	private void run() {
		System.out.println("All threads started successfully");
		
		while(true) {
			
		}
	}
	
	public void start() {
		
		for(int i = 0; i < receivers.length; i++) {
			receivers[i] = new Receiver(this, i);
			receivers[i].start();
			
			senders[i] = new Sender(this, i);
			senders[i].start();
		}
		
		run();
	}
	
	public void stop() {
		for(int i = 0; i < receivers.length; i++) {
			receivers[i].stop();
			senders[i].stop();
		}
	}

	public int getBasePort() {
		return basePort;
	}

	public void setBasePort(int basePort) {
		this.basePort = basePort;
	}
}

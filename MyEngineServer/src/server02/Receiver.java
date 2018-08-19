package server02;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver {

	private Server server;
	private DatagramSocket socket;
	private int port;
	
	private Thread thread;
	
	public Receiver(Server server, int index) {
		this.server = server;
		port = server.getBasePort() + index;
	}
	
	public void start() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		thread = new Thread(() -> {
			run();
		}, "Receiving_Thread_" + (port - server.getBasePort()));
		thread.start();
	}
	
	private void run() {
		while(true) {
			System.out.println("reveiver " + (port - server.getBasePort()));
		}
	}
	
	public void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

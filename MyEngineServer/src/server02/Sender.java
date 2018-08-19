package server02;

import java.net.DatagramSocket;

public class Sender {

	private Server server;
	private int index;
	
	private Thread thread;
	
	public Sender(Server server, int index) {
		this.server = server;
		this.index = index;
	}
	
	public void start() {
		thread = new Thread(() -> {
			run();
		}, "Sender_Thread_" + index);
		thread.start();
	}
	
	private void run() {
		while(true) {
			System.out.println("sender " + index);
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

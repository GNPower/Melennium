package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyEngineServer {

	public static void main(String[] args) {
		Server server = new Server(8192);
		
		server.start();
		
		InetAddress address = null;
		
		try {
			address = InetAddress.getByName("192.168.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		int port = 8192;
		server.send(new byte[] {0, 1, 3, 6}, address, port);
	}
}

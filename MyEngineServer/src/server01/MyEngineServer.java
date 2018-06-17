package server01;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyEngineServer {

	public static void main(String[] args) {
		Server server = new Server(25565);
		
		server.start();
		
		while(true){
			
		}
	}
}

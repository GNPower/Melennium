package server02;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GameServer extends Thread{

	private DatagramSocket socket;
	
	public GameServer(){
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}	
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = new String(packet.getData());
			System.out.println("Client [" + packet.getAddress().getHostAddress() + " : " + packet.getPort() + "] > " + message);
			if(message.trim().equalsIgnoreCase("ping")){
				System.out.println("Returning pong");
				send("pong".getBytes(), packet.getAddress(), packet.getPort());
			}				
		}
	}
	
	public void send(byte[] data, InetAddress ipAddress, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

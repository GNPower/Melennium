package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import serialization.containers.MSDatabase;
import util.dataStructures.BinaryWriter;

public class Client {
	
	private final static byte[] PACKET_HEADER = new byte[] {0x40, 0x40};
	private final static byte PACKET_TPYE_CONNECT = 0x01;

	public enum Error {
		NONE, INVALID_HOST, SOCKET_EXCEPTION,
	}

	private String ipAddress;
	private int port;
	private Error errorCode = Error.NONE;

	private InetAddress serverAddress;

	private DatagramSocket socket;

	/**
	 * Format: 192.168.1.1:5000
	 * 
	 * @param host
	 *            the host public ip
	 */
	public Client(String host) {
		String[] parts = host.split(":");
		if (parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		ipAddress = parts[0];
		try {
			port = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
	}

	public Client(String host, int port) {
		this.ipAddress = host;
		this.port = port;
	}

	public boolean connect() {
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			errorCode = Error.SOCKET_EXCEPTION;
			return false;
		}
		sendConnectionPacket();
		//wait for server to reply
		return true;
	}
	
	private void sendConnectionPacket(){
		BinaryWriter writer = new BinaryWriter();
		writer.write(PACKET_HEADER);
		writer.write(PACKET_TPYE_CONNECT);
		send(writer.getBuffer());
	}

	public void send(byte[] data) {
		assert (socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(MSDatabase database){
		byte[] data = new byte[database.getSize()];
		database.getBytes(data, 0);
		send(data);
	}

	public Error getErrorCode() {
		return errorCode;
	}
}

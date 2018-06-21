package server01;

import java.net.InetAddress;

public class Address {

	private InetAddress address;
	private int port;
	private String id;
	
	public Address(InetAddress address ,int port) {
		this.address = address;
		this.port = port;
		id = address.toString() + ":" + port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String getId() {
		return id;
	}
	
	public static String createAddressID(InetAddress address, int port) {
		return address.toString() + ":" + port;
	}
}

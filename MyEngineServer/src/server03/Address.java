package server03;

import java.net.InetAddress;

public class Address {

	private InetAddress ipaddress;
	private int port;
	private String address;
	
	public Address(InetAddress ipaddress, int port) {
		this.ipaddress = ipaddress;
		this.port = port;
		address = ipaddress + ":" + port;
	}
	
	public String getAddressAsString() {
		return address;
	}
	
	public InetAddress getIpAddress() {
		return ipaddress;
	}
	
	public int getPort() {
		return port;
	}
}

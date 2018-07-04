package server03;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Sender {

	private Server initHost;
	private Hashtable<Address, Client> clients;
	
	private Thread sendThread;
	private boolean sending = false;
	
	private volatile List<byte[]> globalData;
	private Hashtable<Address, ArrayList<byte[]>> specificData;

	public Sender(Server initializingHost, int clientLimit){
		initHost = initializingHost;
		clients = new Hashtable<Address, Client>(clientLimit);
	}
	
	public void intitialize() {
		if(sending)
			return;
		sending = true;
		sendThread = new Thread( () -> {
			run();
		}, "MillenniumServer-Sender");
		sendThread.start();
	}
	
	private void run() {
		System.out.println("Sending Thread Started Successfully!");
		globalData = Collections.synchronizedList(new ArrayList<byte[]>());
		specificData = new Hashtable<Address, ArrayList<byte[]>>();
		while(sending) {
			sendToAllClients();
			sendToSpecificClients();
		}
	}
	
	public void sendGlobally(byte[] data) {
		globalData.add(data);
	}
	
	public void sendSpecifically(byte[] data, Address receiver) {
		if(!specificData.containsKey(receiver)) {
			System.out.println("adding a new send address");
			specificData.put(receiver, new ArrayList<byte[]>());
		}
		specificData.get(receiver).add(data);
	}
	
	private void sendToAllClients() {
		if(!initHost.hasClients)
			return;
		Collection<Client> clients = initHost.getClientList().values();
		List<byte[]> dataList = globalData;		
		for(Client client : clients) {
			for(byte[] data : dataList) {
				send(client.getAddress().getIpAddress(), client.getAddress().getPort(), data);
			}
		}
		globalData.clear();
	}
	
	private void sendToSpecificClients() {
		if(specificData.isEmpty())
			return;
		//System.out.println("copying data");
		Hashtable<Address, ArrayList<byte[]>> map = specificData;
		Iterator<Entry<Address, ArrayList<byte[]>>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			//System.out.println("its got a next");
			Entry<Address, ArrayList<byte[]>> sendAddress = iterator.next();
			for(byte[] data : sendAddress.getValue()) {
				System.out.println("calling send");
				send(sendAddress.getKey().getIpAddress(), sendAddress.getKey().getPort(), data);
			}
		}
		specificData.clear();
	}
	
	private void send(InetAddress address, int port, byte[] data) {
//		if(!initHost.getSocket().isConnected()) {
//			System.out.println("socket not connected");
//			return;
//		}
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			initHost.getSocket().send(packet);
			System.out.println("sent a packet");
		} catch (IOException e) {
			System.err.println("Failed to send a packet!");
			e.printStackTrace();
		}
	}
}

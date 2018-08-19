package server01;

import java.net.InetAddress;

import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSObject;

public class ServerClient {

	public int userID;
	public boolean connected = false;
	private volatile Address storedAddress;
	
	private static int userIDCounter = 1;
	
	private byte[] modelData;
	public volatile String username;
	public volatile float[] positions;
	public volatile float[] rotations;
	public volatile float scale;
	public volatile boolean isSet = false;
	public volatile double connectionConfirmedLast = 0;
	
	public ServerClient(Address address){
		System.out.println("Server Has " + userIDCounter + " Connected Clients");
		userID = userIDCounter++;
		connected = true;
		storedAddress = address;
	}
	
	public void setPlayerID(MSDatabase database){
		MSObject object = database.findObject("Player");
		username = object.findString("username").getString();
		scale = object.findField("scale").getFloat();
		positions = object.findArray("positions").floatData;
		rotations = object.findArray("rotations").floatData;
		isSet = true;
	}
	
	public void update(MSDatabase database){
		MSObject object = database.findObject(username);
		positions = object.findArray("positions").floatData;
		rotations = object.findArray("rotations").floatData;
	}
	
	public void confirmConnection(double time) {
		connectionConfirmedLast = time;
	}
	
	public MSDatabase serialize(){
		return null;
	}
	
	public int hashCode(){
		return userID;
	}
	
	public MSObject getUpdate(){
		MSObject object = new MSObject(username);
		try{
			object.addArray(MSArray.Float("positions", positions));
			object.addArray(MSArray.Float("rotations", rotations));
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return object;
	}
	
	public InetAddress getAddress(){
		return storedAddress.getAddress();
	}
	
	public int getPort(){
		return storedAddress.getPort();
	}
	
	public String getAddressId() {
		return storedAddress.getId();
	}
	
	public double getLastConnectionConfirmation() {
		return connectionConfirmedLast;
	}
}

package server01;

import java.net.InetAddress;

import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSObject;

public class ServerClient {

	public int userID;
	private volatile InetAddress address;
	private volatile int port;
	public boolean connected = false;
	
	private static int userIDCounter = 1;
	
	private byte[] modelData;
	public volatile String username;
	public volatile float[] positions;
	public volatile float[] rotations;
	public volatile float scale;
	public volatile boolean isSet = false;
	
	public ServerClient(InetAddress address, int port){
		userID = userIDCounter++;
		this.address = address;
		this.port = port;
		connected = true;
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
		MSObject object = database.findObject("Player");
		positions = object.findArray("positions").floatData;
		rotations = object.findArray("rotations").floatData;
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
		return address;
	}
	
	public int getPort(){
		return port;
	}
}

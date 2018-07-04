package server03;

import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSObject;
public class Client {

	private volatile Address storedAddress;

	public volatile String username;
	public volatile float[] positions;
	public volatile float[] rotations;
	public volatile float scale;
	public volatile boolean connected = false;
	
	public Client(Address address){
		storedAddress = address;
	}
	
	public void setPlayerID(MSDatabase database){
		MSObject object = database.findObject("Player");
		username = object.findString("username").getString();
		scale = object.findField("scale").getFloat();
		positions = object.findArray("positions").floatData;
		rotations = object.findArray("rotations").floatData;
		connected = true;
	}
	
	public void update(MSDatabase database){
		MSObject object = database.findObject("Player");
		positions = object.findArray("positions").floatData;
		rotations = object.findArray("rotations").floatData;
	}
	
	public MSObject getData(){
		MSObject object = new MSObject(username);
		try{
			object.addArray(MSArray.Float("positions", positions));
			object.addArray(MSArray.Float("rotations", rotations));
		}catch(NullPointerException e){
			System.err.println("Could not store client positions and/or rotations into an MSDatabase");
			e.printStackTrace();
		}
		return object;
	}
	
	public Address getAddress(){
		return storedAddress;
	}
}

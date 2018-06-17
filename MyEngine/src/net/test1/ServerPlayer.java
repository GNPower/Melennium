package net.test1;

import renderEngine.entities.Player;
import renderEngine.models.Model;
import serialization.containers.MSObject;
import util.maths.vectors.Vector3f;

public class ServerPlayer extends Player{

	private String username;
	private static Model playerModel;
	
	public ServerPlayer(float[] pos, float[] rot) {	
		super(playerModel, new Vector3f(pos[0], pos[1], pos[2]), new Vector3f(rot[0], rot[1], rot[2]), 1);		
	}
	
	public void update(MSObject object){
		float[] pos = object.findArray("positions").floatData;
		float[] rot = object.findArray("rotations").floatData;
		super.setPosition(new Vector3f(pos[0], pos[1], pos[2]));
		super.setRotation(new Vector3f(rot[0], rot[1], rot[2]));
	}

	public static Model getPlayerModel() {
		return playerModel;
	}

	public static void setPlayerModel(Model playerModel) {
		ServerPlayer.playerModel = playerModel;
	}
}
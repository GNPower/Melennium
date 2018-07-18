package net;

import core.math.Vec3f;
import core.model.Model;
import core.modules.entities.Entity;
import core.utils.Constants;
import serialization.containers.MSObject;

public class EntityInterface extends Entity{
	
	private String username;

	protected EntityInterface(String username, float[] positions, float[] rotations) {
		super(Constants.DEFAULT_MODEL_LOCATION, 
				new Vec3f(positions[0], positions[1], positions[2]), 
				new Vec3f(rotations[0], rotations[1], rotations[2]));
		this.username = username;
	}
	
	public void update(MSObject object) {
		float[] pos = object.findArray("positions").floatData;
		float[] rot = object.findArray("rotations").floatData;
		
		setPosition(new Vec3f(pos[0], pos[1], pos[2]));
		setRotation(new Vec3f(rot[0], rot[1], rot[2]));
	}
	
	public void render() {
		super.render();
	}
	
	public MSObject serialize() {
		return new MSObject("");
	}
	
	public String getName() {
		return username;
	}
}

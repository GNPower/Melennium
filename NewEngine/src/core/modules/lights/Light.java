package core.modules.lights;

import core.math.Vec3f;

public class Light {

	protected Vec3f position, colour;
	
	public Light(Vec3f position, Vec3f colour) {
		this.position = position;
		this.colour = colour;
	}

	public Vec3f getPosition() {
		return position;
	}

	public void setPosition(Vec3f position) {
		this.position = position;
	}

	public Vec3f getColour() {
		return colour;
	}

	public void setColour(Vec3f colour) {
		this.colour = colour;
	}
}

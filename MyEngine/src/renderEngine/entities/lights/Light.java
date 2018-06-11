package renderEngine.entities.lights;

import util.colours.Colour;
import util.maths.vectors.Vector3f;

public class Light {

	private Vector3f position;
	private Colour colour;
	
	public Light(Vector3f position, Colour colour) {
		super();
		this.position = position;
		this.colour = colour;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Colour getColour() {
		return colour;
	}
	
	public Vector3f getColourVector(){
		return colour.getColour();
	}
	
	public void setColour(Colour colour) {
		this.colour = colour;
	}
}

package renderEngine.entities.lights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.colours.Colour;
import util.maths.vectors.Vector3f;

public class Light {

	private Vector3f position;
	private Colour colour;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	private float brightnessFactor;
	
	public Light(Vector3f position, Colour colour) {
		super();
		this.position = position;
		this.colour = colour;
		brightnessFactor = 100;
	}	
	
	public Light(Vector3f position, Colour colour, float brightnessFactor) {
		super();
		this.position = position;
		this.colour = colour;
		setBrightness(brightnessFactor);
	}	
	
	public Light(Vector3f position, Colour colour, Vector3f attenuation) {
		super();
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
		brightnessFactor = 100;
	}
	
	public Light(Vector3f position, Colour colour, Vector3f attenuation, float brightnessFactor) {
		super();
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
		setBrightness(brightnessFactor);
	}
	
	public static List<Light> sortLights(List<Light> lights, Vector3f camPos) {
		Light[] result = lights.toArray(new Light[lights.size()]);
		
		Light temp;
		boolean isSorted;
		
		for(int i = 0; i < result.length; i++) {
			isSorted = true;
			
			for(int j = 1; j < result.length - 1; j++) {
				if(Vector3f.distance(result[j - 1].getPosition(), camPos) > Vector3f.distance(result[j].getPosition(), camPos)) {
					temp = result[j - 1];
					result[j - 1] = result[j];
					result[j] = temp;
					isSorted = false;
				}
			}
			
			if(isSorted)
				break;
		}
		
		return new ArrayList<Light>(Arrays.asList(result));
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
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

	public float getBrightnessFactor() {
		return brightnessFactor / 100;
	}
	
	public float getBrightness() {
		return brightnessFactor;
	}

	public void setBrightness(float brightnessFactor) {
		if(brightnessFactor > 100)
			this.brightnessFactor = 100;
		else if(brightnessFactor < 0)
			this.brightnessFactor = 0;
		else
			this.brightnessFactor = brightnessFactor;
	}
	
	
}

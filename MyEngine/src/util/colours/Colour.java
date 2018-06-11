package util.colours;

import util.maths.vectors.Vector3f;

public class Colour {

	private Vector3f colour;
	private float alpha;
	
	public Colour(){
		colour = new Vector3f(0, 0, 0);
		alpha = 1;
	}
	
	public Colour(Vector3f colour){
		this.colour = colour;
		this.alpha = 1;
	}
	
	public Colour(Vector3f colour, float alpha){
		this.colour = colour;
		this.alpha = alpha;
	}
	
	public Colour(float r, float g, float b){
		this.colour = new Vector3f(r, g, b);
		this.alpha = 1;
	}
	
	public Colour(float r, float g, float b, float a){
		this.colour = new Vector3f(r, g ,b);
		this.alpha = a;
	}

	public Vector3f getColour() {
		return colour;
	}

	public float getAlpha() {
		return alpha;
	}
	
	public float getR(){
		return colour.getX();
	}
	
	public float getG(){
		return colour.getY();
	}
	
	public float getB(){
		return colour.getZ();
	}
	
	public void setColour(Vector3f col){
		colour.setX(col.getX());
		colour.setY(col.getY());
		colour.setZ(col.getZ());
	}
	
	public void setColour(float r, float g, float b){
		colour.setX(r);
		colour.setY(g);
		colour.setZ(b);
	}
}

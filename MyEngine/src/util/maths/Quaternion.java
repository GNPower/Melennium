package util.maths;

import util.maths.vectors.Vector3f;

public class Quaternion {

	private float x, y, z, w;
	
	public Quaternion(float x, float y, float z, float w){
		this.x = x;
		this.y = y; 
		this.z = z;
		this.w =w;
	}
	
	public Quaternion(Vector3f v, float w){
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.w = w;
	}
	
	public float length(){
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion normalize(){
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	
	public Quaternion conjugate(){
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(Quaternion r)
	{
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(x_, y_, z_, w_);
	}

	public Quaternion mul(Vector3f r)
	{
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ =  w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ =  w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ =  w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion div(float r)
	{
		float w_ = w/r;
		float x_ = x/r;
		float y_ = y/r;
		float z_ = z/r;
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion mul(float r)
	{
		float w_ = w*r;
		float x_ = x*r;
		float y_ = y*r;
		float z_ = z*r;
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion sub(Quaternion r)
	{
		float w_ = w - r.getW();
		float x_ = x - r.getX();
		float y_ = y - r.getY();
		float z_ = z - r.getZ();
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion add(Quaternion r)
	{
		float w_ = w + r.getW();
		float x_ = x + r.getX();
		float y_ = y + r.getY();
		float z_ = z + r.getZ();
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Vector3f xyz(){
		return new Vector3f(x, y, z);
	}
	
	public String toString(){
		return "[" + this.x + "," + this.y + "," + this.z + "," + this.w + "]";
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
}

package util.maths.vectors;

import util.maths.Quaternion;

public class Vector3f {

	private float x, y, z;

	public Vector3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector3f v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public static float distance(Vector3f v1, Vector3f v2){
		v2.sub(v1);
		return v2.length();
	}
	
	public Vector3f normalize(){
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		
		return this;
	}
	
	public float dot(Vector3f v){
		return x * v.getX() + y * v.getY() + z * v.getZ();
	}
	
	public Vector3f cross(Vector3f v){
		float x_ = y * v.getZ() - z * v.getY();
		float y_ = z * v.getX() - x * v.getZ();
		float z_ = x * v.getY() - y * v.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f rotate(float angle, Vector3f axis){
		float sinHalfAngle = (float) Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float) Math.cos(Math.toRadians(angle / 2));
		
		float rotX = axis.getX() * sinHalfAngle;
		float rotY = axis.getY() * sinHalfAngle;
		float rotZ = axis.getZ() * sinHalfAngle;
		float rotW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rotX, rotY, rotZ, rotW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		x = w.getX();
		y = w.getY();
		z = w.getZ();
		
		return this;
	}
	
	public Vector3f invert(){
		return new Vector3f(-x, -y, -z);
	}

	public void add(Vector3f v) {
		this.x += v.getX();
		this.y += v.getY();
		this.z += v.getZ();
	}

	public void add(float f) {
		this.x += f;
		this.y += f;
		this.z += f;
	}
	
	public void sub(Vector3f v) {
		this.x -= v.getX();
		this.y -= v.getY();
		this.z -= v.getZ();
	}

	public void sub(float f) {
		this.x -= f;
		this.y -= f;
		this.z -= f;
	}
	
	public void mul(Vector3f v) {
		this.x *= v.getX();
		this.y *= v.getY();
		this.x *= v.getZ();
	}

	public void mul(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
	}
	
	public void div(Vector3f v) {
		this.x /= v.getX();
		this.y /= v.getY();
		this.z /= v.getZ();
	}

	public void div(float f) {
		this.x /= f;
		this.y /= f;
		this.z /= f;
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
	
	public String toString(){
		return "X: " + this.x + "	Y: " + this.y + "	Z: " + this.z;			
	}
	
	public float[] toArray(){
		return new float[] {this.x, this.y, this.z};
	}
}

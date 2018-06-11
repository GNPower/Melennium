package util.maths.vectors;

public class Vector2f {

	private float x, y;

	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f v) {
		this.x = v.getX();
		this.y = v.getY();
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public static float distance(Vector2f vec1, Vector2f vec2){
		Vector2f ans = new Vector2f(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
		return ans.length();
	}
	
	public float dot(Vector2f v){
		return x * v.getX() + y * v.getY();
	}
	
	public Vector2f normalize(){
		float length = length();
		
		x /= length;
		y /= length;
		
		return this;
	}

	public void add(Vector2f v) {
		this.x += v.getX();
		this.y += v.getY();
	}

	public void add(float f) {
		this.x += f;
		this.y += f;
	}
	
	public void sub(Vector2f v) {
		this.x -= v.getX();
		this.y -= v.getY();
	}

	public void sub(float f) {
		this.x -= f;
		this.y -= f;
	}
	
	public void mul(Vector2f v) {
		this.x *= v.getX();
		this.y *= v.getY();
	}

	public void mul(float f) {
		this.x *= f;
		this.y *= f;
	}
	
	public void div(Vector2f v) {
		this.x /= v.getX();
		this.y /= v.getY();
	}

	public void div(float f) {
		this.x /= f;
		this.y /= y;
	}
	
	public String toString(){
		return "[" + this.x + ", " + this.y + "]";
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
}

package core.maths;

public class Vector2f {
	
	private float X;
	private float Y;
	
	public Vector2f()
	{
		this.setX(0);
		this.setY(0);
	}
	
	public Vector2f(float x, float y)
	{
		this.setX(x);
		this.setY(y);
	}
	
	public Vector2f(Vector2f v)
	{
		this.X = v.getX();
		this.Y = v.getY();
	}
	
	public float length()
	{
		return (float) Math.sqrt(X*X + Y*Y);
	}
	
	public float dot(Vector2f r)
	{
		return X * r.getX() + Y * r.getY();
	}
	
	public Vector2f normalize()
	{
		float length = length();
		
		X /= length;
		Y /= length;
		
		return this;
	}
	
	public Vector2f add(Vector2f r)
	{
		return new Vector2f(this.X + r.getX(), this.Y + r.getY());
	}
	
	public Vector2f add(float r)
	{
		return new Vector2f(this.X + r, this.Y + r);
	}
	
	public Vector2f sub(Vector2f r)
	{
		return new Vector2f(this.X - r.getX(), this.Y - r.getY());
	}
	
	public Vector2f sub(float r)
	{
		return new Vector2f(this.X - r, this.Y - r);
	}
	
	public Vector2f mul(Vector2f r)
	{
		return new Vector2f(this.X * r.getX(), this.Y * r.getY());
	}
	
	public Vector2f mul(float r)
	{
		return new Vector2f(this.X * r, this.Y * r);
	}
	
	public Vector2f div(Vector2f r)
	{
		return new Vector2f(this.X / r.getX(), this.Y / r.getY());
	}
	
	public Vector2f div(float r)
	{
		return new Vector2f(this.X / r, this.Y / r);
	}
	
	public String toString()
	{
		return "[" + this.X + "," + this.Y + "]";
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}
	
	
	
}

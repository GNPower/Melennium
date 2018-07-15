package core.maths;


public class Vector3f {
	
	private float X;
	private float Y;
	private float Z;
	
	public Vector3f()
	{
		this.setX(0);
		this.setY(0);
		this.setZ(0);
	}
	
	public Vector3f(float x, float y, float z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	public Vector3f(Vector3f v)
	{
		this.X = v.getX();
		this.Y = v.getY();
		this.Z = v.getZ();
	}
	
	public float length()
	{
		return (float) Math.sqrt(X*X + Y*Y + Z*Z);
	}
	
	public float dot(Vector3f r)
	{
		return X * r.getX() + Y * r.getY() + Z * r.getZ();
	}
	
	public Vector3f cross(Vector3f r)
	{
		float x = Y * r.getZ() - Z * r.getY();
		float y = Z * r.getX() - X * r.getZ();
		float z = X * r.getY() - Y * r.getX();
		
		return new Vector3f(x,y,z);
	}
	
	public Vector3f normalize()
	{
		float length = this.length();
		
		X /= length;
		Y /= length;
		Z /= length;
		
		return this;
	}
	
	public Vector3f rotate(float angle, Vector3f axis)
	{
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));
		
		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		X = w.getX();
		Y = w.getY();
		Z = w.getZ();
		
		return this;
	}
	
	public Vector3f add(Vector3f r)
	{
		return new Vector3f(this.X + r.getX(), this.Y + r.getY(), this.Z + r.getZ());
	}
	
	public Vector3f add(float r)
	{
		return new Vector3f(this.X + r, this.Y + r, this.Z + r);
	}
	
	public Vector3f sub(Vector3f r)
	{
		return new Vector3f(this.X - r.getX(), this.Y - r.getY(), this.Z - r.getZ());
	}
	
	public Vector3f sub(float r)
	{
		return new Vector3f(this.X - r, this.Y - r, this.Z - r);
	}
	
	public Vector3f mul(Vector3f r)
	{
		return new Vector3f(this.X * r.getX(), this.Y * r.getY(), this.Z * r.getZ());
	}
	
	public Vector3f mul(float x, float y, float z)
	{
		return new Vector3f(this.X * x, this.Y * y, this.Z * z);
	}
	
	public Vector3f mul(float r)
	{
		return new Vector3f(this.X * r, this.Y * r, this.Z * r);
	}
	
	public Vector3f div(Vector3f r)
	{
		return new Vector3f(this.X / r.getX(), this.Y / r.getY(), this.getZ() / r.getZ());
	}
	
	public Vector3f div(float r)
	{
		return new Vector3f(this.X / r, this.Y / r, this.Z / r);
	}
	
	public Vector3f abs()
	{
		return new Vector3f(Math.abs(X), Math.abs(Y), Math.abs(Z));
	}
	
	public boolean equals(Vector3f v)
	{
		if (X == v.getX() && Y == v.getY() && Z == v.getZ())
			return true;
		else return false;
	}
	
	public String toString()
	{
		return "[" + this.X + "," + this.Y + "," + this.Z + "]";
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

	public float getZ() {
		return Z;
	}

	public void setZ(float z) {
		Z = z;
	}
	
	
	
}

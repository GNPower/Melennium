package renderEngine.models;

import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class Vertex {
	
	public static final int FLOATS = 8; 
	public static final int FLOAT = 3;
	public static final int BYTES = FLOATS * Float.BYTES;

	private Vector3f pos;
	private Vector3f normal;
	private Vector2f textureCoord;
	
	public Vertex(){
	}
	
	public Vertex(Vector3f pos){
		this.pos = pos;
		this.normal = new Vector3f(0, 0, 0);
		this.textureCoord = new Vector2f(0, 0);
	}
	
	public Vertex(Vector3f pos, Vector2f texturePos){
		this.pos = pos;
		this.textureCoord = texturePos;
		this.normal = new Vector3f(0, 0, 0);
	}
	
	public Vertex(Vector3f pos, Vector2f texture, Vector3f normal){
		this.pos = pos;
		this.textureCoord = texture;
		this.normal = normal;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	public Vector2f getTextureCoord() {
		return textureCoord;
	}

	public void setTextureCoord(Vector2f textureCoord) {
		this.textureCoord = textureCoord;
	}
}

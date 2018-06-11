package renderEngine.entities;

import renderEngine.models.Model;
import util.maths.vectors.Vector3f;

public class Entity {

	private Model model;
	private int textureIndex = 0;
	private Vector3f position, rotation;
	private float scale;

	public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Entity(Model model, Vector3f position, Vector3f rotation, float scale, int textureIndex) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}

	public Entity(Model model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotation.setX(rotX);
		this.rotation.setY(rotY);
		this.rotation.setZ(rotZ);
		this.scale = scale;
	}
	
	public float getTextureXOffset(){
		int column = textureIndex % model.getAtlasRows();
		return (float) column / (float) model.getAtlasRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex / model.getAtlasRows();
		return (float) row / (float) model.getAtlasRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
		position.add(new Vector3f(dx, dy, dz));
	}

	public void increasePosition(Vector3f displacement) {
		position.add(displacement);
	}

	public void increaseRotation(float rx, float ry, float rz) {
		rotation.add(new Vector3f(rx, ry, rz));
	}

	public void increaseRotation(Vector3f rotation) {
		this.rotation.add(rotation);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}

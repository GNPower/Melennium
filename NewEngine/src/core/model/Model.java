package core.model;

import core.texturing.Texture2D;

public class Model {

	private Mesh mesh;
	private Material material = null;
	private Texture2D texture = null;
	
	public Model(Mesh mesh)
	{
		this.mesh = mesh;
	}
	
	public Model() {
	}

	public Mesh getMesh() {
		return mesh;
	}
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Texture2D getTexture() {
		return texture;
	}

	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
}

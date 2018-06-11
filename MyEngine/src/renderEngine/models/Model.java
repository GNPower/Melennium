package renderEngine.models;

import renderEngine.models.buffers.VAO;
import renderEngine.textures.Texture2D;
import renderEngine.textures.TextureAtlas;
import renderEngine.textures.TexturePack;
import util.loaders.ModelLoader;
import util.loaders.ObjLoader;

public class Model {

	private Mesh mesh;
	private Texture2D texture;
	private TextureAtlas atlas;
	private boolean atlased;
	private TexturePack pack;
	private Material material;
	private VAO vao;
	
	public Model(Mesh mesh){
		this.mesh = mesh;
		this.material = new Material();
		atlased = false;
		createVAO();
	}
	
	public Model(Mesh mesh, Texture2D texture){
		this.mesh = mesh;
		this.texture = texture;
		this.pack = null;
		this.material = new Material();
		atlased = false;
		createVAO();
	}
	
	public Model(Mesh mesh, String textureFile){
		this.mesh = mesh;
		this.texture = new Texture2D(textureFile);
		this.pack = null;
		this.material = new Material();
		atlased = false;
		createVAO();
	}
	
	public Model(String objFile, String textureFile){
		this.mesh = ObjLoader.loadObjModel(objFile);
		this.texture = new Texture2D(textureFile);
		this.material = new Material();
		atlased = false;
		createVAO();		
	}
	
	public Model(Mesh mesh, Texture2D texture, Material material){
		this.mesh = mesh;
		this.texture = texture;
		this.material = material;
		atlased = false;
		createVAO();
	}
	
	public Model(String objFile, String textureFile, int rows){
		this.mesh = ObjLoader.loadObjModel(objFile);
		this.atlas = new TextureAtlas(textureFile, rows);
		this.material = new Material();
		atlased = true;
		createVAO();
	}
	
	public Model(Mesh mesh, TexturePack pack){
		this.mesh = mesh;
		this.pack = pack;
		this.texture = null;
		this.material = new Material();
		atlased = false;
		createVAO();
	}
	
	public Model(String objFile, TexturePack pack){
		this.mesh = ObjLoader.loadObjModel(objFile);
		this.pack = pack;
		this.texture = null;
		atlased = false;
		createVAO();
	}
	
	private void createVAO(){
		vao = ModelLoader.loadToVAO(mesh);
	}

	public TexturePack getPack() {
		return pack;
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
	
	public int getVaoID(){
		return vao.getId();
	}
	
	public VAO getVAO(){
		return vao;
	}

	public Texture2D getTexture() {
		if(atlased)
			return atlas.getTexture();
		return texture;
	}
	
	public int getAtlasRows(){
		if(atlased)
			return atlas.getNumberOfRows();
		return 1;
	}
}

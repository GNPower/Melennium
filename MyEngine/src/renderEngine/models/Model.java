package renderEngine.models;

import renderEngine.models.buffers.MeshVBO;
import renderEngine.models.buffers.VAO;
import renderEngine.textures.Texture2D;
import renderEngine.textures.TextureAtlas;
import renderEngine.textures.TexturePack;
import util.ResourceLoader;
import util.maths.vectors.Vector3f;

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
		this.mesh = ResourceLoader.loadObjModel(objFile);
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
		this.mesh = ResourceLoader.loadObjModel(objFile);
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
		this.mesh = ResourceLoader.loadObjModel(objFile);
		this.pack = pack;
		this.texture = null;
		atlased = false;
		createVAO();
	}
	
	private Model(float[] positions) {
		Vertex[] vertices = new Vertex[positions.length / 2];
		for(int i = 0; i < vertices.length; i++) {
			Vector3f vec = new Vector3f(positions[i * 2], positions[(i * 2) + 1], 0);
			vertices[i] = new Vertex(vec);
		}
		mesh = new Mesh(vertices);
		texture = null;
		atlased = false;
		pack = null;
		material = new Material();
		createGuiVAO(positions);
	}
	
	public static Model createQuadModel(float[] positions) {
		return new Model(positions);
	}
	
	private void createVAO(){
//		vao = ModelLoader.loadToVAO(mesh);
		vao = new VAO(mesh);
	}
	
	private void createGuiVAO(float[] positions) {
//		vao = ModelLoader.loadToVAO(positions);
		vao = new VAO(new MeshVBO(positions, 2));
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
	
	public void setTexture(Texture2D texture) {
		if(atlased) {
			atlas.setTexture(texture);
			return;
		}
		this.texture = texture;
	}
	
	public int getAtlasRows(){
		if(atlased)
			return atlas.getNumberOfRows();
		return 1;
	}
}

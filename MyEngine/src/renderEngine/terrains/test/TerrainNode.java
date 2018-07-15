package renderEngine.terrains.test;

import renderEngine.entities.Camera;
import util.dataStructures.Node;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class TerrainNode extends Node{

	private boolean isleaf;
	private TerrainConfig config;
	private int lod;
	private Vector2f location;
	private Vector3f worldPos;
	private Vector2f index;
	private float gap;
	private PatchVBO buffer;
	
	private Camera camera;
	
	public TerrainNode(PatchVBO buffer, TerrainConfig config, Vector2f location, int lod, Vector2f index, Camera camera) {
		this.buffer = buffer;
		this.config = config;
		this.index = index;
		this.lod = lod;
		this.location = location;
		this.gap = 1f/(TerrainQuadtree.getRootNodes() * (float)(Math.pow(2, lod)));
		
		this.camera = camera;
		
		Vector3f localScaling = new Vector3f(gap, 0, gap);
		Vector3f localTranslation = new Vector3f(location.getX(), 0, location.getY());
		
		setLocalTransform(new Matrix4f().initTranslation(localTranslation).mul(new Matrix4f().initScaling(localScaling)));
		setWorldTransform(new Matrix4f().initTranslation(new Vector3f(-config.getScaleXZ()/2f, 0, -config.getScaleXZ()/2f)).mul(new Matrix4f().initScaling(new Vector3f(config.getScaleXZ(), config.getScaleXZ(), config.getScaleXZ()))));
	
		computeWorldPosition();
		updateQuadtree(camera);
	}
	
	public void render() {
		if(isleaf) {
			//TODO: render me
			buffer.draw();
		}
		
		for(Node child : getChildren()) {
			child.render();
		}
	}
	
	/*
	 prepareTerrain(terrain);
			prepareInstance(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain(terrain.getModel());
			
			
			private void prepareTerrain(Terrain terrain) {
		GL30.glBindVertexArray(terrain.getModel().getVaoID());
		for (int i = 0; i < terrain.getModel().getVAO().getVboIndex(); i++) {
			GL20.glEnableVertexAttribArray(i);
		}

		shader.loadMaterial(terrain.getModel().getMaterial());

		bindTextures(terrain);
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		terrain.getModel().getTexture().bind();
 
 	private void unbindTerrain(Model model) {
		for (int i = 0; i < model.getVAO().getVboIndex(); i++) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrtix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				new Vector3f(0, 0, 0), 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
		
	}
	 */
	
	public void updateQuadtree(Camera camera) {
		if(camera.getPosition().getY() > config.getScaleY()) {
			worldPos.setY(config.getScaleY());
		}
		else {
			worldPos.setY(camera.getPosition().getY());
		}
		
		updateChildNodes(camera);
		
		for(Node child : getChildren()) {
			((TerrainNode) child).updateQuadtree(camera);
		}
	}
	
	private void addChildNodes(int lod) {
		if(isleaf) {
			isleaf = false;
		}
		if(getChildren().size() == 0) {
			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < 2; j++) {
					Vector2f loc = location;
					loc.add(new Vector2f(i*gap/2f,j*gap/2f));
					addChild(new TerrainNode(buffer, config, loc, lod, new Vector2f(i,j), camera));
				}
			}
		}
	}
	
	private void removeChildNodes() {
		if(!isleaf) {
			isleaf = true;
		}
		if(getChildren().size() != 0) {
			getChildren().clear();
		}
	}
	
	private void updateChildNodes(Camera camera) {
		Vector3f cam = camera.getPosition();
		cam.sub(worldPos);
		float distance = cam.length();
		
		if(distance < config.getLod_range()[lod]) {
			addChildNodes(lod+1);
		}else if(distance >= config.getLod_range()[lod]) {
			removeChildNodes();
		}
		
	}
	
	public void computeWorldPosition() {
		Vector2f loc = location;
		loc.add(gap/2f);
		loc.mul(config.getScaleXZ());
		loc.sub(config.getScaleXZ()/2f);
		
		worldPos = new Vector3f(loc.getX(), 0, loc.getY());
	}
	
	public boolean isIsleaf() {
		return isleaf;
	}
	public void setIsleaf(boolean isleaf) {
		this.isleaf = isleaf;
	}
	public TerrainConfig getConfig() {
		return config;
	}
	public void setConfig(TerrainConfig config) {
		this.config = config;
	}
	public int getLod() {
		return lod;
	}
	public void setLod(int lod) {
		this.lod = lod;
	}
	public Vector2f getLocation() {
		return location;
	}
	public void setLocation(Vector2f location) {
		this.location = location;
	}
	public Vector3f getWorldPos() {
		return worldPos;
	}
	public void setWorldPos(Vector3f worldPos) {
		this.worldPos = worldPos;
	}
	public Vector2f getIndex() {
		return index;
	}
	public void setIndex(Vector2f index) {
		this.index = index;
	}
	public float getGap() {
		return gap;
	}
	public void setGap(float gap) {
		this.gap = gap;
	}
	public PatchVBO getBuffer() {
		return buffer;
	}
	public void setBuffer(PatchVBO buffer) {
		this.buffer = buffer;
	}
}

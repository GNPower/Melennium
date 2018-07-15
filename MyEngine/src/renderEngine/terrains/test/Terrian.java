package renderEngine.terrains.test;

import renderEngine.entities.Camera;
import util.dataStructures.Node;

public class Terrian extends Node{

	private TerrainConfig config;
	private Camera camera;
	
	public void init(String config, Camera camera) {
		this.config = new TerrainConfig();
		this.config.loadFile(config);
		
		this.camera = camera;
		
		addChild(new TerrainQuadtree(this.config, camera));
	}
	
	public void updateQuadtree() {
		//TODO: only update if camera is moved
		((TerrainQuadtree) getChildren().get(0)).updateQuadtree();
	}

	public TerrainConfig getConfiguration() {
		return config;
	}

	public void setConfiguration(TerrainConfig config) {
		this.config = config;
	}
}

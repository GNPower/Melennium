package core.modules.terrain;

import core.kernel.Camera;
import core.scene.Node;

public class Terrain extends Node{

	private TerrainConfig config;
	
	public void  init(String config) {
		this.config = new TerrainConfig();
		this.config.loadFile(config);
		
		addChild(new TerrainQuadtree(this.config));
	}
	
	public void updateQuadtree() {
		if(Camera.getInstance().isCameraMoved());
			((TerrainQuadtree) getChildren().get(0)).updateQuadtree();
	}

	public TerrainConfig getConfig() {
		return config;
	}

	public void setConfig(TerrainConfig config) {
		this.config = config;
	}
}

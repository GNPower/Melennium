package renderEngine.terrains.test;

import renderEngine.entities.Camera;
import util.dataStructures.Node;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class TerrainQuadtree extends Node{

	private static int rootNodes = 8;
	
	private Camera camera;
	
	public TerrainQuadtree(TerrainConfig config, Camera camera) {
		
		PatchVBO buffer = new PatchVBO();
		buffer.allocate(generatePatch());
		
		this.camera = camera;
		
		for(int i = 0; i < rootNodes; i++) {
			for(int j = 0; j < rootNodes; j++) {
				addChild(new TerrainNode(buffer, config, new Vector2f(i/(float) rootNodes, j/(float) rootNodes), 0, new Vector2f(i,j), camera));
				//TODO:
			}
		}
		
		setWorldTransform(new Matrix4f().initTranslation(
				new Vector3f(config.getScaleXZ() / 2f, 0, config.getScaleXZ() / 2f))
				.mul(new Matrix4f().initScaling(new Vector3f(config.getScaleXZ(), 
				config.getScaleY(), config.getScaleXZ()))));
	}
	
	public void updateQuadtree() {
		for(Node child : getChildren()) {
			((TerrainNode) child).updateQuadtree(camera);
		}
	}
	
	public Vector2f[] generatePatch() {
		Vector2f[] vertices = new Vector2f[16];
		
		int index = 0;
		
		vertices[index++] = new Vector2f(0, 0);
		vertices[index++] = new Vector2f(0.333f, 0);
		vertices[index++] = new Vector2f(0.666f, 0);
		vertices[index++] = new Vector2f(1, 0);
		
		vertices[index++] = new Vector2f(0, 0.333f);
		vertices[index++] = new Vector2f(0.333f, 0.333f);
		vertices[index++] = new Vector2f(0.666f, 0.333f);
		vertices[index++] = new Vector2f(1, 0.333f);
		
		vertices[index++] = new Vector2f(0, 0.666f);
		vertices[index++] = new Vector2f(0.333f, 0.666f);
		vertices[index++] = new Vector2f(0.666f, 0.666f);
		vertices[index++] = new Vector2f(1, 0.666f);
		
		vertices[index++] = new Vector2f(0, 1);
		vertices[index++] = new Vector2f(0.333f, 1);
		vertices[index++] = new Vector2f(0.666f, 1);
		vertices[index++] = new Vector2f(1, 1);
		
		return vertices;
	}

	public static int getRootNodes() {
		return rootNodes;
	}

	public static void setRootNodes(int rootNodes) {
		TerrainQuadtree.rootNodes = rootNodes;
	}	
}
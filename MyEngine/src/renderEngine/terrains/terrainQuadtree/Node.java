package renderEngine.terrains.terrainQuadtree;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private Node parent;
	private List<Node> children;
	
	private int generation;
	private boolean isleaf;
	
	private TerrainConfig config;
	
	public Node(TerrainConfig config, int generation) {
		this.config = config;
		this.generation = generation;
		children = new ArrayList<Node>();
	}
	
	public void update() {
		for(Node child : children) {
			child.update();
		}
		//TODO: update node
	}
	
	public void render() {
		for(Node child : children) {
			child.render();
		}
		if(isleaf) {
			//TODO: render
		}
	}
	
	public void delete() {
		for(Node child : children) {
			child.delete();
		}
		//TODO: delete
	}
	
	
	public void addChild(Node child) {
		child.setParent(this);
		children.add(child);
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChildren() {
		return children;
	}

	public int getGeneration() {
		return generation;
	}

	public TerrainConfig getConfig() {
		return config;
	}
}

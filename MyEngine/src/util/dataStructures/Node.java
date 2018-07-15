package util.dataStructures;

import java.util.ArrayList;
import java.util.List;

import util.maths.matrices.Matrix4f;

public class Node {

	private  Node parent;
	private List<Node> children;
	private Matrix4f worldTransform;
	private Matrix4f localTransform;
	
	public Node() {
		worldTransform = new Matrix4f().initIdentity();
		children = new ArrayList<Node>();
	}
	
	public void addChild(Node child) {
		child.setParent(this);
		children.add(child);
	}
	
	public void update() {
		for(Node child : children) {
			child.update();
		}
	}
	
	public void input() {
		for(Node child : children) {
			child.input();
		}
	}
	
	public void render() {
		for(Node child : children) {
			child.render();
		}
	}
	
	public void shutdown() {
		for(Node child : children) {
			child.shutdown();
		}
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

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Matrix4f getWorldTransform() {
		return worldTransform;
	}

	public void setWorldTransform(Matrix4f transformationMatrix) {
		this.worldTransform = transformationMatrix;
	}

	public Matrix4f getLocalTransform() {
		return localTransform;
	}

	public void setLocalTransform(Matrix4f localTransform) {
		this.localTransform = localTransform;
	}
}

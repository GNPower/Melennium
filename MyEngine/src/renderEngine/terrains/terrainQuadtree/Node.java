package renderEngine.terrains.terrainQuadtree;

import util.maths.vectors.Vector2f;

public class Node {

	private Quadtree tree;
	private Node parent;
	
	private Node[] children = new Node[4];
	
	private float xPos, zPos ,xRange, zRange;
	private Vector2f[] positions;
	
	public Node(Quadtree tree, Node parent, float xPos, float zPos, float xRange, float zRange){
		this.tree = tree;
		this.parent = parent;
		this.xPos = xPos;
		this.zPos = zPos;
		this.xRange = xRange;
		this.zRange = zRange;
		
		tree.addRenderableQuad(this);
		
		positions = new Vector2f[4];
		positions[0] = new Vector2f(xPos, zPos);
		positions[1] = new Vector2f(xPos, zPos + zRange);
		positions[2] = new Vector2f(xPos + xRange, zPos);
		positions[3] = new Vector2f(xPos + xRange, zPos + zRange);
		System.out.println("p1: " + positions[0]);
		System.out.println("p2: " + positions[1]);
		System.out.println("p3: " + positions[2]);
		System.out.println("p4: " + positions[3]);
		
		System.out.println("X: " + xPos);
		System.out.println("Z: " + zPos);
		System.out.println("XRange: " + xRange);
		System.out.println("ZRange: " + zRange);
	}
	
	public void updateNode(){
//		if(closestCorner()){
//			
//		}else{
//			
//		}
	}
	
	private float closestCorner(){
		
		Vector2f camPos = new Vector2f(tree.getCamera().getPosition().getX(), tree.getCamera().getPosition().getZ());
		
		float c1 = Vector2f.distance(camPos, positions[0]);
		float c2 = Vector2f.distance(camPos, positions[1]);
		float c3 = Vector2f.distance(camPos, positions[2]);
		float c4 = Vector2f.distance(camPos, positions[3]);
		
//		System.out.println("C1: " + c1);
//		System.out.println("C2: " + c2);
//		System.out.println("C3: " + c3);
//		System.out.println("C4: " + c4);
		
		float a1 = Math.min(c1, c2);
		float a2 = Math.min(c3, c4);
		
		return Math.min(a1, a2);
	}
	
	private void split(){
		children[0] = new Node(tree, this, xPos, zPos, xRange / 2.0f, zRange / 2.0f);
		children[1] = new Node(tree, this, xPos, zPos + (zRange / 2.0f), xRange / 2.0f, zRange / 2.0f);
		children[2] = new Node(tree, this, xPos + (xRange / 2.0f), zPos + (zRange / 2.0f), xRange / 2.0f, zRange / 2.0f);
		children[3] = new Node(tree, this, xPos + (xRange / 2.0f), zPos, xRange / 2.0f, zRange / 2.0f);
	}

	public Node[] getChildren() {
		return children;
	}

	public float getxPos() {
		return xPos;
	}

	public float getzPos() {
		return zPos;
	}

	public float getxRange() {
		return xRange;
	}

	public float getzRange() {
		return zRange;
	}
}

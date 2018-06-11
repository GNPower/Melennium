package renderEngine.terrains.terrainQuadtree;

import java.util.ArrayList;
import java.util.List;

import renderEngine.entities.Camera;
import renderEngine.models.Vertex;
import renderEngine.terrains.Terrain;
import util.loaders.LODLoader;
import util.maths.vectors.Vector3f;

public class Quadtree {

	private final int maxSplits = 8;
	
	private List<Vertex> renderableVertices;
	private List<Integer> renderableIndices;
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private Node master;
	
	int[][] lodData;
	
	private Camera camera;
	private float xPos, zPos;
	
	private int splits = 0;
	
	public Quadtree(Camera camera, String lodFile, float xPos, float zPos){
		this.camera = camera;
		this.xPos = xPos;
		this.zPos = zPos;
		
		renderableVertices = new ArrayList<Vertex>();
		renderableIndices = new ArrayList<Integer>();
		
		lodData = LODLoader.loadLOD(lodFile);
		
//		master = new Node(this, null, xPos, zPos, Terrain.getSIZE(), Terrain.getSIZE());
		updateTree();
	}
	
	public void updateTree(){
		master.updateNode();
	}
	
	public void addRenderableQuad(Node node){
		generateQuad(renderableVertices, renderableIndices, 
				node.getxPos(), node.getzPos(), node.getxRange(), node.getzRange());
	}
	
	private void generateQuad(List<Vertex> vertices, List<Integer> indices, float xPos, float zPos, float xRange, float zRange){
		
		Vertex vert0 = new Vertex(new Vector3f(xPos, 0, zPos));
		Vertex vert1 = new Vertex(new Vector3f(xPos,0, zPos + zRange));
		Vertex vert2 = new Vertex(new Vector3f(xPos + xRange, 0, zPos + zRange));
		Vertex vert3 = new Vertex(new Vector3f(xPos + xRange, 0, zPos));
		
		vertices.add(vert0);
		vertices.add(vert1);
		vertices.add(vert2);
		vertices.add(vert3);
		
		indices.add(vertices.indexOf(vert0));
		indices.add(vertices.indexOf(vert1));
		indices.add(vertices.indexOf(vert3));
		
		indices.add(vertices.indexOf(vert3));
		indices.add(vertices.indexOf(vert1));
		indices.add(vertices.indexOf(vert2));
	}

	public List<Vertex> getRenderableVertices() {
		return renderableVertices;
	}

	public List<Integer> getRenderableIndices() {
		return renderableIndices;
	}
	
	public Camera getCamera(){
		return camera;
	}
}

package renderEngine.models;

public class Mesh {

	private Vertex[] vertices;
	private int[] indices;
	
	public Mesh(){
		
	}
	
	public Mesh(Vertex[] vertices){
		this.vertices = vertices;
	}

	public Mesh(Vertex[] vertices, int[] indices){
		this.vertices = vertices;
		this.indices = indices;
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}
	
	public int getVertexCount(){
		return indices.length;
	}
}

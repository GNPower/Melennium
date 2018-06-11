package renderEngine.terrains;

import java.awt.image.BufferedImage;

import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.models.Vertex;
import renderEngine.textures.Texture2D;
import renderEngine.textures.TexturePack;
import util.loaders.Textures;
import util.maths.Maths;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class Terrain {
	
	public static int TERRAIN_SIZE = 800;
	private static int VERTEX_COUNT = 128;
	
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

	private float x, z;
	private Model model;
	private Texture2D blendMap;
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, TexturePack pack, String blendMap){
		this.x = gridX * TERRAIN_SIZE;
		this.z = gridZ * TERRAIN_SIZE;
		this.blendMap = new Texture2D(blendMap);
		model = new Model(generateBasicTerrain(VERTEX_COUNT), pack);
	}
	
	public Terrain(int gridX, int gridZ, TexturePack pack, String blendMap, String heightMap){
		this.x = gridX * TERRAIN_SIZE;
		this.z = gridZ * TERRAIN_SIZE;
		this.blendMap = new Texture2D(blendMap);
		model = new Model(generateHeightMappedTerrain(Textures.loadImage(heightMap)), pack);
	}
	
	private Mesh generateBasicTerrain(int vertexCount){
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		
		Vertex[] vertexArray = new Vertex[count];				
		int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];				
	
		int vertexPointer = 0;
		for(int i=0;i<vertexCount;i++){
			for(int j=0;j<vertexCount;j++){				
				vertexArray[vertexPointer] = new Vertex(new Vector3f((float)j/((float)vertexCount - 1) * TERRAIN_SIZE, 0, 
						(float)i/((float)vertexCount - 1) * TERRAIN_SIZE), new Vector2f((float)j/((float)vertexCount - 1), 
						(float)i/((float)vertexCount - 1)), new Vector3f(0, 1, 0));		
				
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vertexCount-1;gz++){
			for(int gx=0;gx<vertexCount-1;gx++){
				int topLeft = (gz*vertexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vertexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return new Mesh(vertexArray, indices);
	}
	
	private Mesh generateHeightMappedTerrain(BufferedImage heightMap){
		int vertexCount = heightMap.getHeight();
		
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		
		Vertex[] vertexArray = new Vertex[count];				
		int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
		heights = new float[vertexCount][vertexCount];
	
		int vertexPointer = 0;
		for(int i=0;i<vertexCount;i++){
			for(int j=0;j<vertexCount;j++){			
				float height = getHeight(j, i, heightMap);
				heights[j][i] = height;
				vertexArray[vertexPointer] = new Vertex(new Vector3f((float)j/((float)vertexCount - 1) * TERRAIN_SIZE, 
						height, (float)i/((float)vertexCount - 1) * TERRAIN_SIZE), 
						new Vector2f((float)j/((float)vertexCount - 1), (float)i/((float)vertexCount - 1)), 
						calculateNormal(j, i, heightMap));				
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vertexCount-1;gz++){
			for(int gx=0;gx<vertexCount-1;gx++){
				int topLeft = (gz*vertexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vertexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return new Mesh(vertexArray, indices);
	}
	
	private float getHeight(int x, int y, BufferedImage image){
		if(x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()){
			return 0;
		}
		float height = image.getRGB(x, y);
		height += MAX_PIXEL_COLOUR / 2f;
		height /= MAX_PIXEL_COLOUR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal = normal.normalize();
		return normal;
	}
	
	public float getHeight(float x, float z){
		float terrainX = x - this.x;
		float terrainZ = z - this.z;
		float gridSquareSize = TERRAIN_SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
			return 0;
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		
		float answer;
		if(xCoord <= (1-zCoord)){
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), 
					new Vector3f(1, heights[gridX + 1][gridZ], 0), 
					new Vector3f(0, heights[gridX][gridZ + 1], 1), 
					new Vector2f(xCoord, zCoord));
		}else{
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), 
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), 
					new Vector3f(0, heights[gridX][gridZ + 1], 1), 
					new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	public void setMesh(Mesh mesh){
		this.model.setMesh(mesh);
	}

	public Texture2D getBlendMap() {
		return blendMap;
	}
	
	public TexturePack getTexturePack(){
		return model.getPack();
	}
}

/*

	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	
	private float x, z;
	private Model model;
	private Camera cam;
	private int[][] lods;
	private Mesh[] lodMesh;
	
	private Quadtree tree;
	
	public Terrain(Camera camera, int gridX, int gridZ, String texture, boolean quadTree){
		Mesh mesh1 = generateTerrain();
		Mesh mesh2 = Utils.translateMesh(generateTerrain(), new Vector3f(-SIZE, 0, 0));
		//model = new Model(Utils.combineMesh(mesh1, mesh2), texture);
		
		//model = new Model(mesh1, texture);					
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		
		if(quadTree){
			tree = new Quadtree(camera, "terrainDetails.ldd",this.x, this.z);
			model = new Model(generateTerrain(tree.getRenderableVertices(), tree.getRenderableIndices()), texture);
			cam = null;
			lodMesh = null;
		}else{
			cam = camera;
			tree = null;
			lods = LODLoader.loadLOD("terrainDetails.ldd");
			lodMesh = new Mesh[lods.length];
			for(int i = 0; i < lods.length; i++){
				lodMesh[i] = generateBasicTerrain(lods[i][1]);
			}
			model = new Model(lodMesh[3], texture);		
			for(int i = 0; i < lods.length; i++){
				System.out.println("LOD: " + lods[i][0]);
			}
		}					
	}
	
	public void update(){
			float distance = closestCorner();
			for(int i = 0; i < lods.length; i++){
				if(distance > lods[i][0]){
					model.setMesh(lodMesh[i]);
					return;
				}
			}
	}
	
	private float closestCorner(){
		
		Vector2f camPos = new Vector2f(cam.getPosition().getX(), cam.getPosition().getZ());
		
		float c1 = Vector2f.distance(camPos, new Vector2f(x, z));
		float c2 = Vector2f.distance(camPos, new Vector2f(x, z + SIZE));
		float c3 = Vector2f.distance(camPos, new Vector2f(x + SIZE, z));
		float c4 = Vector2f.distance(camPos, new Vector2f(x + SIZE, z + SIZE));

		float a1 = Math.min(c1, c2);
		float a2 = Math.min(c3, c4);
		
		return Math.min(a1, a2);
	}
	
	private Mesh generateBasicTerrain(int vertexCount){
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		
		Vertex[] vertexArray = new Vertex[count];				
		int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
	
		int vertexPointer = 0;
		for(int i=0;i<vertexCount;i++){
			for(int j=0;j<vertexCount;j++){				
				vertexArray[vertexPointer] = new Vertex(new Vector3f((float)j/((float)vertexCount - 1) * SIZE, 0, 
						(float)i/((float)vertexCount - 1) * SIZE), new Vector2f((float)j/((float)vertexCount - 1), 
						(float)i/((float)vertexCount - 1)), new Vector3f(0, 1, 0));				
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vertexCount-1;gz++){
			for(int gx=0;gx<vertexCount-1;gx++){
				int topLeft = (gz*vertexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vertexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return new Mesh(vertexArray, indices);
	}
	
	private Mesh generateTerrain(List<Vertex> vertices, List<Integer> indices){
		Vertex[] vertexArray = new Vertex[vertices.size()];
		for(int i =0; i < vertices.size(); i++){
			vertexArray[i] = vertices.get(i);
		}
		
		int[] indexArray = new int[indices.size()];
		for(int i = 0; i < indices.size(); i++){
			indexArray[i] = indices.get(i);
		}
		
		return new Mesh(vertexArray, indexArray);
	}
	
	private Mesh generateTerrain(){
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Integer> indices = new ArrayList<Integer>();
		
		generateQuad(vertices, indices, 0, 0, 1, 1);
		
		Vertex[] vertexArray = new Vertex[vertices.size()];
		for(int i =0; i < vertices.size(); i++){
			vertexArray[i] = vertices.get(i);
		}
		
		int[] indexArray = new int[indices.size()];
		for(int i = 0; i < indices.size(); i++){
			indexArray[i] = indices.get(i);
		}
		
		return new Mesh(vertexArray, indexArray);
	}
	
	public void generateQuad(List<Vertex> vertices, List<Integer> indices, float xPos, float zPos, float xRange, float zRange){
		
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

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public Model getModel() {
		return model;
	}

	public Texture2D getTexture() {
		return model.getTexture();
	}
	
	public static float getSIZE(){
		return Terrain.SIZE;
	}
*/
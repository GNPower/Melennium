package renderEngine.terrains;

import java.util.ArrayList;
import java.util.List;

import renderEngine.entities.Camera;
import renderEngine.models.Mesh;
import renderEngine.models.Vertex;
import renderEngine.rendering.MasterRenderer;
import util.ResourceLoader;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class TerrainManager {

	public static final int TERRAIN_SIZE = 800;
	private final String CONFIG_FILE = "terrain.cfg";
	
	private MasterRenderer renderer;
	private Camera camera;
	
	private Mesh[] meshLOD;
	private int[] distanceLOD;

	private List<Terrain> terrains;
	
	public TerrainManager(MasterRenderer renderer, Camera camera){
		terrains = new ArrayList<Terrain>();
		this.renderer = renderer;
		this.camera = camera;
		createLodMap(ResourceLoader.loadLOD(CONFIG_FILE));
	}
	
	public void update(){		
	}
	
	public void render(){
		for(Terrain terrain : terrains){
			//float distance = closestCorner(camera, terrain);
			//terrain.setMesh(meshLOD[findMesh(distance)]);
			//terrain.setMesh(meshLOD[0]);
			renderer.processTerrain(terrain);
		}
	}
	
	private int findMesh(float distance){		
		
		for(int i = 0; i < distanceLOD.length; i++){
			if(distance > distanceLOD[i]){
				return i;
			}
		}
		return distanceLOD.length - 1;
	}
	
	private float closestCorner(Camera cam, Terrain ter){
		
		Vector2f camPos = new Vector2f(cam.getPosition().getX(), cam.getPosition().getZ());
		
		float c1 = Vector2f.distance(camPos, new Vector2f(ter.getX(), ter.getZ()));
		float c2 = Vector2f.distance(camPos, new Vector2f(ter.getX(), ter.getZ() + TERRAIN_SIZE));
		float c3 = Vector2f.distance(camPos, new Vector2f(ter.getX() + TERRAIN_SIZE, ter.getZ()));
		float c4 = Vector2f.distance(camPos, new Vector2f(ter.getX() + TERRAIN_SIZE, ter.getZ() + TERRAIN_SIZE));

		float a1 = Math.min(c1, c2);
		float a2 = Math.min(c3, c4);
		
		return Math.min(a1, a2);
	}
	
	private Mesh generateMesh(int vertexCount){
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
	
	private void createLodMap(int[][] lodData){				
		meshLOD = new Mesh[lodData.length];
		distanceLOD = new int[lodData.length];
		
		for(int i = 0; i < lodData.length; i++){
			distanceLOD[i] = lodData[i][0];
			meshLOD[i] = generateMesh(lodData[i][1]);
		}
	}
	
	public void addTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public Terrain getTerrain(float worldX, float worldZ) {
		for(Terrain terrain : terrains) {
			if(worldX >= terrain.getX() && worldX <= terrain.getX() + Terrain.TERRAIN_SIZE && worldZ >= terrain.getX() && worldZ <= terrain.getZ() + Terrain.TERRAIN_SIZE) {
				return terrain;
			}
		}
		return Terrain.returnEmptyTerrain();
	}
}
/*
 	private Mesh generateNode(int vertexCount){
		int count = vertexCount * vertexCount;
		
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
 */

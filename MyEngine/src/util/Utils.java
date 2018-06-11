package util;

import java.util.ArrayList;

import renderEngine.models.Mesh;
import renderEngine.models.Vertex;
import util.maths.vectors.Vector3f;

public class Utils {

	public static float[] createFloatArrayOfVertexPositions(Vertex[] vertices) {
		float[] floats = new float[vertices.length * Vertex.FLOAT];

		int index = 0;
		for (int i = 0; i < vertices.length; i++) {
			floats[index] = vertices[i].getPos().getX();
			index++;
			floats[index] = vertices[i].getPos().getY();
			index++;
			floats[index] = vertices[i].getPos().getZ();
			index++;
		}

		return floats;
	}

	public static float[] createFloatArrayOfVertexTextures(Vertex[] vertices) {
		float[] floats = new float[vertices.length * 2];

		int index = 0;
		for (int i = 0; i < vertices.length; i++) {
			floats[index] = vertices[i].getTextureCoord().getX();
			index++;
			floats[index] = vertices[i].getTextureCoord().getY();
			index++;
		}

		return floats;
	}

	public static float[] createFloatArrayOfVertexNormals(Vertex[] vertices) {
		float[] floats = new float[vertices.length * Vertex.FLOAT];

		int index = 0;
		for (int i = 0; i < vertices.length; i++) {
			floats[index] = vertices[i].getNormal().getX();
			index++;
			floats[index] = vertices[i].getNormal().getY();
			index++;
			floats[index] = vertices[i].getNormal().getZ();
			index++;
		}

		return floats;
	}

	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < data.length; i++) {
			if (!data[i].equals("")) {
				list.add(data[i]);
			}
		}

		String[] result = new String[list.size()];
		list.toArray(result);

		return result;
	}

	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];

		for (int i = 0; i < data.length; i++) {
			result[i] = data[i].intValue();
		}

		return result;
	}
	
	public static float[] combineArrays(float[] a1, float[] a2){
		float[] answer = new float[a1.length + a2.length];
		
		for(int i = 0; i < a1.length; i++){
			answer[i] = a1[i];
		}
		for(int i = 0; i < a2.length; i++){
			answer[a1.length + i] = a2[i];
		}
		
		return answer;
	}
	
	public static int[] combineIntArray(int[] a1, int[] a2){
		int[] answer = new int[a1.length + a2.length];
		
		for(int i = 0; i < a1.length; i++){
			answer[i] = a1[i];
		}
		for(int i = 0; i < a2.length; i++){
			answer[a1.length + i] = a2[i];
		}
		
		return answer;
	}
	
	public static Vertex[] combineVertexArray(Vertex[] v1, Vertex[] v2){
		Vertex[] answer = new Vertex[v1.length + v2.length];
		
		for(int i = 0; i < v1.length; i++){
			answer[i] = v1[i];
		}
		for(int i = 0; i < v2.length; i++){
			answer[v1.length + i] = v2[i];
		}
		
		return answer;
	}
	
	public static Mesh combineMesh(Mesh m1, Mesh m2){
		
		Vertex[] v1 = m1.getVertices();
		Vertex[] v2 = m2.getVertices();
		Vertex[] combinedV = combineVertexArray(v1, v2);
		
		int[] i1 = m1.getIndices();
		int[] i2 = m2.getIndices();
		int[] combinedI = new int[m1.getIndices().length + m2.getIndices().length];
		
		
		for(int i = 0; i < i1.length; i++){
			combinedI[i] = i1[i];
		}
		for(int i = 0; i < i2.length; i++){
			combinedI[i1.length + i] = i2[i] + i1.length;
		}
		
		return new Mesh(combinedV, combinedI);
	}
	
	public static Mesh translateMesh(Mesh mesh, Vector3f displacement){
		Vertex[] vertices = mesh.getVertices();
		for(int i = 0; i < vertices.length; i++){
			Vector3f pos = vertices[i].getPos();
			pos.add(displacement);
			vertices[i].setPos(pos);
		}		
		mesh.setVertices(vertices);
		
		return mesh;
	}
}

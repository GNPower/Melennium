package util.loaders;

import renderEngine.models.Mesh;
import renderEngine.models.buffers.VAO;
import util.Utils;

public class ModelLoader {

	public static VAO loadToVAO(Mesh mesh){
		VAO vao = new VAO();
		vao.bind();
		
		vao.addStaticIndices(mesh.getIndices());
		
		float[] vertices = Utils.createFloatArrayOfVertexPositions(mesh.getVertices());
		vao.addStaticVBO(vertices, 3);		
		
		float[] textureCoords = Utils.createFloatArrayOfVertexTextures(mesh.getVertices());
		vao.addStaticVBO(textureCoords, 2);
		
		float[] normals = Utils.createFloatArrayOfVertexNormals(mesh.getVertices());
		vao.addStaticVBO(normals, 3);
		
		vao.unbind();
		
		return vao;
	}
	
	public static VAO loadToVAO(float[] vertices, float[] texturesCoords, float[] normals, int[] indices){
		VAO vao = new VAO();
		vao.bind();
		
		vao.addStaticIndices(indices);
		
		vao.addStaticVBO(vertices, 3);
		
		vao.addStaticVBO(texturesCoords, 2);
		
		vao.unbind();
		return vao;
	}
}

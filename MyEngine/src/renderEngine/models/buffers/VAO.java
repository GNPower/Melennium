package renderEngine.models.buffers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import renderEngine.models.Mesh;
import util.Utils;

public class VAO {

	private static List<VAO> vaos = new ArrayList<VAO>();
	
	private int vaoId;
	
	private IndicesVBO indexVbo;
	private VBO[] vbos = new VBO[15];
	
	private int vboIndex;
	
	public VAO(IndicesVBO indices, MeshVBO... meshVbos) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		create(indices, meshVbos);
		vaos.add(this);
	}
	
	public VAO(Mesh mesh) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		create(new IndicesVBO(mesh.getIndices()),
				new MeshVBO(Utils.createFloatArrayOfVertexPositions(mesh.getVertices()), 3),
				new MeshVBO(Utils.createFloatArrayOfVertexTextures(mesh.getVertices()), 2),
				new MeshVBO(Utils.createFloatArrayOfVertexNormals(mesh.getVertices()), 3));
		vaos.add(this);
	}
	
	public VAO(MeshVBO meshVbo) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		create(meshVbo);
		vaos.add(this);
	}
	
	private void create(IndicesVBO indices, MeshVBO... meshVbos) {
		bind();
		indices.create();
		indexVbo = indices;
		for(MeshVBO vbo : meshVbos) {
			vbo.create(vboIndex);
			vbos[vboIndex++] = vbo;
		}
		unbind();
	}
	
	private void create(MeshVBO vbo) {
		bind();
		indexVbo = null;
		vbo.create(vboIndex);
		vbos[vboIndex++] = vbo;
		unbind();
	}

	public void bind() {
		GL30.glBindVertexArray(vaoId);
	}
	
	public void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	public void enable() {
		bind();
		for(int i = 0; i < vboIndex; i++) {
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	public void disable() {
		for(int i = 0; i < vboIndex; i++) {
			GL20.glDisableVertexAttribArray(i);
		}
		unbind();
	}
	
	public void render(boolean preBound, int dataLength) {
		if(!preBound)
			enable();
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, dataLength, GL11.GL_UNSIGNED_INT, 0);
		
		if(!preBound)
			disable();
	}
	
	public void delete() {
		GL30.glDeleteVertexArrays(vaoId);
		vaos.remove(this);
	}
	
	private void deleteVBOS() {
		for(int i = 0; i < vboIndex; i++) {
			GL15.glDeleteBuffers(vbos[i].getId());
		}
		vbos = null;
	}
	
	public int getId() {
		return vaoId;
	}
	
	public int getVboIndex() {
		return vboIndex;
	}
	
	public static void cleanUp() {
		for(VAO vao : vaos) {
			vao.deleteVBOS();
			GL30.glDeleteVertexArrays(vao.getId());
		}
		vaos.clear();
	}
}
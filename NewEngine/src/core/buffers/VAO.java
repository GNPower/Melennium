package core.buffers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import core.model.Mesh;
import core.utils.Util;

public class VAO {

private static List<VAO> vaos = new ArrayList<VAO>();
	
	private int vaoId;
	private boolean drawPatches;
	private int dataSize = -1;
	
	private IndicesVBO indexVbo;
	private VBO[] vbos = new VBO[15];
	
	private int vboIndex;
	
	public VAO(IndicesVBO indices, MeshVBO... meshVbos) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		create(indices, meshVbos);
		drawPatches = false;
		vaos.add(this);
	}
	
	public VAO(PatchVBO...patchVBOs) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		create(patchVBOs);
		drawPatches = true;
		vaos.add(this);
	}
	
	public VAO(Mesh mesh) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		dataSize = mesh.getIndices().length;
		create(new IndicesVBO(mesh.getIndices()),
				new MeshVBO(Util.createFloatArrayOfVertexPositions(mesh.getVertices()), 3),
				new MeshVBO(Util.createFloatArrayOfVertexTextures(mesh.getVertices()), 2),
				new MeshVBO(Util.createFloatArrayOfVertexNormals(mesh.getVertices()), 3));
		drawPatches = false;
		vaos.add(this);
	}
	
	public VAO(MeshVBO meshVbo) {
		vaoId = GL30.glGenVertexArrays();
		vboIndex = 0;
		create(meshVbo);
		drawPatches = false;
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
	
	private void create(PatchVBO...patchVBOs) {
		bind();
		indexVbo = null;
		for(PatchVBO vbo : patchVBOs) {
			vbo.create(vboIndex);
			dataSize = vbo.getDataSize();
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
	
	public void render(boolean preBound) {
		if(!preBound)
			enable();

		if(!drawPatches)
			GL11.glDrawElements(GL11.GL_TRIANGLES, dataSize, GL11.GL_UNSIGNED_INT, 0);
		else {
			GL11.glDrawArrays(GL40.GL_PATCHES, 0, dataSize);			
		}
		
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

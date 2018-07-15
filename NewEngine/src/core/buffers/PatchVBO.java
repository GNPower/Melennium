package core.buffers;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;

import core.math.Vec2f;
import core.utils.Util;

public class PatchVBO implements VBO{

	private int id;
	
	private Vec2f[] vertices;
	private int dataSize;
	
	public PatchVBO(Vec2f[] vertices) {
		id = GL15.glGenBuffers();
		dataSize = 2;
		this.vertices = vertices;
	}
	
	public void create(int index) {
		bind();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, dataSize, GL11.GL_FLOAT, false, 0, 0);
		GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, vertices.length);
		unbind();
	}
	
	@Override
	public void bind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
	}

	@Override
	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void delete() {
		GL15.glDeleteBuffers(id);
	}

	@Override
	public int getId() {
		return id;
	}
	
	public int getDataSize() {
		return vertices.length;
	}
}

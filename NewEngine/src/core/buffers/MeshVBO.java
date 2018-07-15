package core.buffers;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import core.utils.Util;

public class MeshVBO implements VBO{
	
	private int id;
	
	private float[] data;
	private int dataSize;

	public MeshVBO(float[] data, int dataSize) {
		id = GL15.glGenBuffers();
		this.data = data;
		this.dataSize = dataSize;
	}

	public void create(int index) {
		FloatBuffer buffer = Util.createFlippedBuffer(data);
		bind();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, dataSize, GL11.GL_FLOAT, false, 0, 0);
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
	
	public int getDataSize() {
		return dataSize;
	}
	
	public int getDataLength() {
		return data.length;
	}
	
	@Override
	public int getId() {
		return id;
	}
}

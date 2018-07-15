package renderEngine.models.buffers;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;

import util.buffers.BufferUtil;

public class IndicesVBO implements VBO{

	private int id;
	
	private int[] data;
	
	public IndicesVBO(int[] data) {
		id = GL15.glGenBuffers();
		this.data = data;
	}
	
	public void create() {
		IntBuffer buffer = BufferUtil.createFlippedBuffer(data);
		bind();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void bind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
	}

	@Override
	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	@Override
	public int getId() {
		return id;
	}

//	@Override
//	public void delete() {
//		GL15.glDeleteBuffers(id);
//	}
}

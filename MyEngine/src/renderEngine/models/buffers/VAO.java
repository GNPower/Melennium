package renderEngine.models.buffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import util.buffers.BufferUtil;

public class VAO {
	
	private static ArrayList<VAO> list = new ArrayList<VAO>();
	
	private int id;
	
	private int vboIndex;
	private VBO indexVbo;
	private VBO[] vbos = new VBO[15];

	public VAO(){
		create();
		vboIndex = 0;
		list.add(this);
	}
	
	private void create(){
		id = GL30.glGenVertexArrays();
	}
	
	public void addStaticVBO(float[] data, int dataSize){
		vbos[vboIndex] = new VBO();
		FloatBuffer buffer = BufferUtil.createFlippedBuffer(data);
		vbos[vboIndex].bind(VBO.ARRAY_BUFFER);
		GL15.glBufferData(VBO.ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(vboIndex, dataSize, GL11.GL_FLOAT, false, 0, 0);
		vbos[vboIndex].unbind(VBO.ARRAY_BUFFER);
		vboIndex++;
	}
	
	public void addStaticIndices(int[] data){
		indexVbo = new VBO();
		IntBuffer buffer = BufferUtil.createFlippedBuffer(data);
		indexVbo.bind(VBO.ELEMENT_ARRAY_BUFFER);
		GL15.glBufferData(VBO.ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	public void bind(){
		GL30.glBindVertexArray(id);
	}
	
	public void unbind(){
		GL30.glBindVertexArray(0);
	}
	
	public void delete(){
		GL30.glDeleteVertexArrays(id);
		list.remove(this);
	}

	public int getId() {
		return id;
	}

	public int getVboIndex() {
		return vboIndex;
	}
	
	public static void cleanUp(){
		for(VAO vao : list){
			GL30.glDeleteVertexArrays(vao.getId());
		}
		list = null;
	}
}

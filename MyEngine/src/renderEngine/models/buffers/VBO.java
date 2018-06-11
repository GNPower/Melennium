package renderEngine.models.buffers;

import java.util.ArrayList;

import org.lwjgl.opengl.GL15;


public class VBO {
	
	public static final int ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER;
	public static final int ELEMENT_ARRAY_BUFFER = GL15.GL_ELEMENT_ARRAY_BUFFER;
	
	private static ArrayList<VBO> list = new ArrayList<VBO>();

	private int id;
	
	public VBO(){
		create();
	}
	
	private void  create(){
		id = GL15.glGenBuffers();
		list.add(this);
	}
	
	public void bind(int type){
		GL15.glBindBuffer(type, id);
	}
	
	public void unbind(int type){
		GL15.glBindBuffer(type, 0);
	}
	
	public void delete(){
		GL15.glDeleteBuffers(id);
		list.remove(this);
	}
	
	public static void cleanUp(){
		for(VBO vbo : list){
			GL15.glDeleteBuffers(vbo.id);
		}
		list = null;
	}
}

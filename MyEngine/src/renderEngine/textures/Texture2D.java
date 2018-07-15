package renderEngine.textures;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import util.ResourceLoader;

public class Texture2D {
	
	private static ArrayList<Texture2D> list = new ArrayList<Texture2D>();

	private int id;
	private int width, height;
	
	private boolean hasTransparency = false;
	private boolean syncNormals = false;
	
	public Texture2D(){	
	}
	
	public Texture2D(String fileName){
		id = ResourceLoader.loadTexture2D(fileName);
		list.add(this);
	}
	
	public void generate(){
		id = GL11.glGenTextures();
	}
	
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void unbind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void delete(){
		GL11.glDeleteTextures(id);
		list.remove(this);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getId() {
		return id;
	}
	
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isSyncNormals() {
		return syncNormals;
	}

	public void setSyncNormals(boolean syncNormals) {
		this.syncNormals = syncNormals;
	}

	public static void cleanUp(){
		for(Texture2D texture : list){
			GL11.glDeleteTextures(texture.getId());
		}
		list = null;
	}
}

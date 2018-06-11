package renderEngine.guis;

import renderEngine.textures.Texture2D;
import util.maths.vectors.Vector2f;

public class Gui {

	private Texture2D texture;
	private Vector2f position;
	private Vector2f scale;
	
	public Gui(String texture, Vector2f position, Vector2f scale){
		this.texture = new Texture2D(texture);
		this.position = position;
		this.scale = scale;
	}
	
	public Texture2D getTexture() {
		return texture;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
}

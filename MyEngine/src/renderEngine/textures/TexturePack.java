package renderEngine.textures;


public class TexturePack {
	
	public static final int SUPPORTED_TEXTUREPACK_AMOUNT = 4;

	private Texture2D[] textures;
	private Texture2D background;
	
	public TexturePack(Texture2D texture, Texture2D... textures) {
		background = texture;
		this.textures = textures;
	}
	
	public Texture2D getBackground(){
		return background;
	}
	
	public Texture2D getTexture(int index){
		if(index < 0 || index >= textures.length)
			return background;
		return textures[index];
	}
}

package renderEngine.textures;

public class TextureAtlas {

	private int numberOfRows;
	private Texture2D texture;
	
	public TextureAtlas(String fileName, int numberOfRows){
		this.numberOfRows = numberOfRows;
		texture = new Texture2D(fileName);
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public Texture2D getTexture() {
		return texture;
	}

	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
}

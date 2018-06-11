package util.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import renderEngine.textures.Texture2D;
import renderEngine.textures.TexturePack;
import util.RenderUtil;

public class Textures {
	
	public static final String TEXTURE_PATH = "res/textures/";
	public static final String TERRAIN_PATH = "terrains/";	

	public static int loadTexture2D(String fileName) {

		String[] splitName = fileName.split("\\.");
		String ext = splitName[splitName.length - 1];

		Texture texture = null;

		try {
			texture = TextureLoader.getTexture(ext, new FileInputStream(TEXTURE_PATH + fileName));
			RenderUtil.enableTextureMippmapping();
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found!");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error Loading Texture File!");
			e.printStackTrace();
			System.exit(1);
		}
		
		int texID = texture.getTextureID();
		
		return texID;
	}
	
	public static TexturePack loadTexturePack(String directory){
		Texture2D[] textures = new Texture2D[TexturePack.SUPPORTED_TEXTUREPACK_AMOUNT]; 
		for(int i = 0; i < TexturePack.SUPPORTED_TEXTUREPACK_AMOUNT; i++){
			String fileName = TERRAIN_PATH + directory + "/tile" + (i + 1) + ".png";
			textures[i] = new Texture2D(fileName);
		}
		Texture2D background = new Texture2D(TERRAIN_PATH + directory + "/background.png");
		
		return new TexturePack(background, textures);
	}
	
	public static BufferedImage loadImage(String fileName){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(TEXTURE_PATH + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
}

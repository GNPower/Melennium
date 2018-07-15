package core.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class ImageLoader {
	
	private static HashMap<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();

	public static int[] loadImage(String fileName) {

		String[] splitName = fileName.split("\\.");
		String ext = splitName[splitName.length - 1];

		Texture texture = null;

		try {
			texture = TextureLoader.getTexture(ext, new FileInputStream(fileName));
//			RenderUtil.enableTextureMippmapping();
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
		
		try {
			images.put(texID, loadBufferedImage(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] data = {texID, images.get(texID).getWidth(), images.get(texID).getHeight()};
		
		return data;
	}
	
	public static BufferedImage loadBufferedImage(String file) throws IOException {
		return ImageIO.read(new File(file));
	}
	
//	public static int[] loadImage(String file) {
//	ByteBuffer imageBuffer;
//	
//	try {
//		imageBuffer = ioResourceToByteBuffer(file, 128 * 128);
//	} catch (IOException e) {
//		throw new RuntimeException(e);
//	}
//	
//	IntBuffer w = BufferUtils.createIntBuffer(1);
//	IntBuffer h = BufferUtils.createIntBuffer(1);
//	IntBuffer c = BufferUtils.createIntBuffer(1);
//	
//	//use info to read image metadata without decoding the entire image
//	if(!stbi_info_from_memory(imageBuffer, w, h, c)) {
//		throw new RuntimeException("Failed to load image information: " + stbi_failure_reason());
//	}
//	
//	//Decode the image
//	ByteBuffer image = stbi_load_from_memory(imageBuffer, w, h, c, 0);
//	if(image == null)
//		throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
//	
//	int width = w.get(0);
//	int height = h.get(0);
//	int comp = c.get(0);
//	
//	int texId = GL11.glGenTextures();
//	
//	GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
//	
//	if(comp == 3) {
//		if((width & 3) != 0) {
//			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (width & 1));
//		}
//		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_INT, image);
//	} else {
//		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_INT, image);
//	}
//	
//	stbi_image_free(image);
//	
//	int[] data = {texId, w.get(), h.get()};
//	
//	return data;
//}
	
//	public static ByteBuffer loadImageToByteBuffer(String file) {
//		ByteBuffer imageBuffer;
//		
//		try {
//			imageBuffer = ioResourceToByteBuffer(file, 128*128);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		
//		IntBuffer w = BufferUtils.createIntBuffer(1);
//		IntBuffer h = BufferUtils.createIntBuffer(1);
//		IntBuffer c = BufferUtils.createIntBuffer(1);
//		
//		//use info to read image metadata without decoding the entire image
//		if(!stbi_info_from_memory(imageBuffer, w, h, c)) {
//			throw new RuntimeException("Failed to load image information: " + stbi_failure_reason());
//		}
//		
//		//Decode the image
//		ByteBuffer image = stbi_load_from_memory(imageBuffer, w, h, c, 0);
//		if(image == null)
//			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
//		
//		return image;
//	}
//	
//	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException{
//		ByteBuffer buffer;
//		
//		Path path = Paths.get(resource);
//		if(Files.isReadable(path)) {
//			try(SeekableByteChannel fc = Files.newByteChannel(path)){
//				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
//				while(fc.read(buffer) != -1) {
//					;
//				}
//			}
//		} else {
//			try(
//				InputStream source = Thread.class.getClassLoader().getResourceAsStream(resource);
//				ReadableByteChannel rbc = Channels.newChannel(source);
//				){
//				buffer = BufferUtils.createByteBuffer(bufferSize);
//				
//				while(true) {
//					int bytes = rbc.read(buffer);
//					if(bytes == -1)
//						break;
//					if(buffer.remaining() == 0)
//						buffer = resizeBuffer(buffer, buffer.capacity() * 2);
//				}
//			}
//		}
//		
//		buffer.flip();
//		return buffer;
//	}
//	
//	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
//		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
//		buffer.flip();
//		newBuffer.put(buffer);
//		return newBuffer;
//	}
}

package util.buffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;


public class BufferUtil {
	
	public static FloatBuffer createFloatBuffer(int size){
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size){
		return BufferUtils.createIntBuffer(size);
	}

	public static FloatBuffer createFlippedBuffer(float... data){
		FloatBuffer buffer = createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createFlippedBuffer(int... data){
		IntBuffer buffer = createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector2f... data) {
		float[] array = new float[data.length * 2];
		for(int i = 0; i < data.length; i++) {
			array[i * 2] = data[i].getX();
			array[(i * 2) + 1] = data[i].getY();
		}
		FloatBuffer buffer = createFloatBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
	
//	public static FloatBuffer storeMatrix4f(Matrix4f matrix){
//		float[] data = new float[4*4];
//		int counter = 0;
//		for(int x = 0; x < 4; x++){
//			for(int y = 0; y < 4; y++){
//				data[counter] = matrix.get(x, y);
//				counter++;
//			}
//		}
//		return createFlippedBuffer(data);
//	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f data){
		FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				buffer.put(data.get(i, j));
			}
		}
		
		buffer.flip();
		return buffer;
	}
}

package util.interfacing.accessories;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class KeyboardInput {

	public static final int MAX_KEYCODES = 256;

	private static ArrayList<Integer> currentKeys = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private static ArrayList<Integer> releasedKeys = new ArrayList<Integer>();
	
	public static void init(){
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			System.err.println("Could not initialize keyboard interfacing");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void update() {
		
		releasedKeys.clear();
		for(int i = 0 ; i < MAX_KEYCODES; i++){
			if(!key(i) && currentKeys.contains(i))
				releasedKeys.add(i);
		}
		
		pressedKeys.clear();
		for(int i = 0; i < MAX_KEYCODES; i++){
			if(key(i) && !currentKeys.contains(i))
				pressedKeys.add(i);
		}
		
		currentKeys.clear();
		for(int i = 0; i < MAX_KEYCODES; i++){
			if(key(i))
				currentKeys.add(i);
		}
	}
	
	private static boolean key(int keyCode){
		return Keyboard.isKeyDown(keyCode);
	}

	public static boolean getKey(int keyCode) {
		return currentKeys.contains(keyCode);
	}

	public static boolean getKeyDown(int keyCode) {
		return pressedKeys.contains(keyCode);
	}

	public static boolean getKeyUp(int keyCode) {
		return releasedKeys.contains(keyCode);
	}
	
	public static void cleanUp(){
		Keyboard.destroy();
	}
}

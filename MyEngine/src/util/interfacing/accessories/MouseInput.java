package util.interfacing.accessories;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import util.maths.vectors.Vector2f;

public class MouseInput {

	public static final int MAX_MOUSECODES = 8;

	private static ArrayList<Integer> currentButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> releasedButtons = new ArrayList<Integer>();
	
	public static void init(){
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			System.err.println("Could not initialize mouse interfacing");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void update() {

		releasedButtons.clear();
		for (int i = 0; i < MAX_MOUSECODES; i++) {
			if (!mouse(i) && currentButtons.contains(i))
				releasedButtons.add(i);
		}

		pressedButtons.clear();
		for (int i = 0; i < MAX_MOUSECODES; i++) {
			if (mouse(i) && !currentButtons.contains(i))
				pressedButtons.add(i);
		}

		currentButtons.clear();
		for (int i = 0; i < MAX_MOUSECODES; i++) {
			if (mouse(i))
				currentButtons.add(i);
		}
	}

	private static boolean mouse(int mouseCode) {
		return Mouse.isButtonDown(mouseCode);
	}

	public static boolean getMouseButton(int mouseButton) {
		return currentButtons.contains(mouseButton);
	}

	public static boolean getMouseDown(int mouseButton) {
		return pressedButtons.contains(mouseButton);
	}

	public static boolean getMouseUp(int mouseButton) {
		return releasedButtons.contains(mouseButton);
	}

	public static void enableCursor(boolean enabled) {
		Mouse.setGrabbed(!enabled);
	}

	public static void setCursorPosition(Vector2f position) {
		Mouse.setCursorPosition((int) position.getX(), (int) position.getY());
	}

	public static void setCursorPosition(int x, int y) {
		Mouse.setCursorPosition(x, y);
	}
	
	public static Vector2f getCursorPosition(){
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}
	
	public static void cleanUp(){
		Mouse.destroy();
	}
}

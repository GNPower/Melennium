package core.kernel.input;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import core.math.Vec2f;

public class Input {

	public static final int MAX_KEYCODES = 256;
	public static final int MAX_MOUSECODES = 8;
	
	private static Input instance = null;

	private ArrayList<Integer> currentKeys = new ArrayList<Integer>();
	private ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private ArrayList<Integer> releasedKeys = new ArrayList<Integer>();
	private ArrayList<Integer> currentButtons = new ArrayList<Integer>();
	private ArrayList<Integer> pressedButtons = new ArrayList<Integer>();
	private ArrayList<Integer> releasedButtons = new ArrayList<Integer>();
	
	private Vec2f cursorPosition;
	private Vec2f lockedCursorPosition;
	
	private float scrollOffset;
	
	public static Input getInstance() {
		if(instance == null)
			instance = new Input();
		return instance;
	}
	
	protected Input() {
		cursorPosition = new Vec2f();
		lockedCursorPosition = new Vec2f();
		
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			System.err.println("Could not initialize keyboard interfacing");
			e.printStackTrace();
			System.exit(1);
		}
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			System.err.println("Could not initialize mouse interfacing");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void update() {
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
		
		scrollOffset = Mouse.getDWheel();
	}
	
	private boolean key(int keyCode) {
		return Keyboard.isKeyDown(keyCode);
	}
	
	private boolean mouse(int keyCode) {
		return Mouse.isButtonDown(keyCode);
	}
	
	public boolean getKey(int keyCode) {
		return currentKeys.contains(keyCode);
	}
	
	public boolean isKeyPushed(int keyCode) {
		return pressedKeys.contains(keyCode);
	}
	
	public boolean isKeyReleased(int keyCode) {
		return releasedKeys.contains(keyCode);
	}
	
	public boolean getButton(int keyCode) {
		return currentButtons.contains(keyCode);
	}
	
	public boolean isButtonPushed(int keyCode) {
		return pressedButtons.contains(keyCode);
	}
	
	public boolean isButtonReleased(int keyCode) {
		return releasedButtons.contains(keyCode);
	}
	
	public boolean getMouseButton(int keyCode) {
		return currentButtons.contains(keyCode);
	}
	
	public boolean isMouseButtonPushed(int keyCode) {
		return pressedButtons.contains(keyCode);
	}
	
	public boolean isMouseButtonReleased(int keyCode) {
		return releasedButtons.contains(keyCode);
	}
	
	public float getDWheel() {
		return (float) Mouse.getDWheel() * 0.1f;
	}
	
	public void enableCursor() {
		Mouse.setGrabbed(true);
	}
	
	public void disableCursor() {
		Mouse.setGrabbed(false);
	}

	public void setCursorPosition(Vec2f position) {
		this.cursorPosition = position;
		Mouse.setCursorPosition((int) position.getX(), (int) position.getY());
	}

	public void setCursorPosition(int x, int y) {
		this.cursorPosition = new Vec2f(x, y);
		Mouse.setCursorPosition(x, y);
	}
	
	public Vec2f getCursorPosition(){
		return cursorPosition;
	}
	
	public Vec2f getLockedCursorPosition() {
		return lockedCursorPosition;
	}
	
	public void setLockedCursorPosition(Vec2f position) {
		this.lockedCursorPosition = position;
	}
	
	public void setLockedCursorPosition(int x, int y) {
		this.lockedCursorPosition = new Vec2f(x, y);
	}
	
	public ArrayList<Integer> getPressedKeys(){
		return pressedKeys;
	}
	
	public ArrayList<Integer> getReleasedKeys(){
		return releasedKeys;
	}
	
	public ArrayList<Integer> getCurrentKeys(){
		return currentKeys;
	}
	
	public ArrayList<Integer> getPressedMouseButtons(){
		return pressedButtons;
	}
	
	public ArrayList<Integer> getReleasedMouseButtons(){
		return releasedButtons;
	}
	
	public ArrayList<Integer> getCurrentMouseButtons(){
		return currentButtons;
	}
	
	public ArrayList<Integer> getHeldMouseButtons(){
		return pressedButtons;
	}
	
	public float getScrollOffset() {
		return scrollOffset;
	}
	
	public void setScrollOffset(float offset) {
		this.scrollOffset = offset;
	}
	
	public static void cleanUp(){
		Keyboard.destroy();
		Mouse.destroy();
	}
}

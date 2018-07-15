package core.kernel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import core.utils.Constants;

public class Window {

	private static Window instance = null;
	
	private String title;
	private double aspectRatio;
	private int width, height;
	
	public static Window getInstance() {
		if(instance == null)
			instance = new Window();
		return instance;
	}
	
	protected Window() {
		this.title = Constants.WINDOW_TITLE;
		this.aspectRatio = Constants.ASPECT_RATIO;
		this.width = Constants.WNIDOW_WIDTH;
		this.height = (int) (width / aspectRatio);
		createDisplay();
	}
	
	public void init() {

	}
	
	private void createDisplay() {
		Display.setTitle(title);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("Error creating the display!");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void render() {
		Display.update();
	}
	
	public void destroyWindow() {
		Display.destroy();
	}
	
	public boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

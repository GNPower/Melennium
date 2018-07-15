package core.kernel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import core.utils.dataStructures.AspectRatio;
import core.utils.loader.ResourceLoader;

public class Window {
	
	private static Window instance = null;

	private String title;
	private int width, height;
	private AspectRatio aspectRatio;
	
	public static Window getInstance() {
		if(instance == null)
			instance = new Window();
		return instance;
	}
	
	protected Window() {}
	
	public void init(String settingsFile) {
		String[] data = ResourceLoader.loadWindowSettings(settingsFile);
		title = data[0];
		width = Integer.valueOf(data[1]);
		aspectRatio = new AspectRatio(Integer.valueOf(data[2]), Integer.valueOf(data[3]));
		height = (int) (width / aspectRatio.getAspect());
		createDisplay();
	}
	
	public void createDisplay() {
		Display.setTitle(title);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			//TODO: handle error
			e.printStackTrace();
		}
	}
	
	public void render() {
		Display.update();
	}
	
	public void destroy() {
		Display.destroy();
		System.exit(0);
	}
	
	public boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getAspectRatio() {
		return aspectRatio.getAspectRatio();
	}
}

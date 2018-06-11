package coreEngine.screen;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	
	private String title;
	private double aspectRatio;
	private static int width, height;


	public Window(String title, int width, int aspectWidth, int aspectHeight){
		this.title = title;
		double ratio = (double)aspectWidth / (double)aspectHeight;
		this.aspectRatio = ratio;
		Window.width = width;
		Window.height = (int)(width / aspectRatio);
		createDisplay(title, width, height);
	}
	
	private void createDisplay(String title, int width, int height){
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
	
	public void render(){
		Display.update();
	}
	
	public void destroyWindow(){
		Display.destroy();
	}
	
	public boolean isCloseRequested(){
		return Display.isCloseRequested();
	}

	public String getTitle() {
		return title;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}

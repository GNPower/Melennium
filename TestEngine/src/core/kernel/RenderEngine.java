package core.kernel;

import core.configs.Default;

public class RenderEngine {

	private Window window;
	
	public RenderEngine() {
		window = Window.getInstance();
	}
	
	public void init() {
		window.init("./res/settings/window_settings.ves");
	}
	
	public void update() {

	}
	
	public void render() {
		Camera.getInstance().update();
		Default.clearScreen();
		
		window.render();
	}
	
	public void shutdown() {
		window.destroy();
	}
}

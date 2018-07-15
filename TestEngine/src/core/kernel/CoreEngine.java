package core.kernel;

import core.configs.Default;
import core.kernel.input.Input;
import core.utils.Constants;

public class CoreEngine {

	private static int fps;
	private static float framerate = 1024;
	private static float frameTime = 1.0f / framerate;
	private boolean isRunning = false;
	private RenderEngine renderer;
	
	
	public CoreEngine() {
		renderer = new RenderEngine();
	}
	
	public void init() {
		Default.init();
		renderer.init();
	}
	
	public void start() {
		if(isRunning)
			return;
		run();
	}
	
	private void update() {
		Input.getInstance().update();
		Camera.getInstance().update();
		renderer.update();
	}
	
	private void render() {
		renderer.render();
	}
	
	public void run() {
		isRunning = true;
		
		int frames = 0;
		int frameCounter = 0;
		
		long lastTime = System.nanoTime();
		double unprocessedTime = 0;
		
		while(isRunning) {
			boolean render = false;
			long startTime = System.nanoTime();
			
			long passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime / (double) Constants.NANOSECOND;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime) {
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(Window.getInstance().isCloseRequested())
					stop();
								
				update();
				
				if(frameCounter >= Constants.NANOSECOND) {
					fps = frames;
					frames = 0;
					frameCounter = 0;
				}
			}
			if(render) {
				render();
				frames++;
			}else {
				try {
					Thread.sleep(10);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		cleanUp();
	}
	
	public void stop() {
		if(!isRunning)
			return;
		isRunning = false;
	}
	
	private void cleanUp() {
		renderer.shutdown();
	}
	
	public static int getFps() {
		return fps;
	}
	
	public static float getFrameTime() {
		return frameTime;
	}
}

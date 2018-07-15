package core.kernel;

import core.configs.Default;
import core.kernel.input.Input;
import core.kernel.input.Keys;
import core.math.Vec3f;
import core.modules.entities.Entity;
import core.modules.sky.Skydome;
import core.modules.terrain.Terrain;

/**
 * 
 * @author oreon3D
 * The RenderingEngine manages the render calls of all 3D entities
 * with shadow rendering and post processing effects
 *
 */
public class RenderingEngine {
	
	private Window window;
	
	private Skydome skydome;
	private Terrain terrain;
	private Entity entity;
	
	public RenderingEngine()
	{
		window = Window.getInstance();
		skydome = new Skydome();
		terrain = new Terrain();
	}
	
	public void init()
	{
		window.init();
		terrain.init("./res/settings/terrain_settings.txt");
		entity = new Entity("", new Vec3f(10, 10, 10), new Vec3f(0,0,0));
	}

	public void render()
	{	
		Camera.getInstance().update();
		Default.clearScreen();
		
		skydome.render();
		
		terrain.updateQuadtree();
		terrain.render();
		entity.render();
		// draw into OpenGL window
		window.render();
	}
	
	public void update(){
		entity.move(new Vec3f(0,0.005f,0), new Vec3f(0,0,0));
		if(Input.getInstance().isKeyPushed(Keys.KEY_M)) {
			if(RenderContext.getInstance().isWireframe()) {
				RenderContext.getInstance().setWireframe(false);
			}else {
				RenderContext.getInstance().setWireframe(true);
			}
		}
	}
	
	public void shutdown(){}
}

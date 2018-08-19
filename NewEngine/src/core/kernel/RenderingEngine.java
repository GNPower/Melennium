package core.kernel;

import core.configs.Default;
import core.kernel.input.Input;
import core.kernel.input.Keys;
import core.math.Vec3f;
import core.modules.entities.Entity;
import core.modules.entities.EntityConfig;
import core.modules.sky.Skydome;
import core.modules.terrain.Terrain;
import net.Client;

public class RenderingEngine {
	
	private Window window;
	private Client client;
	
	private Skydome skydome;
	private Terrain terrain;
	private Entity entity;
	private Entity barrel;
	
	public RenderingEngine()
	{
		window = Window.getInstance();
		skydome = new Skydome();
		terrain = new Terrain();
	}
	
	public void init()
	{
		window.init();
		World.getInstance().init();
		
		terrain.init("./res/settings/terrain_settings.txt");
		
		entity = new Entity("res/models/barrel/barrel.obj", "res/textures/barrel.png", new Vec3f(-180,105,-30), new Vec3f(0,0,0));
		entity.setConfig(new EntityConfig("./res/settings/entity_settings.txt"));
		entity.setShouldRender(false);
		
		barrel = new Entity("res/models/dragon/dragon.obj", "res/textures/cyan.png", new Vec3f(-180, 105, -30), new Vec3f(0,0,0));
		
		Camera.getInstance().setTrakedEntity(entity);
		
		String username = "Graham" + (System.nanoTime() / 100000000);
		System.out.println("Username is: " + username);
		
//		float theta = (float) Math.toDegrees(Math.acos(173.205/200.0));
//		System.out.println("Theta is: " + theta);
		
		//CONNECT TO SERVER01 HERE:
		client = new Client("localhost", 25565);
			if(!client.connect(entity, username))
				System.out.println("Failed to connect to server! Loading into local session");
	}

	public void render()
	{	
		Default.clearScreen();
		
		skydome.render();
		
		terrain.updateQuadtree();
		terrain.render();
		entity.render();
		barrel.render();
//		System.out.println("entity pos:" + entity.getPosition().toString());
		client.render();
		// draw into OpenGL window
		window.render();
	}
	
	public void update(){
		client.update(entity);
//		entity.move(new Vec3f(0,0.005f,0), new Vec3f(0,0,0));
		
		//sets whether the engine should render in full or only wireframe
		if(Input.getInstance().isKeyPushed(Keys.KEY_M)) {
			if(RenderContext.getInstance().isWireframe()) {
				RenderContext.getInstance().setWireframe(false);
			}else {
				RenderContext.getInstance().setWireframe(true);
			}
		}
		
		if(Input.getInstance().isKeyPushed(Keys.KEY_C)) {
			if(Camera.getInstance().isTrackEntity()) {
				Camera.getInstance().setTrackEntity(false);
				entity.setShouldRender(false);
			}else {
				Camera.getInstance().setTrackEntity(true);
				entity.setShouldRender(true);
			}
		}
	}
	
	public void shutdown(){}
}

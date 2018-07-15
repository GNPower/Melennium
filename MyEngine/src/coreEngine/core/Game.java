package coreEngine.core;

import java.util.ArrayList;
import java.util.List;

import coreEngine.screen.Window;
import net.test1.Client;
import net.test1.ServerPlayer;
import renderEngine.entities.Camera;
import renderEngine.entities.Entity;
import renderEngine.entities.EntityManager;
import renderEngine.entities.Player;
import renderEngine.entities.lights.Light;
import renderEngine.guis.Gui;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.models.buffers.VAO;
import renderEngine.rendering.GuiRenderer;
import renderEngine.rendering.MasterRenderer;
import renderEngine.terrains.Terrain;
import renderEngine.terrains.TerrainManager;
import renderEngine.textures.Texture2D;
import util.RenderUtil;
import util.ResourceLoader;
import util.colours.Colour;
import util.interfacing.accessories.KeyboardInput;
import util.interfacing.accessories.Keys;
import util.interfacing.accessories.MouseInput;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;
import util.time.Time;
import util.time.Timer;

public class Game {
	
	private static final double FPS_CAP = 1024.0;
	
	private Window window;
		
	MasterRenderer renderer = new MasterRenderer();
	

	Model model = new Model("barrel", "cyan.png");
	Model model2 = new Model("fern", "fernatlas.png", 2);
//	Entity entity = new Entity(model, new Vector3f(-10, 0, -10), new Vector3f(0, 0, 0), 1);
	Entity fern = new Entity(model2, new Vector3f(10, 0, 10), new Vector3f(0,0,0), 1, 1);
	Player player = new Player(model, new Vector3f(5, 0, -5f), new Vector3f(0, 0, 0), 1);	
	
	
	Camera camera = new Camera(player);
	TerrainManager Tmanager = new TerrainManager(renderer, camera);
	EntityManager Emanager = new EntityManager(renderer);
	
	List<Light> lights = new ArrayList<Light>();
	Light light = new Light(new Vector3f(0, 3000000, -5), new Colour(1, 1, 1), 20);
	Timer timer = new Timer(1.0);
	
	Terrain terrain = new Terrain(0, 0, ResourceLoader.loadTexturePack("terrain1"), "terrains/terrain1/blend.png", "terrains/terrain1/heightmap.png");
	
	List<Gui> guis = new ArrayList<Gui>();
	GuiRenderer Grenderer =  new GuiRenderer();
	
	private double FPS;
	private boolean isRunning;
	
	//SERVER DATA HERE:
	//private GameClient client;
	//Client client = new Client(this, "99.253.248.160", 25565);
	Client client = new Client(this, "localhost", 25565);
	ServerPlayer sp;

	public Game(Window window){
		this.window = window;
		isRunning = false;
		init();
	}
	
	private void init(){
		KeyboardInput.init();
		MouseInput.init();
		
		//entity.getModel().getMaterial().setShineDamper(10f);
		//entity.getModel().getMaterial().setReflectivity(1.2f);
//		entity.getModel().getTexture().setHasTransparency(true);
//		entity.getModel().getTexture().setSyncNormals(true);
		
//		int[][] data = LODLoader.loadLOD("terrainDetails.ldd");
//		for(int i = 0; i < data.length; i++){
//			System.out.print(data[i][0] + "		");
//			System.out.println(data[i][1]);
//		}
		
		Tmanager.addTerrain(terrain);
		fern.getModel().getTexture().setHasTransparency(true);
		
		Emanager.createEntity(model2, new Vector3f(80, Tmanager.getTerrain(80, 80).getHeight(80, 80), 80), new Vector3f(0, 0, 0), 1, 1);
		Emanager.createEntity(Tmanager, model2, new Vector2f(60, 60), new Vector3f(0,0,0), 1, 1);
		Emanager.addEntity(fern);
		Emanager.addEntity(player);
		
		lights.add(new Light(new Vector3f(20, 10, 20), new Colour(new Vector3f(10, 0, 0)), new Vector3f(1, 0.01f, 0.002f), 20));
		lights.add(new Light(new Vector3f(40, 10, 40), new Colour(new Vector3f(0, 10, 0)), new Vector3f(1, 0.01f, 0.002f),20));
		lights.add(new Light(new Vector3f(60, 10, 60), new Colour(new Vector3f(0, 0, 10)), new Vector3f(1, 0.01f, 0.002f),100));
		lights.add(new Light(new Vector3f(80, 10, 80), new Colour(new Vector3f(10, 10, 0)), new Vector3f(1, 0.01f, 0.002f),60));
		lights.add(new Light(new Vector3f(100, 10, 100), new Colour(new Vector3f(10, 0, 0)), new Vector3f(1, 0.01f, 0.002f),20));
		
//		Mesh mesh = ResourceLoader.loadObjModel("dome");
		
		
//		guis.add(new Gui("cyan.png", new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f)));
		
		String username = "Graham" + (System.nanoTime() / 100000000);
		player.setName(username);
		System.out.println("Username is: " + username);
		
		//CONNECT TO SERVER01 HERE:
		
		//Client client = new Client("localhost", 8192);
//			ServerPlayer.setPlayerModel(model);
//			if(!client.connect(username)){		
//				System.out.println("Failed to connect to server! Loading into local session");
//			}else {
//				System.out.println("Connected to server!");
//			}
//			sp = new ServerPlayer("bob", new float[]{10, 0, 10}, new float[]{0,0,0});
		//client.addPlayer(sp);
		
		//CONNECT TO SERVER02 HERE:
//		client = new GameClient(this, "localhost");
//		client.start();
//		client.send("ping".getBytes());
		
		//MSDatabase db = MSDatabase.deserializeFromFile("level.pcdb");
		//client.send(db);
	}
	
	private void input(){
		KeyboardInput.update();
		MouseInput.update();
		
		if(KeyboardInput.getKeyDown(Keys.KEY_M))
			RenderUtil.advanceRenderMode();
	}
	
	private void update(){
		//entity.increasePosition(0, 0, 0.002f);
		//entity.increaseRotation(0, 0.05f, 0);
		//player.move(camera);
		camera.move();		
		player.move(Tmanager.getTerrain(player.getPosition().getX(), player.getPosition().getZ()));	
//		lights = Light.sortLights(lights, camera.getPosition());
//		client.update();
		//System.out.println(entity.getPosition());
//		lights = Light.sortLights(lights, camera.getPosition());
	}
	
	private void render(){
		Tmanager.render();
		Emanager.render();
//		client.renderPlayers(renderer);
		//renderer.processEntity(sp);
		//renderer.processEntity(player);
		renderer.render(light, lights, camera);
		Grenderer.render(guis);
		window.render();
		//System.out.println(camera.getPosition());
	}
	
	private void run(){
		int frames = 0;
		int frameCounter = 0;
		
		final double frameTime = 1.0 / FPS_CAP;
		
		long lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning){
			boolean render = false;
			long startTime = Time.getTime();

			long passedTime = startTime - lastTime;
			lastTime = startTime;
			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime){
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(window.isCloseRequested())
					stop();
				
				Time.setDelta(frameTime);
				input();
				update();
				
				if(frameCounter >= Time.SECOND){
					FPS = frames;
//					System.out.println(frames + " Fps");
					frames = 0;
					frameCounter = 0;
				}
			}
			
			if(render){
				render();
				frames++;
			}else{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}		
		cleanUp();
	}
	
	private void cleanUp(){
		Grenderer.cleanUp();
		renderer.cleanUp();
		Texture2D.cleanUp();
		VAO.cleanUp();
		KeyboardInput.cleanUp();
		MouseInput.cleanUp();
		window.destroyWindow();
	}
	
	public void start(){
		if(isRunning)
			return;
		isRunning = true;
		run();
	}
	
	public void stop(){
		if(!isRunning)
			return;
		isRunning = false;
	}

	public double getFPS() {
		return FPS;
	}
	
	public Player getPlayer(){
		return player;
	}
}

package core.kernel;

import java.util.ArrayList;
import java.util.List;

import core.math.Vec3f;
import core.modules.entities.Entity;
import core.modules.lights.Light;
import core.modules.sky.Skydome;
import core.modules.terrain.Terrain;
import core.scene.Component;

public class World extends Component{
	
	private Light light;
	private Skydome skydome;
	private Terrain terrain;
	private List<Entity> entities;

	private static World instance = null;
	
	public static World getInstance() {
		if(instance == null)
			instance = new World();
		return instance;
	}
	
	public void init(Skydome dome) {
		this.skydome = dome;
		light = new Light(new Vec3f(100,1000,1), new Vec3f(1,1,1));
	}
	
	protected World() {
		entities = new ArrayList<Entity>();
	}
	
	public void render() {
		skydome.render();
		terrain.updateQuadtree();
		terrain.render();
		for(Entity e : entities) {
			e.render();
		}
	}
	
	public Light getLight() {
		return light;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}

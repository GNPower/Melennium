package renderEngine.entities;

import java.util.ArrayList;
import java.util.List;

import renderEngine.models.Model;
import renderEngine.rendering.MasterRenderer;
import renderEngine.terrains.TerrainManager;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class EntityManager {

	private MasterRenderer renderer;
	private List<Entity> entities;
	
	public EntityManager(MasterRenderer renderer) {
		this.renderer = renderer;
		entities = new ArrayList<Entity>();
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void createEntity(Model model, Vector3f pos, Vector3f rot, float scale, int textureIndex) {
		entities.add(new Entity(model, pos, rot, scale, textureIndex));
	}
	
	public void createEntity(TerrainManager Tmanager, Model model, Vector2f pos, Vector3f rot, float scale, int textureIndex) {
		entities.add(new Entity(model, new Vector3f(pos.getX(), Tmanager.getTerrain(pos.getX(), pos.getY()).getHeight(pos.getX(), pos.getY()), pos.getY()), rot, scale, textureIndex));
	}
	
	public void render() {
		for(Entity entity: entities) {
			renderer.processEntity(entity);
		}
	}
}

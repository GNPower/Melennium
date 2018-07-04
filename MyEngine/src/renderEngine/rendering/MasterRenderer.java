package renderEngine.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renderEngine.entities.Camera;
import renderEngine.entities.Entity;
import renderEngine.entities.lights.Light;
import renderEngine.models.Model;
import renderEngine.shaders.entities.StaticShader;
import renderEngine.shaders.terrain.TerrainShader;
import renderEngine.terrains.Terrain;
import util.RenderUtil;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer = new EntityRenderer(shader);
	
	private TerrainShader Tshader = new TerrainShader();
	private TerrainRenderer Trenderer = new TerrainRenderer(Tshader);
	
	private Map<Model, List<Entity>> entities = new HashMap<Model, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer(){
		RenderUtil.initSettings();
		RenderUtil.setCullMode(RenderUtil.GL_CULL_BACK);
	}
	
	public void render(Light light, Camera camera){
		prepare();
		renderEntities(light, camera);
		renderTerrains(light, camera);
	}
	
	private void renderEntities(Light light, Camera camera){
		shader.start();
		shader.loadSkyColour(RenderUtil.getColour());
		shader.loadAmbientLightFactor(0.06f);
		shader.renderFog(true);
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	private void renderTerrains(Light light, Camera camera){
		Tshader.start();
		Tshader.loadSkyColour(RenderUtil.getColour());
		Tshader.loadAmbientLightFactor(0.06f);
		Tshader.renderFog(true);
		Tshader.loadLight(light);
		Tshader.loadViewMatrix(camera);
		Tshader.loadProjectionMatrix(camera.getProjectionMatrix());
		Tshader.connectTextureUnits();
		Trenderer.render(terrains);
		Tshader.stop();
		terrains.clear();
	}
	
	public void processEntity(Entity entity){
		Model model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if(batch != null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void prepare() {
		RenderUtil.clearScreen();
	}
	
	public void cleanUp(){
		shader.cleanUp();
		Tshader.cleanUp();
	}
}

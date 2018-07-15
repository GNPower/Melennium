package renderEngine.rendering;

import java.util.List;
import java.util.Map;

import renderEngine.entities.Entity;
import renderEngine.models.Model;
import renderEngine.shaders.entities.StaticShader;
import util.RenderUtil;
import util.maths.Maths;
import util.maths.matrices.Matrix4f;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer(StaticShader shader) {		
		this.shader = shader;
	}	

	public void render(Map<Model, List<Entity>> entities) {
		for (Model model : entities.keySet()) {
			prepareModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				model.getVAO().render(true, model.getMesh().getVertexCount());
			}
			unbindModel(model);
		}
	}

	private void prepareModel(Model model) {
		model.getVAO().enable();

		shader.loadMaterial(model.getMaterial());
		shader.loadTexture(model.getTexture());
		
		shader.loadNumberOfRows(model.getAtlasRows());

		if(model.getTexture().isHasTransparency())
			RenderUtil.setCullMode(RenderUtil.GL_CULL_NONE);
	
		RenderUtil.enableTextureBank(0);
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		model.getTexture().bind();
	}

	private void unbindModel(Model model) {
		model.getVAO().disable();
		
		RenderUtil.setCullMode(RenderUtil.GL_CULL_BACK);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrtix(entity.getPosition(), entity.getRotation(),
				entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}

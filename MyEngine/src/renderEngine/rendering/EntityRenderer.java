package renderEngine.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindModel(model);
		}
	}

	private void prepareModel(Model model) {
		GL30.glBindVertexArray(model.getVaoID());
		for (int i = 0; i < model.getVAO().getVboIndex(); i++) {
			GL20.glEnableVertexAttribArray(i);
		}

		shader.loadMaterial(model.getMaterial());
		shader.loadTexture(model.getTexture());
		
		shader.loadNumberOfRows(model.getAtlasRows());

		if(model.getTexture().isHasTransparency())
			RenderUtil.setCullMode(RenderUtil.GL_CULL_NONE);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		model.getTexture().bind();
	}

	private void unbindModel(Model model) {
		for (int i = 0; i < model.getVAO().getVboIndex(); i++) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		
		RenderUtil.setCullMode(RenderUtil.GL_CULL_BACK);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrtix(entity.getPosition(), entity.getRotation(),
				entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

//	public void render(Entity entity, StaticShader shader) {
//		Model model = entity.getModel();
//		GL30.glBindVertexArray(model.getVaoID());
//		for (int i = 0; i < model.getVAO().getVboIndex(); i++) {
//			GL20.glEnableVertexAttribArray(i);
//		}
//
//		Matrix4f transformationMatrix = Maths.createTransformationMatrtix(entity.getPosition(), entity.getRotation(),
//				entity.getScale());
//		shader.loadTransformationMatrix(transformationMatrix);
//		shader.loadMaterial(entity.getModel().getMaterial());
//
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		model.getTexture().bind();
//		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//
//		for (int i = 0; i < model.getVAO().getVboIndex(); i++) {
//			GL20.glDisableVertexAttribArray(i);
//		}
//		GL30.glBindVertexArray(0);
//	}
}

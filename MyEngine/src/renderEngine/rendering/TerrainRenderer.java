package renderEngine.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import renderEngine.models.Model;
import renderEngine.shaders.terrain.TerrainShader;
import renderEngine.terrains.Terrain;
import renderEngine.textures.TexturePack;
import util.maths.Maths;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector3f;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader){
		this.shader = shader;
	}
	
	public void render(List<Terrain> terrains){
		for(Terrain terrain : terrains){
			prepareTerrain(terrain);
			prepareInstance(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain(terrain.getModel());
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		GL30.glBindVertexArray(terrain.getModel().getVaoID());
		for (int i = 0; i < terrain.getModel().getVAO().getVboIndex(); i++) {
			GL20.glEnableVertexAttribArray(i);
		}

		shader.loadMaterial(terrain.getModel().getMaterial());

		bindTextures(terrain);
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		terrain.getModel().getTexture().bind();
		
	}
	
	private void bindTextures(Terrain terrain){
		TexturePack pack = terrain.getTexturePack();
		
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		pack.getBackgroundTexture().bind();
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE1);
//		pack.getrTexture().bind();
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE2);
//		pack.getgTexture().bind();
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE3);
//		pack.getbTexture().bind();
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE4);
//		terrain.getBlendMap().bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		pack.getBackground().bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		pack.getTexture(0).bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		pack.getTexture(1).bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		pack.getTexture(2).bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		pack.getTexture(3).bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		terrain.getBlendMap().bind();
	}

	private void unbindTerrain(Model model) {
		for (int i = 0; i < model.getVAO().getVboIndex(); i++) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrtix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				new Vector3f(0, 0, 0), 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}

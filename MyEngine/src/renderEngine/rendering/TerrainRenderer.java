package renderEngine.rendering;

import java.util.List;

import renderEngine.shaders.terrain.TerrainShader;
import renderEngine.terrains.Terrain;
import renderEngine.textures.TexturePack;
import util.RenderUtil;
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
			terrain.getModel().getVAO().render(true, terrain.getModel().getMesh().getVertexCount());
			terrain.getModel().getVAO().disable();
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		terrain.getModel().getVAO().enable();

		shader.loadMaterial(terrain.getModel().getMaterial());

		bindTextures(terrain);
	}
	
	private void bindTextures(Terrain terrain){
		TexturePack pack = terrain.getTexturePack();
		
		RenderUtil.enableTextureBank(0);
		//GL13.glActiveTexture(GL13.GL_TEXTURE0);
		pack.getBackground().bind();
		
		RenderUtil.enableTextureBank(1);
		//GL13.glActiveTexture(GL13.GL_TEXTURE1);
		pack.getTexture(0).bind();
		
		RenderUtil.enableTextureBank(2);
		//GL13.glActiveTexture(GL13.GL_TEXTURE2);
		pack.getTexture(1).bind();
		
		RenderUtil.enableTextureBank(3);
		//GL13.glActiveTexture(GL13.GL_TEXTURE3);
		pack.getTexture(2).bind();
		
		RenderUtil.enableTextureBank(4);
		//GL13.glActiveTexture(GL13.GL_TEXTURE4);
		pack.getTexture(3).bind();
		
		RenderUtil.enableTextureBank(5);
		//GL13.glActiveTexture(GL13.GL_TEXTURE5);
		terrain.getBlendMap().bind();
	}

	private void prepareInstance(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrtix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				new Vector3f(0, 0, 0), 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}

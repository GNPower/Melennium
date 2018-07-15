package renderEngine.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import renderEngine.guis.Gui;
import renderEngine.models.Model;
import renderEngine.shaders.guis.GuiShader;
import util.maths.Maths;
import util.maths.matrices.Matrix4f;

public class GuiRenderer {
	
	private Model quad;
	private GuiShader shader;

	public GuiRenderer() {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1};
		quad = Model.createQuadModel(positions);
		shader = new GuiShader();
	}
	
	public void  render(List<Gui> guis) {
		shader.start();
		quad.getVAO().bind();
		for(int i = 0; i < quad.getVAO().getVboIndex(); i++) {
			GL20.glEnableVertexAttribArray(i);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		//rendering
		for(Gui gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			gui.getTexture().bind();
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformationMatrix(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getMesh().getVertices().length);
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		for(int i = 0; i < quad.getVAO().getVboIndex(); i++) {
			GL20.glDisableVertexAttribArray(i);
		}
		quad.getVAO().unbind();
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}

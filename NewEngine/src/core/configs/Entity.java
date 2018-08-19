package core.configs;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Entity implements RenderConfig{

	@Override
	public void enable() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	@Override
	public void disable() {
		
	}
}

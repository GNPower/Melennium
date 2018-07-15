package core.modules.gpgpu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import core.texturing.Texture2D;

public class SplatMapRenderer {

	private Texture2D splatmap;
	private SplatMapShader shader;
	private int N;
	
	public SplatMapRenderer(int N) {
		this.N = N;
		shader = SplatMapShader.getInstance();
		splatmap = new Texture2D();
		splatmap.generate();
		splatmap.bind();
		splatmap.bilinearFilter();
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, (int) (Math.log(N) / Math.log(2)), GL30.GL_RGBA16F, N, N);
		
	}
	
	public void render(Texture2D normalMap) {
		shader.bind();
		shader.updateUniforms(normalMap, N);
		GL42.glBindImageTexture(0, splatmap.getId(), 0, false, 0, GL15.GL_WRITE_ONLY, GL30.GL_RGBA16F);
		GL43.glDispatchCompute(N/16, N/16, 1);
		GL11.glFinish();
		splatmap.bind();
		splatmap.bilinearFilter();
	}
	
	public Texture2D getSplatmap() {
		return splatmap;
	}
	public void setSplatmap(Texture2D splatmap) {
		this.splatmap = splatmap;
	}
}

package core.modules.gpgpu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import core.texturing.Texture2D;

public class NormalMapRenderer {

	private float strength;
	private Texture2D normalMap;
	private NormalMapShader shader;
	private int N;
	
	public NormalMapRenderer(int N) {
		this.N = N;
		shader = NormalMapShader.getInstance();
		normalMap = new Texture2D();
		normalMap.generate();
		normalMap.bind();
		normalMap.bilinearFilter();
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, (int) (Math.log(N) / Math.log(2)), GL30.GL_RGBA32F, N, N);
	}
	
	public void render(Texture2D heightmap) {
		shader.bind();
		shader.updateUniforms(heightmap, N, strength);
		GL42.glBindImageTexture(0, normalMap.getId(), 0, false, 0, GL15.GL_WRITE_ONLY, GL30.GL_RGBA32F);
		GL43.glDispatchCompute(N/16, N/16, 1);
		GL11.glFinish();
		normalMap.bind();
		normalMap.bilinearFilter();
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public Texture2D getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(Texture2D normalMap) {
		this.normalMap = normalMap;
	}
}

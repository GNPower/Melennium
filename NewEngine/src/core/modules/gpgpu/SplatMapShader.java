package core.modules.gpgpu;

import org.lwjgl.opengl.GL13;

import core.shaders.Shader;
import core.texturing.Texture2D;
import core.utils.ResourceLoader;

public class SplatMapShader extends Shader{

	private static SplatMapShader instance = null;
	
	public static SplatMapShader getInstance() {
		if(instance == null)
			instance = new SplatMapShader();
		return instance;
	}
	
	protected SplatMapShader() {
		super();
		
		addComputeShader(ResourceLoader.loadShader("shaders/gpgpu/SplatMap.glsl"));
		
		compileShader();
		
		addUniform("normalmap");
		addUniform("N");
	}
	
	public void updateUniforms(Texture2D normalmap, int N) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		normalmap.bind();
		setUniformi("normalmap", 0);
		setUniformi("N", N);
	}
}

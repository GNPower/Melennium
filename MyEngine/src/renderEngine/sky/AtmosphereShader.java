package renderEngine.sky;

import renderEngine.shaders.test.Shader;

public class AtmosphereShader extends Shader{

	private static AtmosphereShader instance = null;
	
	public static AtmosphereShader getInstance() {
		if(instance == null)
			instance = new AtmosphereShader();
		return instance;
	}
	
	protected AtmosphereShader() {
		super();
		
		addVertexShader("");
		addFragmentShader("");
		compileShader();
	}
}

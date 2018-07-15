package core.modules.entities;

import core.kernel.Camera;
import core.scene.GameObject;
import core.shaders.Shader;
import core.utils.ResourceLoader;

public class EntityShader extends Shader{

	private static EntityShader instance = null;
	
	public static EntityShader getInstance() {
		if(instance == null)
			instance = new EntityShader();
		return instance;
	}
	
	protected EntityShader() {
		super();
		
		addVertexShader(ResourceLoader.loadShader("shaders/entities/entity_VS.glsl"));
		addFragmentShader(ResourceLoader.loadShader("shaders/entities/entity_FS.glsl"));
		
		compileShader();
		
		addUniform("m_MVP");
		addUniform("m_World");
	}
	
	public void updateUniforms(GameObject object) {		
		
		setUniform("m_MVP", object.getWorldTransform().getMVPMatrix());
		setUniform("m_World", object.getWorldTransform().getWorldMatrix());
	}
}

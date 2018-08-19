package core.modules.entities;

import core.kernel.Camera;
import core.scene.GameObject;
import core.shaders.Shader;
import core.utils.ResourceLoader;

public class EntityWireframeShader extends Shader{

	private static EntityWireframeShader instance = null;
	
	public static EntityWireframeShader getInstance() {
		if(instance == null)
			instance = new EntityWireframeShader();
		return instance;
	}
	
	protected EntityWireframeShader() {
		super();
		
		addVertexShader(ResourceLoader.loadShader("shaders/entities/entity_VS.glsl"));
		addGeometryShader(ResourceLoader.loadShader("shaders/entities/wireframe_GS.glsl"));
		addFragmentShader(ResourceLoader.loadShader("shaders/entities/wireframe_FS.glsl"));
		compileShader();
		
		addUniform("m_MVP");
		
		addUniform("m_World");
	}
	
	public void updateUniforms(GameObject object) {		
		
		setUniform("m_MVP", object.getWorldTransform().getMVPMatrix());
		setUniform("m_World", object.getWorldTransform().getWorldMatrix());
	}
}

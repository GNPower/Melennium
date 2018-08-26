package core.modules.entities;

import org.lwjgl.opengl.GL13;

import core.kernel.Camera;
import core.kernel.World;
import core.modules.terrain.TerrainNode;
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
		addUniform("lightPosition");
		addUniform("lightColour");
		addUniform("reflectivity");
		
		addUniform("tex");
		addUniform("specularmap");
//		addUniform("normalmap");
	}
	
	public void updateUniforms(GameObject object) {		
		
		Entity entity = (Entity) object;
		
		setUniform("m_MVP", object.getWorldTransform().getMVPMatrix());
		setUniform("m_World", object.getWorldTransform().getWorldMatrix());
		setUniform("lightPosition", World.getInstance().getLight().getPosition());
		setUniform("lightColour", World.getInstance().getLight().getColour());
		setUniformf("reflectivity", entity.getModel().getMaterial().getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		if(entity.getModel().getMaterial().shouldOverrideModelTexture())
			entity.getModel().getMaterial().getDiffusemap().bind();
		else
			entity.getModel().getTexture().bind();
		setUniformi("tex", 0);
		
		if(entity.getModel().getMaterial().getSpecularmap() != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE4);
			entity.getModel().getMaterial().getSpecularmap().bind();
			setUniformi("specularmap", 4);
		}else {
			System.out.println("errorrr");
		}
		
//		if(entity.getModel().getMaterial().getNormalmap() != null) {
//			GL13.glActiveTexture(GL13.GL_TEXTURE1);
//			entity.getModel().getMaterial().getNormalmap().bind();
//			setUniformi("normalmap", 1);
//		}
	}
}

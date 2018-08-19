package core.modules.terrain;

import org.lwjgl.opengl.GL13;

import core.kernel.Camera;
import core.kernel.World;
import core.scene.GameObject;
import core.shaders.Shader;
import core.utils.ResourceLoader;

public class TerrainShader extends Shader{

	private static TerrainShader instance = null;
	
	public static TerrainShader getInstance() {
		if(instance == null)
			instance = new TerrainShader();
		return instance;
	}
	
	protected TerrainShader() {
		super();
		
		addVertexShader(ResourceLoader.loadShader("shaders/terrain/terrain_VS.glsl"));
		addTessellationControlShader(ResourceLoader.loadShader("shaders/terrain/terrain_TC.glsl"));
		addTessellationEvaluationShader(ResourceLoader.loadShader("shaders/terrain/terrain_TE.glsl"));
		addGeometryShader(ResourceLoader.loadShader("shaders/terrain/terrain_GS.glsl"));
		addFragmentShader(ResourceLoader.loadShader("shaders/terrain/terrain_FS.glsl"));
		
//		System.out.println("compiling shader");
		compileShader();
//		System.out.println("done compiling");
		
		addUniform("localMatrix");
		addUniform("worldMatrix");
		addUniform("m_ViewProjection");
/**/		addUniform("lightPosition");
/**/		addUniform("lightColour");
		
		addUniform("cameraPosition");
		addUniform("scaleY");
		addUniform("lod");
		addUniform("index");
		addUniform("gap");
		addUniform("location");
		
		for(int i = 0; i < 8; i++) {
			addUniform("lod_morphing_area[" + i + "]");
		}
		
		addUniform("tessellationFactor");
		addUniform("tessellationSlope");
		addUniform("tessellationShift");
		
		addUniform("heightmap");
		addUniform("normalmap");
		addUniform("splatmap");
		
		addUniform("tbn_range");
		
		for(int i = 0; i < 3; i++) {
			addUniform("materials[" + i + "].diffusemap");
			addUniform("materials[" + i + "].normalmap");
			addUniform("materials[" + i + "].heightmap");
			addUniform("materials[" + i + "].heightScaling");
			addUniform("materials[" + i + "].horizontalScaling");
		}
	}
	
	public void updateUniforms(GameObject object) {
		
		TerrainNode terrainNode = (TerrainNode) object;
		
/**/		setUniform("lightPosition", World.getInstance().getLight().getPosition());
/**/		setUniform("lightColour", World.getInstance().getLight().getColour());
		
		setUniform("cameraPosition", Camera.getInstance().getPosition());
		setUniformf("scaleY", terrainNode.getConfig().getScaleY());
		setUniformi("lod", terrainNode.getLod());
		setUniform("index", terrainNode.getIndex());
		setUniformf("gap", terrainNode.getGap());
		setUniform("location", terrainNode.getLocation());
		
		for(int i = 0; i < 8; i++) {
			setUniformi("lod_morphing_area[" + i + "]", terrainNode.getConfig().getLod_morphing_area()[i]);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		terrainNode.getConfig().getHeightMap().bind();
		setUniformi("heightmap", 0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		terrainNode.getConfig().getNormalMap().bind();
		setUniformi("normalmap", 1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		terrainNode.getConfig().getSplatmap().bind();
		setUniformi("splatmap", 2);
		
		setUniformi("tessellationFactor", terrainNode.getConfig().getTessellationFactor());
		setUniformf("tessellationSlope", terrainNode.getConfig().getTessellationSlope());
		setUniformf("tessellationShift", terrainNode.getConfig().getTessellationShift());
		
		setUniform("m_ViewProjection", Camera.getInstance().getViewProjectionMatrix());
		setUniform("localMatrix", object.getLocalTransform().getWorldMatrix());
		setUniform("worldMatrix", object.getWorldTransform().getWorldMatrix());

		setUniformi("tbn_range", terrainNode.getConfig().getTbn_range());
		
		int texUnit = 3;
		for(int i = 0; i < 3; i++) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + texUnit);
			terrainNode.getConfig().getMaterials().get(i).getDiffusemap().bind();
			setUniformi("materials[" + i + "].diffusemap", texUnit);
			texUnit++;
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + texUnit);
			terrainNode.getConfig().getMaterials().get(i).getDisplacemap().bind();
			setUniformi("materials[" + i + "].heightmap", texUnit);
			texUnit++;
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + texUnit);
			terrainNode.getConfig().getMaterials().get(i).getNormalmap().bind();
			setUniformi("materials[" + i + "].normalmap", texUnit);
			texUnit++;
			
			setUniformf("materials[" + i + "].heightScaling", terrainNode.getConfig().getMaterials().get(i).getDisplaceScale());
			setUniformf("materials[" + i + "].horizontalScaling", terrainNode.getConfig().getMaterials().get(i).getHorizontalScale());
		}
	}
}

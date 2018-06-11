package renderEngine.shaders.terrain;

import renderEngine.entities.Camera;
import renderEngine.entities.lights.Light;
import renderEngine.models.Material;
import renderEngine.shaders.ShaderProgram;
import util.colours.Colour;
import util.maths.Maths;
import util.maths.matrices.Matrix4f;

public class TerrainShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/renderEngine/shaders/terrain/terrainVertex.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shaders/terrain/terrainFragment.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_ambientLightFactor;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_aTexture;
	private int location_bTexture;
	private int location_cTexture;
	private int location_dTexture;
	private int location_blendMap;
	private int location_renderFog;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_ambientLightFactor = super.getUniformLocation("ambientLightFactor");
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_aTexture = super.getUniformLocation("aTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_cTexture = super.getUniformLocation("cTexture");
		location_dTexture = super.getUniformLocation("dTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_renderFog = super.getUniformLocation("renderFog");
	}
	
	public void renderFog(boolean render){
		super.loadBoolean(location_renderFog, render);
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_aTexture, 1);
		super.loadInt(location_bTexture, 2);
		super.loadInt(location_cTexture, 3);
		super.loadInt(location_dTexture, 4);
		super.loadInt(location_blendMap, 5);
	}
	
	public void loadSkyColour(Colour colour){
		super.loadVector(location_skyColour, colour.getColour());
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		super.loadMatrix(location_viewMatrix, Maths.createViewMatrtix(camera));
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadAmbientLightFactor(float ambientFactor){
		super.loadFloat(location_ambientLightFactor, ambientFactor);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColourVector());
	}
	
	public void loadMaterial(Material material){
		super.loadFloat(location_shineDamper, material.getShineDamper());
		super.loadFloat(location_reflectivity, material.getReflectivity());
	}
}

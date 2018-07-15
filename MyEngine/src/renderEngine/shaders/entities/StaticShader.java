package renderEngine.shaders.entities;

import java.util.List;

import renderEngine.entities.Camera;
import renderEngine.entities.lights.Light;
import renderEngine.models.Material;
import renderEngine.shaders.ShaderProgram;
import renderEngine.textures.Texture2D;
import util.colours.Colour;
import util.maths.Maths;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class StaticShader extends ShaderProgram{
	
	public static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/renderEngine/shaders/entities/vertex.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shaders/entities/fragment.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_lightBrightnessFactor[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_ambientLightFactor;
	private int location_syncNormals;
	private int location_skyColour;
	private int location_renderFog;
	private int location_numberOfRows;
	private int location_offset;

	public StaticShader() {
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
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_ambientLightFactor = super.getUniformLocation("ambientLightFactor");
		location_syncNormals = super.getUniformLocation("syncNormals");
		location_skyColour = super.getUniformLocation("skyColour");
		location_renderFog = super.getUniformLocation("renderFog");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		

		location_lightPosition = new int[MAX_LIGHTS + 1];
		location_lightColour = new int[MAX_LIGHTS + 1];
		location_attenuation = new int[MAX_LIGHTS];
		location_lightBrightnessFactor = new int[MAX_LIGHTS + 1];
		for(int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
			location_lightBrightnessFactor[i] = super.getUniformLocation("lightBrightnessFactor[" + i + "]");
		}
		location_lightPosition[MAX_LIGHTS] = super.getUniformLocation("lightPosition[" + MAX_LIGHTS + "]");
		location_lightColour[MAX_LIGHTS] = super.getUniformLocation("lightColour[" + MAX_LIGHTS + "]");
		location_lightBrightnessFactor[MAX_LIGHTS] = super.getUniformLocation("lightBrightnessFactor[" + MAX_LIGHTS + "]");
	}
	
	public void loadNumberOfRows(int rows){
		super.loadFloat(location_numberOfRows, rows);
	}
	
	public void loadOffset(float xOff, float yOff){
		super.loadVector(location_offset, new Vector2f(xOff, yOff));
	}
	
	public void renderFog(boolean render){
		super.loadBoolean(location_renderFog, render);
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
	
	public void loadLights(Light sun, List<Light> lights){
		
		for(int i = 0; i < MAX_LIGHTS; i++) {
			if(i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColourVector());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
				super.loadFloat(location_lightBrightnessFactor[i], lights.get(i).getBrightnessFactor());
			}else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
				super.loadFloat(location_lightBrightnessFactor[i], 1);
			}
		}
		
		super.loadVector(location_lightPosition[MAX_LIGHTS], sun.getPosition());
		super.loadVector(location_lightColour[MAX_LIGHTS], sun.getColourVector());
		super.loadFloat(location_lightBrightnessFactor[MAX_LIGHTS], sun.getBrightnessFactor());
	}
	
	public void loadMaterial(Material material){
		super.loadFloat(location_shineDamper, material.getShineDamper());
		super.loadFloat(location_reflectivity, material.getReflectivity());
	}
	
	public void loadTexture(Texture2D texture){
		super.loadBoolean(location_syncNormals, texture.isSyncNormals());
	}
}

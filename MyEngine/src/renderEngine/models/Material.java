package renderEngine.models;

import renderEngine.textures.Texture2D;
import util.colours.Colour;

public class Material {

	private String name;
	private Texture2D diffuseMap;
	private Texture2D ambientMap;
	private Texture2D specularMap;
	private Texture2D normalMap;
	private Texture2D displaceMap;
	private Texture2D alphaMap;
	
	private Texture2D opacityMap;
	private Texture2D bumpMap;
	
	private Colour baseColour;
	private float displaceScale;
	private float emission;
	private float reflectivity = 0;
	private float shineDamper = 1;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Texture2D getDiffuseMap() {
		return diffuseMap;
	}
	public void setDiffuseMap(Texture2D diffuseMap) {
		this.diffuseMap = diffuseMap;
	}
	public Texture2D getAmbientMap() {
		return ambientMap;
	}
	public void setAmbientMap(Texture2D ambientMap) {
		this.ambientMap = ambientMap;
	}
	public Texture2D getSpecularMap() {
		return specularMap;
	}
	public void setSpecularMap(Texture2D specularMap) {
		this.specularMap = specularMap;
	}
	public Texture2D getNormalMap() {
		return normalMap;
	}
	public void setNormalMap(Texture2D normalMap) {
		this.normalMap = normalMap;
	}
	public Texture2D getDisplaceMap() {
		return displaceMap;
	}
	public void setDisplaceMap(Texture2D displaceMap) {
		this.displaceMap = displaceMap;
	}
	public Texture2D getAlphaMap() {
		return alphaMap;
	}
	public void setAlphaMap(Texture2D alphaMap) {
		this.alphaMap = alphaMap;
	}
	public Colour getBaseColour() {
		return baseColour;
	}
	public void setBaseColour(Colour baseColour) {
		this.baseColour = baseColour;
	}
	public float getDisplaceScale() {
		return displaceScale;
	}
	public void setDisplaceScale(float displaceScale) {
		this.displaceScale = displaceScale;
	}
	public float getEmission() {
		return emission;
	}
	public void setEmission(float emission) {
		this.emission = emission;
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	public float getShineDamper() {
		return shineDamper;
	}
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
}

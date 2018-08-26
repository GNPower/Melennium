package core.model;

import core.math.Vec3f;
import core.texturing.Texture2D;

public class Material{
	
	private String name;
	private Texture2D diffusemap;
	private boolean overrideModelTexture = false;
	private Texture2D normalmap = null;
	private Texture2D displacemap = null;
	private Texture2D ambientmap = null;
	private Texture2D specularmap = null;
	private Texture2D alphamap = null;
	private Vec3f color = new Vec3f(0.1f,0.1f,1.0f);
	private float alpha = 1;
	private float displaceScale = 1;
	private float horizontalScale = 1;
	private float emission = 0;
	private float reflectivity = 1;
	
	public Texture2D getDiffusemap() {
		return diffusemap;
	}
	public void setDiffusemap(Texture2D diffusemap) {
		this.diffusemap = diffusemap;
	}
	public Texture2D getNormalmap() {
		return normalmap;
	}
	public void setNormalmap(Texture2D normalmap) {
		this.normalmap = normalmap;
	}
	public Texture2D getDisplacemap() {
		return displacemap;
	}
	public void setDisplacemap(Texture2D displacemap) {
		this.displacemap = displacemap;
	}
	public Texture2D getAmbientmap() {
		return ambientmap;
	}
	public void setAmbientmap(Texture2D ambientmap) {
		this.ambientmap = ambientmap;
	}
	public Texture2D getSpecularmap() {
		return specularmap;
	}
	public void setSpecularmap(Texture2D specularmap) {
		this.specularmap = specularmap;
	}
	public Texture2D getAlphamap() {
		return alphamap;
	}
	public void setAlphamap(Texture2D alphamap) {
		this.alphamap = alphamap;
	}
	public Vec3f getColor() {
		return color;
	}
	public void setColor(Vec3f color) {
		this.color = color;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
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
	public void setReflectivity(float shininess) {
		this.reflectivity = shininess;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getHorizontalScale() {
		return horizontalScale;
	}
	public void setHorizontalScale(float horizontalScale) {
		this.horizontalScale = horizontalScale;
	}
	public boolean shouldOverrideModelTexture() {
		return overrideModelTexture;
	}
	public void setOverrideModelTexture(boolean overrideModelTexture) {
		this.overrideModelTexture = overrideModelTexture;
	}
}
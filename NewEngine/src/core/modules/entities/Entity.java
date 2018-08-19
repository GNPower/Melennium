package core.modules.entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import core.buffers.VAO;
import core.configs.Default;
import core.kernel.RenderContext;
import core.math.Vec3f;
import core.model.Model;
import core.renderer.RenderInfo;
import core.renderer.Renderer;
import core.scene.GameObject;
import core.texturing.Texture2D;
import core.utils.Constants;
import core.utils.objloader.OBJLoader;

public class Entity extends GameObject{
	
	private static List<Entity> entities = new ArrayList<Entity>(); 

	private EntityConfig config = null;
	
	private Model model;
	private Vec3f position, rotation;
	private boolean shouldRender = true;
	
	public Entity(String file, String texture, Vec3f position, Vec3f rotation) {
		model = new Model(OBJLoader.loadObjModel(file));
		model.setTexture(new Texture2D(texture));
		this.position = position;
		this.rotation = rotation;
		
		VAO vao = new VAO(model.getMesh());
		
		
		Renderer renderer = new Renderer();
		renderer.setVAO(vao);
		renderer.setInfo(new RenderInfo(new Default(), EntityShader.getInstance()));
		
		Renderer wireframeRenderer = new Renderer();
		wireframeRenderer.setVAO(vao);
		wireframeRenderer.setInfo(new RenderInfo(new Default(), EntityWireframeShader.getInstance()));
		
		addComponent(Constants.RenderComponents.RENDERER_COMPONENT, renderer);
		addComponent(Constants.RenderComponents.WIREFRAME_RENDERER_COMPONENT, wireframeRenderer);
		
		getWorldTransform().setTranslation(position);
		getWorldTransform().setRotation(rotation);
		
		entities.add(this);
	}
	
	public Entity(String file, Vec3f position, Vec3f rotation) {
		model = new Model(OBJLoader.loadObjModel("res/models/barrel/barrel.obj"));
		this.position = position;
		this.rotation = rotation;
		
		VAO vao = new VAO(model.getMesh());
		
		
		Renderer renderer = new Renderer();
		renderer.setVAO(vao);
		renderer.setInfo(new RenderInfo(new Default(), EntityShader.getInstance()));
		
		Renderer wireframeRenderer = new Renderer();
		wireframeRenderer.setVAO(vao);
		wireframeRenderer.setInfo(new RenderInfo(new Default(), EntityWireframeShader.getInstance()));
		
		addComponent(Constants.RenderComponents.RENDERER_COMPONENT, renderer);
		addComponent(Constants.RenderComponents.WIREFRAME_RENDERER_COMPONENT, wireframeRenderer);
		
		getWorldTransform().setTranslation(position);
		getWorldTransform().setRotation(rotation);
		
		entities.add(this);
	}
	
	public void move(Vec3f movDisplacement, Vec3f rotDisplacement) {
		position.setX(position.getX() + movDisplacement.getX());
		position.setY(position.getY() + movDisplacement.getY());
		position.setZ(position.getZ() + movDisplacement.getZ());
		rotation.setX(rotation.getX() + rotDisplacement.getX());
		rotation.setY(rotation.getY() + rotDisplacement.getY());
		rotation.setZ(rotation.getZ() + rotDisplacement.getZ());
		getWorldTransform().setRotation(rotation);
		getWorldTransform().setTranslation(position);
	}
	
	public void setPosition(Vec3f position) {
		this.position = position;
		getWorldTransform().setTranslation(position);
	}
	
	public void setRotation(Vec3f rotation) {
		this.rotation = rotation;
		getWorldTransform().setRotation(rotation);
	}
	
	public void render() {
		if(!shouldRender)
			return;
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
		
		if(RenderContext.getInstance().isWireframe())
			getComponents().get(Constants.RenderComponents.WIREFRAME_RENDERER_COMPONENT).render();
		else
			getComponents().get(Constants.RenderComponents.RENDERER_COMPONENT).render();
	}
	
	public void changeModel(Model model) {
		VAO vao = new VAO(model.getMesh());	
		
		Renderer renderer = new Renderer();
		renderer.setVAO(vao);
		renderer.setInfo(new RenderInfo(new Default(), EntityShader.getInstance()));
		
		Renderer wireframeRenderer = new Renderer();
		wireframeRenderer.setVAO(vao);
		wireframeRenderer.setInfo(new RenderInfo(new Default(), EntityWireframeShader.getInstance()));
		
		addComponent(Constants.RenderComponents.RENDERER_COMPONENT, renderer);
		addComponent(Constants.RenderComponents.WIREFRAME_RENDERER_COMPONENT, wireframeRenderer);
	}
	
	public static List<Entity> getEntities(){
		return entities;
	}

	public Vec3f getPosition() {
		return position;
	}

	public Vec3f getRotation() {
		return rotation;
	}

	public boolean isShouldRender() {
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender) {
		this.shouldRender = shouldRender;
	}

	public EntityConfig getConfig() {
		if(config == null)
			return EntityConfig.defaultConfig();
		return config;
	}

	public void setConfig(EntityConfig config) {
		this.config = config;
	}
}

package core.modules.entities;

import core.buffers.VAO;
import core.configs.Default;
import core.kernel.RenderContext;
import core.math.Vec3f;
import core.model.Model;
import core.renderer.RenderInfo;
import core.renderer.Renderer;
import core.scene.GameObject;
import core.utils.Constants;
import core.utils.objloader.OBJLoader;

public class Entity extends GameObject{

	private Model model;
	private Vec3f position, rotation;
	
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
	
	public void render() {
		if(RenderContext.getInstance().isWireframe())
			getComponents().get(Constants.RenderComponents.WIREFRAME_RENDERER_COMPONENT).render();
		else
			getComponents().get(Constants.RenderComponents.RENDERER_COMPONENT).render();
	}
}

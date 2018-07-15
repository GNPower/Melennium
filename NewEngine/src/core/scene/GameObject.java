package core.scene;

import java.util.HashMap;

import core.utils.Constants;

public class GameObject extends Node{
	
	private HashMap<Constants.RenderComponents, Component> components;
	
	public GameObject() {
		components = new HashMap<Constants.RenderComponents, Component>();
	}
	
	public void input() {
		for(Constants.RenderComponents key : components.keySet()) {
			components.get(key).input();
		}
		super.input();
	}
	
	public void update() {
		for(Constants.RenderComponents key : components.keySet()) {
			components.get(key).update();
		}
		super.update();
	}
	
	public void render() {
		for(Constants.RenderComponents key : components.keySet()) {
			components.get(key).render();
		}
		super.render();
	}
	
	public void addComponent(Constants.RenderComponents key, Component component) {
		component.setParent(this);
		components.put(key, component);
	}
	
	public HashMap<Constants.RenderComponents, Component> getComponents() {
		return components;
	}

	public void setComponents(HashMap<Constants.RenderComponents, Component> components) {
		this.components = components;
	}
}

package core.kernel;

import core.math.Vec3f;
import core.modules.lights.Light;

public class World {
	
	private Light light;

	private static World instance = null;
	
	public static World getInstance() {
		if(instance == null)
			instance = new World();
		return instance;
	}
	
	public void init() {
		light = new Light(new Vec3f(100,1000,1), new Vec3f(1,1,1));
	}
	
	protected World() {
	}
	
	public Light getLight() {
		return light;
	}
}

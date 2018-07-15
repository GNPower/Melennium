package core.modules.sky;

import core.buffers.VAO;
import core.configs.CCW;
import core.kernel.RenderContext;
import core.math.Vec3f;
import core.model.Mesh;
import core.renderer.RenderInfo;
import core.renderer.Renderer;
import core.scene.GameObject;
import core.utils.Constants;
import core.utils.objloader.OBJLoader;

public class Skydome extends GameObject{

	public Skydome() {
		Mesh mesh = new OBJLoader().load("./res/models/dome", "dome.obj", null)[0].getMesh();
		VAO vao = new VAO(mesh);
		
		Renderer renderer = new Renderer();
		renderer.setVAO(vao);
		renderer.setInfo(new RenderInfo(new CCW(), AtmosphereShader.getInstance()));
		addComponent(Constants.RenderComponents.RENDERER_COMPONENT, renderer);
		getWorldTransform().setScaling(new Vec3f(Constants.ZFAR * 0.5f, 
											Constants.ZFAR * 0.5f, 
											Constants.ZFAR * 0.5f));
	}
	
	@Override
	public void render() {
		if(!RenderContext.getInstance().isWireframe()) {
			super.render();
		}
	}
}

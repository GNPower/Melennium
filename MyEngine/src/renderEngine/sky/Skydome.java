package renderEngine.sky;

import renderEngine.models.Mesh;
import renderEngine.models.buffers.VAO;
import util.ResourceLoader;

public class Skydome {

	private static final String OBJ_FILE = "";
	
	public Skydome() {
		Mesh mesh = ResourceLoader.loadObjModel("dome");
		VAO vao = new VAO(mesh);
	}
}

package renderEngine.shaders.guis;

import renderEngine.shaders.ShaderProgram;
import util.maths.matrices.Matrix4f;

public class GuiShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/renderEngine/shaders/guis/vertex.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shaders/guis/fragment.glsl";
	
	private int location_transformationMatrix;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);		
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}

package renderEngine.shaders.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import util.ResourceLoader;
import util.buffers.BufferUtil;
import util.maths.Quaternion;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public abstract class Shader {

	private int programId;
	private HashMap<String, Integer> uniforms;
	private List<Integer> shaderIds;
	
	public Shader() {
		programId = GL20.glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		shaderIds = new ArrayList<Integer>();
		
		if(programId == 0) {
			System.err.println("Shader Creation Failed");
			System.exit(1);
		}
	}
	
	public void bind() {
		GL20.glUseProgram(programId);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void addUniform(String uniform) {
		int uniformLocation = GL20.glGetUniformLocation(programId, uniform);
		
		if(uniformLocation == 0xFFFFFFFF) {
			System.err.println(this.getClass().getName() + "Error: Could bnot find uniform location " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		uniforms.put(uniform, uniformLocation);
	}
	
	public void addUniformBlock(String uniform) {
		int uniformLocation = GL31.glGetUniformBlockIndex(programId, uniform);
		
		if(uniformLocation == 0xFFFFFFFF) {
			System.err.println(this.getClass().getName() + "Error: Could bnot find uniform location " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		uniforms.put(uniform, uniformLocation);
	}
	
	public void addVertexShader(String text) {
		addProgram(text, GL20.GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String text) {
		addProgram(text, GL32.GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String text) {
		addProgram(text, GL20.GL_FRAGMENT_SHADER);
	}
	
	public void addTessellationControlShader(String text) {
		addProgram(text, GL40.GL_TESS_CONTROL_SHADER);
	}
	
	public void addTessellationEvaluationShader(String text) {
		addProgram(text, GL40.GL_TESS_EVALUATION_SHADER);
	}
	
	public void addComputeShader(String text) {
		addProgram(text, GL43.GL_COMPUTE_SHADER);
	}
	
	public void compileShader() {
		GL20.glLinkProgram(programId);
		
		if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
			System.err.println(this.getClass().getName() + " " + GL20.glGetProgramInfoLog(programId, 1024));
			System.exit(1);
		}
		
		GL20.glValidateProgram(programId);
		
		if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == 0) {
			System.err.println(this.getClass().getName() + " " + GL20.glGetProgramInfoLog(programId, 1024));
			System.exit(1);
		}				
	}
	
	private void addProgram(String source, int type) {
		String text = ResourceLoader.loadShader(source);
		
		int shader = GL20.glCreateShader(type);
		shaderIds.add(shader);
		
		if(shader == 0) {
			System.err.println(this.getClass().getName() + " Shader creation failed");
			System.exit(1);
		}
		
		GL20.glShaderSource(shader, text);
		GL20.glCompileShader(shader);
		
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
			System.err.println(this.getClass().getName() + " " + GL20.glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		GL20.glAttachShader(programId, shader);
	}
	
	public void loadInt(String uniformName, int value) {
		GL20.glUniform1i(uniforms.get(uniformName), value);
	}
	
	public void loadFloat(String uniformName, float value) {
		GL20.glUniform1f(uniforms.get(uniformName), value);
	}
	
	public void loadVector(String uniformName, Vector2f vec) {
		GL20.glUniform2f(uniforms.get(uniformName), vec.getX(), vec.getY());
	}
	
	public void loadVector(String uniformName, Vector3f vec) {
		GL20.glUniform3f(uniforms.get(uniformName), vec.getX(), vec.getY(), vec.getZ());
	}
	
	public void loadMatrix(String uniformName, Matrix4f mat) {
		GL20.glUniformMatrix4(uniforms.get(uniformName), true, BufferUtil.createFlippedBuffer(mat));
	}
	
	public void loadQuaternion(String uniformName, Quaternion quat) {
		GL20.glUniform4f(uniforms.get(uniformName), quat.getX(), quat.getY(), quat.getZ(), quat.getW());
	}
	
	public void bindUniformBlock(String uniformBlockName, int uniformBlockBinding) {
		GL31.glUniformBlockBinding(programId, uniforms.get(uniformBlockName), uniformBlockBinding);
	}
	
	public void bindFragDataLocation(String name, int index) {
		GL30.glBindFragDataLocation(programId, index, name);
	}
	
	public void cleanUp() {
		unbind();
		for(Integer id : shaderIds) {
			GL20.glDetachShader(programId, id);
			GL20.glDeleteShader(id);
		}
		GL20.glDeleteProgram(programId);
	}
	
	public int gtProgramId() {
		return programId;
	}
}
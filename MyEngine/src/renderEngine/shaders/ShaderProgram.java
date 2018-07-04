package renderEngine.shaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import util.buffers.BufferUtil;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public abstract class ShaderProgram {

	private int programID, vertexShaderID, fragmentShaderID;

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();

		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);

		bindAttributes();

		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);

		if (GL20.glGetShaderi(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could Not Validate Shader Program");
			System.err.println(GL20.glGetProgramInfoLog(programID, 1000));
			System.exit(1);
		}

		getAllUniformLocations();
	}

	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}

	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	protected void loadVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.getX(), vector.getY());
	}

	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
	}
	
	protected void loadBoolean(int location, boolean value){
		int data = 0;
		if(value)
			data = 1;
		GL20.glUniform1i(location, data);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix){
		GL20.glUniformMatrix4(location, true, BufferUtil.createFlippedBuffer(matrix));
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	private static int loadShader(String fileName, int type) {
		StringBuilder source = new StringBuilder();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;

			while ((line = reader.readLine()) != null) {
				source.append(line).append("\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could Not Find Shader File!");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error Interpreting Shader File!");
			e.printStackTrace();
			System.exit(1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, source);
		GL20.glCompileShader(shaderID);

		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could Not Compile Shader:");
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 1000));
			System.exit(1);
		}

		return shaderID;
	}
}

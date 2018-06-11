package util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import util.colours.Colour;

public class RenderUtil {

	public static final int GL_WIREFRAME_VIEW = 11;
	public static final int GL_NORMAL_VIEW = 12;
	public static final int GL_POINT_VIEW = 13;

	public static final int GL_CULL_BACK = 21;
	public static final int GL_CULL_FRONT = 22;
	public static final int GL_CULL_NONE = 23;

	private static boolean cullEnabled = false;
	private static int cullMode = GL_CULL_NONE;
	private static int renderMode = GL_NORMAL_VIEW;

	private static Colour colour = new Colour();

	public static String openGLVersion() {
		return GL11.glGetString(GL11.GL_VERSION);
	}

	public static void initSettings() {
		GL11.glFrontFace(GL11.GL_CCW);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
		
		colour.setColour(0.5f, 0.5f, 0.5f);
	}

	public static void clearScreen() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(colour.getR(), colour.getG(), colour.getB(), colour.getAlpha());
		GL11.glClearDepth(1.0);		
	}

	public static void setRenderMode(int mode) {
		int face = 0;
		switch (cullMode) {
		case GL_CULL_NONE:
			face = GL11.GL_FRONT_AND_BACK;
			break;
		case GL_CULL_BACK:
			face = GL11.GL_FRONT;
			break;
		case GL_CULL_FRONT:
			face = GL11.GL_BACK;
			break;
		default:
			System.err.println("RenderUtil cullMode Holds Invalid Value: OpenGL Render Mode Was Not Changed!");
			return;
		}

		if (mode == GL_NORMAL_VIEW) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			renderMode = GL_NORMAL_VIEW;
		} else if (mode == GL_WIREFRAME_VIEW) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			renderMode = GL_WIREFRAME_VIEW;
		} else if (mode == GL_POINT_VIEW) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
			renderMode = GL_POINT_VIEW;
		} else {
			System.err.println("Invalid RenderMode Variable Passed To RenderUtil.setRenderMode(), "
					+ "No Actions Applied, The Method Did Not Run");
		}
	}

	public static void advanceRenderMode() {
		int render = renderMode;
		if (render - 1 >= GL_WIREFRAME_VIEW) {
			render--;
		} else {
			render = GL_POINT_VIEW;
		}
		setRenderMode(render);
	}

	public static void setCullMode(int mode) {
		switch (mode) {
		case GL_CULL_BACK:
			if (!cullEnabled)
				enableCulling(true);
			GL11.glCullFace(GL11.GL_BACK);
			cullMode = GL_CULL_BACK;
			//System.out.println("Culling: Back Face");
			break;
		case GL_CULL_FRONT:
			if (!cullEnabled)
				enableCulling(true);
			GL11.glCullFace(GL11.GL_FRONT);
			cullMode = GL_CULL_FRONT;
			//System.out.println("Culling: Front Face");
			break;
		case GL_CULL_NONE:
			if (cullEnabled)
				enableCulling(false);
			cullMode = GL_CULL_NONE;
			break;
		default:
			System.err.println("Bad Value Assigned Inm RenerUtil.setCullMode(), "
					+ "No Actuions Applied, The Method Did Not Run!");
			break;
		}
	}

	private static void enableCulling(boolean cull) {
		if (cull) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			cullEnabled = true;
			//System.out.println("OpenGL Culling Mode: Enabled");
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);
			cullEnabled = false;
			//System.out.println("OpenGL Culling Mode: Disabled");
		}
	}
	
	public static void enableTextureMippmapping(){
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		//increase the last number to decrease the detail of textures, good for stopping lag
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
	}

	public static Colour getColour() {
		return colour;
	}

	public static void setColour(Colour colour) {
		RenderUtil.colour = colour;
	}
}

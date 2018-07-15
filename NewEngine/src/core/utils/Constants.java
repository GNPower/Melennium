package core.utils;

public class Constants {

	public static final long NANOSECOND = 1000000000;
	public static final float ZFAR = 10000.0f;
	public static final float ZNEAR = 0.1f;
	
	public static final String WINDOW_TITLE = "Vortex Engine 1.1.0";
	public static final int WNIDOW_WIDTH = 1280;
	public static final double ASPECT_RATIO = 16.0 / 9.0;

	public static enum RenderComponents{
		RENDERER_COMPONENT, WIREFRAME_RENDERER_COMPONENT;
	}

//	public static final String RENDERER_COMPONENT = "Renderer";
//	public static final String WIREFRAME_RENDERER_COMPONENT = "WFRenderer";
}

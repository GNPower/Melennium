package util.colours;

import java.util.HashMap;

public class ColourUtil {
	
	public static final Colour white = new Colour(1, 1, 1, 1);
	public static final Colour red = new Colour(1, 0, 0, 1);
	public static final Colour green = new Colour(0, 1, 0, 1);
	public static final Colour blue = new Colour(0, 0, 1, 1);
	public static final Colour orange = new Colour(1, (float) (153.0 / 155.0), (float) (102.0 / 155.0), 1);
	public static final Colour yellow = new Colour(1, 1, 0, 1);
	
	private static HashMap<String, Colour> colours = new HashMap<String, Colour>();
	
	public static void addColour(String name, Colour colour){
		colours.put(name, colour);
	}
	
	public static Colour getColour(String name){
		return colours.get(name);
	}
}

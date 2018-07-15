package core.utils.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.utils.Constants;

public class ResourceLoader {

	public static String[] loadWindowSettings(String filePath) {
		if(true)
			return Constants.DEFAULT_WINDOW_SETTINGS;
		
		
		BufferedReader reader = null;
		String[] data = Constants.DEFAULT_WINDOW_SETTINGS;
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				
				if(tokens.length == 0 || tokens[0].startsWith("#"))
					continue;
				
				if(tokens[0].equals("Title:")) {
					String title = "";
					for(int i = 1; i < tokens.length; i++){
						title += tokens[i];
					}
					data[0] = title;
				}
				if(tokens[0].equals("width")) {
					data[1] = tokens[1];
				}
				if(tokens[0].equals("aspect_ratio")) {
					data[2] = tokens[1];
					data[3] = tokens[2];
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			//TODO: handle all errors
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}

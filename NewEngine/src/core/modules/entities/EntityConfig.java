package core.modules.entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.texturing.Texture2D;
import core.utils.Util;

public class EntityConfig {
	
	private Texture2D normalmap;
	private float[] directionalSpeeds;
	private float rotationSpeed;

	public EntityConfig(String filePath) {
		directionalSpeeds = new float[] {0.1f, 0.1f, 0.1f};
		rotationSpeed = 0.8f / 8f;
		loadFile(filePath);
	}
	
	public static EntityConfig defaultConfig() {
		return new EntityConfig();
	}
	
	protected EntityConfig() {
		directionalSpeeds = new float[] {0.1f, 0.1f, 0.1f};
		rotationSpeed = 0.8f / 8f;
		normalmap = null;
	}
	
	private void loadFile(String filePath) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			
			String line;
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].startsWith("#"))
					continue;
				
				if(tokens[0].equals("normalmap")) {
					normalmap = new Texture2D(tokens[1]);
				}
				else if(tokens[0].equals("rotationSpeed")) {
					rotationSpeed = Float.valueOf(tokens[1]);
				}
				else if(tokens[0].equals("movement:")) {
					for(int i = 0; i < 3; i++) {
						line = reader.readLine();
						tokens = line.split(" ");
						tokens = Util.removeEmptyStrings(tokens);
						
						if(tokens[0].equals("direction_" + (i + 1))) {
							directionalSpeeds[i] = Float.valueOf(tokens[1]);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Texture2D getNormalmap() {
		return normalmap;
	}
	
	public float getXAxisSpeed() {
		return directionalSpeeds[0];
	}
	
	public float getYAxisSpeed() {
		return directionalSpeeds[1];
	}
	
	public float getZAxisSpeed() {
		return directionalSpeeds[2];
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}
}

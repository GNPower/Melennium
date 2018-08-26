package core.modules.entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.math.Vec3f;
import core.model.Material;
import core.model.Model;
import core.texturing.Texture2D;
import core.utils.Util;
import core.utils.objloader.OBJLoader;

public class EntityConfig {
	
	private String filePath;
	private List<Material> materials;
	private Vec3f initialPosition, initialRotation;

	public EntityConfig(String filePath) {
		this.filePath = filePath;
		materials = new ArrayList<Material>();
		initialPosition = new Vec3f(0,0,0);
		initialRotation = new Vec3f(0,0,0);
	}

	public static EntityConfig defaultConfig() {
		return new EntityConfig();
	}

	private EntityConfig() {
	}

	public Model loadFile() {
		BufferedReader reader = null;
		Model model = null;

		try {
			reader = new BufferedReader(new FileReader(filePath));

			String line = "";
			String[] tokens;
			while ((line = reader.readLine()) != null) {
				tokens = Util.removeEmptyStrings(line.split(" "));

				if (tokens.length == 0 || tokens[0].startsWith("#"))
					continue;

				if (tokens[0].equals("model:")) {
					model = new Model(OBJLoader.loadObjModel(tokens[1]));
				}

				
				if (tokens[0].equals("position:")) {
					initialPosition.setX(Float.valueOf(tokens[1]));
					initialPosition.setY(Float.valueOf(tokens[2]));
					initialPosition.setZ(Float.valueOf(tokens[3]));
				}
				if (tokens[0].equals("rotation:")) {
					initialRotation.setX(Float.valueOf(tokens[1]));
					initialRotation.setY(Float.valueOf(tokens[2]));
					initialRotation.setZ(Float.valueOf(tokens[3]));
				}

				
				if (tokens[0].equals("material")) {
					Material material = new Material();
					material.setName(tokens[1]);
					
					line = reader.readLine();
					while(line != "/material" && line != null) {
						tokens = Util.removeEmptyStrings(line.split(" "));
						
						if(tokens[0].equals("material_DIF")) {
							Texture2D diffusemap = new Texture2D(tokens[1]);
							material.setDiffusemap(diffusemap);
							material.setOverrideModelTexture(true);
						}
						else if(tokens[0].equals("material_NRM")) {
							material.setNormalmap(new Texture2D(tokens[1]));
						}
						else if(tokens[0].equals("material_SPEC")) {
							material.setSpecularmap(new Texture2D(tokens[1]));
						}
						else if(tokens[0].equals("material_ALPH")) {
							material.setAlphamap(new Texture2D(tokens[1]));
						}
						else if(tokens[0].equals("material_rgb")) {
							material.setColor(new Vec3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
						}
						else if(tokens[0].equals("material_baseAlpha")) {
							material.setAlpha(Float.valueOf(tokens[1]));
						}
						else if(tokens[0].equals("material_emission")) {
							material.setEmission(Float.valueOf(tokens[1]));
						}
						else if(tokens[0].equals("material_baseSpecular")) {
							material.setReflectivity(Float.valueOf(tokens[1]));
						}
						else if(tokens[0].equals("/material"))
							break;
						
						line = reader.readLine();
					}
					
					materials.add(material);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(model == null) {
			System.err.println("no model was loaded");
			System.exit(1);
		}
		
		return model;
	}
	
	public Material[] getMaterials() {
		return materials.toArray(new Material[0]);
	}

	public Vec3f getInitialPosition() {
		return initialPosition;
	}

	public Vec3f getInitialRotation() {
		return initialRotation;
	}
}

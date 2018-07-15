package util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import renderEngine.models.Mesh;
import renderEngine.models.Vertex;
import renderEngine.textures.Texture2D;
import renderEngine.textures.TexturePack;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class ResourceLoader {
	
	public static final String TEXTURE_PATH = "res/textures/";
	public static final String TERRAIN_PATH = "terrains/";

	public static int[][] loadLOD(String configFile){
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader("res/configs/" + configFile));
			String line;
			int lod = 0;
			int[][] lodData = null;
			
			while((line = reader.readLine()) != null){
				if(line.startsWith("#") || line.equals(""))
					continue;
				String[] tokens = line.split(" ");
				
				if(tokens[0].equals("LOD_AMOUNT:")){
					lod = Integer.valueOf(tokens[1]);
					lodData = new int[lod][2];
				}				
				if(tokens[0].equals("LOD_LISTING:")){
					for(int i = 0; i < lod; i++){
						line = reader.readLine();
						String[] data = line.split(" ");
						lodData[i][0] = Integer.valueOf(data[1]);
						lodData[i][1] = Integer.valueOf(data[2]);
					}
					break;
				}
			}
			reader.close();
			return lodData;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Mesh loadObjModel(String fileName) {
    	BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("res/models/" + fileName + ".obj")));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
            System.exit(1);
        }       
        
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        
        List<Integer> indices = new ArrayList<Integer>();
        List<String[]> faces = new ArrayList<String[]>();
        
        Vertex[] vertexArray = null;
        int[] indicesArray = null;
        
        try {
        	
        	String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if(tokens[0].equals("#") || tokens[0].equals("o") || tokens[0].equals("s")){
                	continue;
                } else if (tokens[0].equals("v")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    vertices.add(vertex);
                } else if (tokens[0].equals("vt")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(texture);
                } else if (tokens[0].equals("vn")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    normals.add(normal);
                } else if (tokens[0].equals("f")) {
                	String[] face1 = tokens[1].split("/");
                	String[] face2 = tokens[2].split("/");
                	String[] face3 = tokens[3].split("/");
                	faces.add(face1);
                	faces.add(face2);
                	faces.add(face3);                    
                }
            }
            reader.close();
            vertexArray = new Vertex[vertices.size()];
                 
            for(String[] face : faces){
            	int currentVertex = Integer.parseInt(face[0]) - 1;
            	int currentTexture = Integer.parseInt(face[1]) - 1;
            	int currentNormal = Integer.parseInt(face[2]) - 1;
                indices.add(currentVertex);
                
                Vector2f texture = new Vector2f(textures.get(currentTexture).getX(), 1 - textures.get(currentTexture).getY());
                
                Vertex vert = new Vertex(vertices.get(currentVertex), texture, 
                		normals.get(currentNormal));
                vertexArray[currentVertex] = vert;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        indicesArray = new int[indices.size()]; 
        for(int i=0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }
        
        return new Mesh(vertexArray, indicesArray);
    }

	public static int loadTexture2D(String fileName) {

		String[] splitName = fileName.split("\\.");
		String ext = splitName[splitName.length - 1];

		Texture texture = null;

		try {
			texture = TextureLoader.getTexture(ext, new FileInputStream(TEXTURE_PATH + fileName));
			RenderUtil.enableTextureMippmapping();
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found!");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error Loading Texture File!");
			e.printStackTrace();
			System.exit(1);
		}
		
		int texID = texture.getTextureID();
		
		return texID;
	}
	
	public static TexturePack loadTexturePack(String directory){
		Texture2D[] textures = new Texture2D[TexturePack.SUPPORTED_TEXTUREPACK_AMOUNT]; 
		for(int i = 0; i < TexturePack.SUPPORTED_TEXTUREPACK_AMOUNT; i++){
			String fileName = TERRAIN_PATH + directory + "/tile" + (i + 1) + ".png";
			textures[i] = new Texture2D(fileName);
		}
		Texture2D background = new Texture2D(TERRAIN_PATH + directory + "/background.png");
		
		return new TexturePack(background, textures);
	}
	
	public static BufferedImage loadImage(String fileName){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(TEXTURE_PATH + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	public static String loadShader(String filePath) {
		StringBuilder source = new StringBuilder();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader("./res/" + filePath));
			String line;
			
			while((line = reader.readLine()) != null) {
				source.append(line).append("\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return source.toString();
	}
}
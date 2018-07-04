package util.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import renderEngine.models.Mesh;
import renderEngine.models.Vertex;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class ObjLoader {

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
}

/*OLD WORKING LOADER
     public static Mesh loadObjModel(String fileName) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/models/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        
        Vertex[] vertexArray = null;
        int[] indicesArray = null;
        try {
 
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    vertexArray = new Vertex[vertices.size()];
                    break;
                }
            }
 
            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                 
                processVertex(vertex1,indices,vertices,textures,normals,vertexArray);
                processVertex(vertex2,indices,vertices,textures,normals,vertexArray);
                processVertex(vertex3,indices,vertices,textures,normals,vertexArray);
                line = reader.readLine();
            }
            reader.close();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        indicesArray = new int[indices.size()]; 
        for(int i=0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }

        return new Mesh(vertexArray, indicesArray);
    }
 
    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector3f> vertices, 
            List<Vector2f> textures, List<Vector3f> normals, Vertex[] vertexArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1); 
        Vector3f currentVertex = vertices.get(currentVertexPointer);
        
        Vertex vert = new Vertex(currentVertex, currentTex, currentNorm);
        vertexArray[currentVertexPointer] = vert;
    }
 
 */
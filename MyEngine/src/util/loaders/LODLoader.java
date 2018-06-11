package util.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LODLoader {
	
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

//	public static int[][] loadLOD(String fileName){
//		
//		String[] fileNames = fileName.split("\\.");
//		String ext = fileNames[fileNames.length - 1];
//		
//		if(!ext.equals("ldd")){
//			System.err.println("Error: File format not supported: " + ext);
//			new Exception().printStackTrace();
//			System.exit(1);
//		}
//		
//		BufferedReader reader = null;
//		int[][] lodData = null;
//		
//		try {
//			reader = new BufferedReader(new FileReader(new File("res/configs/" + fileName)));
//			
//			String line;
//			int counter = 0;
//			while((line = reader.readLine()) != null){
//				String[] tokens = line.split(" ");				
//				if(line.startsWith("#")){
//					continue;
//				}
//				else if(tokens[0].equals("LOD_AMOUNT:")){
//					lodData = new int[Integer.parseInt(tokens[1])][2];
//				}else if(tokens[0].startsWith("lod")){
//					lodData[counter][0] = Integer.parseInt(tokens[1]);
//					lodData[counter][1] = Integer.parseInt(tokens[2]);
//				}
//			}
//			reader.close();
//			return lodData;
//			
//		} catch (FileNotFoundException e) {
//			System.err.println("Could not find file!");
//			e.printStackTrace();
//			System.exit(1);
//		} catch (IOException e) {
//			System.err.println("Error reading ldd file. It may be corrupted!");
//			e.printStackTrace();
//			System.exit(1);
//		}	
//		
//		return null;
//	}
}

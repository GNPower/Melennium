package coreEngine.core;

import coreEngine.launcher.Launcher;
import coreEngine.screen.Window;

public class Main {

	public static void main(String[] args) {
		
		Launcher launcher = new Launcher();
		if(!launcher.run(args))
			return;
		
		
		Window window = new Window("Test Engine 2.0", 1280, 16, 9);		
//		int[][] data = LODLoader.loadLOD("terrain.cfg");
//		for(int i = 0; i < data.length; i++){
//			System.out.println("LOD " + i + ": " + data[i][0] + "		" + data[i][1]);
//		}				
		Game game = new Game(window);
		game.start();
	}
}
package core;

import core.kernel.Game;

public class Main {

	public static void main(String[] args) {
		
		Game game = new Game();
		game.getEngine().createWindow();
		game.init();
		game.launch();
	}
}

package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("launcher.fxml"));		
		primaryStage.setTitle("Millennium Launcher");
		Scene scene = new Scene(root, 1280, 720);
		scene.getStylesheets().add(LauncherUtils.getStyleURL("Orion"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

package coreEngine.launcher;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Launcher extends Application{

	Button button;
	
	public Launcher() {
	}
	
	public boolean run(String[] args) {
		launch(args);
		return false;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Millennium Engine Launcher");
		
		button = new Button();
		button.setText("click me");
		button.setOnAction(e -> System.out.println("Click me again daddy"));
		
		StackPane layout = new StackPane();
		layout.getChildren().add(button);
		
		Scene scene = new Scene(layout, 300, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

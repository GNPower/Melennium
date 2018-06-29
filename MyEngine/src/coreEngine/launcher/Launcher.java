package coreEngine.launcher;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application{

	Stage window;
	Scene home, games, engine, characterEditor;
	
	public Launcher() {
	}
	
	public boolean run(String[] args) {
		launch(args);
		return false;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("title is title-like");
		
		home = setHomeScene();
		home.getStylesheets().add(LauncherUtils.getStyleURL("Orion"));
		window.setScene(home);
		window.show();
	}		
	
	private Scene setHomeScene() {
		GridPane menuPane = new GridPane();
		menuPane.setPadding(new Insets(10, 10, 10, 10));
		menuPane.setHgap(25);
		menuPane.setVgap(10);
		
		Button homeButton = new Button("Home");
		homeButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();
		});
		Button gamesButton = new Button("Games");
		gamesButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();
		});
		Button serversButton = new Button("Servers");
		serversButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();
		});
		Button engineButton = new Button("Engine");
		engineButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();
		});
		Button editorButton = new Button("Editors");
		editorButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();
		});
		Button settingsButton = new Button("Settings");
		settingsButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();			
		});
		GridPane.setConstraints(homeButton, 0, 0);
		GridPane.setConstraints(gamesButton, 1, 0);
		GridPane.setConstraints(serversButton, 2, 0);
		GridPane.setConstraints(engineButton, 3, 0);
		GridPane.setConstraints(editorButton, 4, 0);
		GridPane.setConstraints(settingsButton, 5, 0);
		menuPane.getChildren().addAll(homeButton, gamesButton, serversButton, engineButton, editorButton, settingsButton);
		
		GridPane loginPane = new GridPane();
		loginPane.setPadding(new Insets(10, 10, 10, 10));
		loginPane.setHgap(8);
		loginPane.setVgap(10);
		
		Label usernameLabel = new Label("Username: ");
		//usernameLabel.setStyle("-fx-text-fill: #e8e8e8");
		TextField usernameInput = new TextField();
		usernameInput.setPromptText("type here");
		
		Label passwordLabel = new Label("Password: ");
		TextField passwordInput = new TextField();
		passwordInput.setPromptText("type here");
		
		Label websiteLink = new Label("www.websitehere.com");
		
		Button launchButton = new Button("LAUNCH");
		launchButton.getStyleClass().add("button-blue");
		launchButton.setId("bold-label");
		launchButton.setOnAction(e -> {
			LauncherUtils.unimplementedAlert();
		});
		
		GridPane.setConstraints(usernameLabel, 0, 0);
		GridPane.setConstraints(passwordLabel, 0, 1);
		GridPane.setConstraints(usernameInput, 1, 0);
		GridPane.setConstraints(passwordInput, 1, 1);
		GridPane.setConstraints(websiteLink, 2, 0);
		GridPane.setConstraints(launchButton, 3, 0);
		loginPane.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, websiteLink, launchButton);
		
		//TUTORIAL STUFF STARTS HERE
		VBox box = new VBox();
		Button button = new Button("Click Me");	
		
		CheckBox box1 = new CheckBox("Label Me");
		CheckBox box2 = new CheckBox("Not Me");
		box2.setSelected(true);
		
		ChoiceBox<String> choices = new ChoiceBox<String>();
		choices.getItems().addAll("apples", "potato", "muffin", "banana");
		choices.setValue("apples");
		
		choices.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
			System.out.println("you selected " + newValue);
		});
		
		ComboBox<String> combo = new ComboBox<String>();
		combo.getItems().addAll("lod", "erth", "captain crunch", "mexico");
		combo.setPromptText("pick me now");
		combo.setEditable(true);		
		combo.setOnAction(e -> {
			System.out.println("you picked: " + combo.getValue());
		});
		
		ListView<String> list = new ListView<String>();
		list.getItems().addAll("movie me", "another movie", "this sucks", "lelelelel");
		list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		button.setOnAction(e -> {
			if(box1.isSelected())
				System.out.println("Ive Been Labeled");
			if(box2.isSelected())
				System.out.println("Noo not me");
			ObservableList<String> olist = list.getSelectionModel().getSelectedItems();
			for(String item : olist) {
				System.out.println(item + " was selected");
			}
		});
		
		TreeItem<String> root, base1, base2;
		
		//Root
		root = new TreeItem<String>();
		root.setExpanded(true);
		
		//Base1
		base1 = LauncherUtils.createBranch("base1", root);
		LauncherUtils.createBranch("testme1", base1);
		LauncherUtils.createBranch("testme2", base1);
		LauncherUtils.createBranch("testme3", base1);
		LauncherUtils.createBranch("testme4", base1);
		
		//Base2
		base2 = LauncherUtils.createBranch("base2", root);
		LauncherUtils.createBranch("tryme1", base2);
		LauncherUtils.createBranch("tryme2", base2);
		LauncherUtils.createBranch("tryme3", base2);
		LauncherUtils.createBranch("tryme4", base2);
		
		//Create tree
		TreeView<String> view = new TreeView<String>(root);
		view.setShowRoot(false);
		view.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
			if(newValue != null)
				System.out.println(newValue.getValue());
		});
		
		box.getChildren().addAll(box1, box2, choices, combo, view, button);
		//TUTORIAL STUFF ENDS HERE
		
		BorderPane homePane = new BorderPane();
		homePane.setTop(menuPane);
		homePane.setBottom(loginPane);
		homePane.setCenter(box);
		
		return new Scene(homePane, 1280, 720);
	}
}

package launcher;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Control implements Initializable{
	
	public Button button;
	public Button engineLaunch;
	public TabPane tabPane;
	
	public TextField homeUsername;
	public TextField gamesUsername;
	public TextField serversUsername;
	public TextField engineUsername;
	public TextField editorsUsername;
	
	public PasswordField homePassword;
	public PasswordField gamesPassword;
	public PasswordField serversPassword;
	public PasswordField enginePassword;
	public PasswordField editorsPassword;
	
	public ImageView image;
	
	private String usernameText = "";
	private String passwordText = "";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("loading int itnint");
		tabPane.getSelectionModel().selectedItemProperty().addListener( (v, oldTab, newTab) -> {
			switch(oldTab.getText()) {
			case "Home":
				usernameText = homeUsername.getText();
				passwordText = homePassword.getText();
				break;
			case "Games":
				usernameText = gamesUsername.getText();
				passwordText = gamesPassword.getText();
				break;
			}
			homeUsername.setText(usernameText);
			gamesUsername.setText(usernameText);
//			serversUsername.setText(usernameText);
			engineUsername.setText(usernameText);
//			editorsUsername.setText(usernameText);
			
			homePassword.setText(passwordText);
			gamesPassword.setText(passwordText);
//			serversPassword.setText(passwordText);
			enginePassword.setText(passwordText);
//			editorsPassword.setText(passwordText);			
		});
	}	
	
	public void clicked() {
		System.out.println("you clicked me");
//		try {
//			new ProcessBuilder("C:\\Program Files (x86)\\Notepad++\\notepad++.exe").start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.exit(1);
	}
	
	public void launchEngine() {
		System.out.println("launch the engine here");
	}
	
}
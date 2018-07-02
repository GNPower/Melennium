package launcher;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

public class LauncherUtils {
	
	private static final String STYLE_PATH = "res/styles/";

	public static void closeProgram(Stage window, Event e) {		
		e.consume();
		if(!ConfirmationBox.display("Close", "Are you sure you want to close the program?")) 
			return;
		
		System.out.println("closing me");
		window.close();
	}
	
	public static void unimplementedAlert() {
		AlertBox.display("Error", "That functionality is not implemented at this time.");
	}
	
	public static boolean validateInteger(TextField field) {
		try {
			int number = Integer.parseInt(field.getText());
			System.out.println("numbers be cool");
		}catch(NumberFormatException e) {
			System.out.println("thats no number my guy!");
			return false;
		}
		return true;
	}
	
	public static String getStyleURL(String name) {
		String[] nameData = name.split("\\.");
		if(nameData.length > 2) {
			System.out.println("Error, .css files can only have one extension");
		}
		String path = STYLE_PATH + nameData[0] + ".css";
		URL url = null;
		try {
			url = (new File(path)).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url.toString(); 
	}
	
	public static TreeItem<String> createBranch(String name, TreeItem<String> parent) {
		TreeItem<String> item = new TreeItem<String>(name);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}
}

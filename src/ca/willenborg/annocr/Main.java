package ca.willenborg.annocr;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
 
public class Main extends Application {
	
	private CharacterImage characterImage;
	
    public static void main(String[] args) {    	
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {   	
        primaryStage.setTitle("Artifical Neural Network OCR");
        
        Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("gui/main.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
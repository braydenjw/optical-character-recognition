package ca.willenborg.annocr.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import ca.willenborg.annocr.DocumentImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MainController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button binaryButton;
    @FXML private Button updateButton;
    @FXML private Button grayscaleButton;
    @FXML private ImageView previewImage;

    private DocumentImage docImage;
    
    @FXML
    void grayscaleButtonPressed(ActionEvent event) {
    	docImage.convertToGrayscale();
    	previewImage.setImage(docImage.image);
    }

    @FXML
    void binaryButtonPressed(ActionEvent event) {
    	docImage.convertToBinary();
    	previewImage.setImage(docImage.image);
    }

    @FXML
    void updateButtonPressed(ActionEvent event) {
    	docImage.lineSegment();
    }

    @FXML
    void initialize() {
        assert binaryButton != null : "fx:id=\"binaryButton\" was not injected: check your FXML file 'main.fxml'.";
        assert updateButton != null : "fx:id=\"updateButton\" was not injected: check your FXML file 'main.fxml'.";
        assert grayscaleButton != null : "fx:id=\"grayscaleButton\" was not injected: check your FXML file 'main.fxml'.";
        assert previewImage != null : "fx:id=\"previewImage\" was not injected: check your FXML file 'main.fxml'.";
        
        docImage = new DocumentImage("document.png");
        previewImage.setImage(docImage.image);
    }
}

package ca.willenborg.annocr.gui.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import ca.willenborg.annocr.DocumentImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button nextButton;
    @FXML private ImageView previewImage;

    private DocumentImage _docImage;
    private List<Image> _lineImages;
    private State _state;
    private int _lineIndex = 0;

    @FXML
    void nextButtonPressed(ActionEvent event) {
    	switch(_state) {
    		case GreyScale:
    			previewImage.setImage(_docImage.GenerateGreyscale());
    			_state = State.Binarize;
    			break;
    		case Binarize:
    			previewImage.setImage(_docImage.GenerateBinary());
    			_state = State.ViewLines;
    			break;
    		case ViewLines:
    			if (_lineIndex == 0) {
    				_lineImages = _docImage.GenerateLineImages();
    			}
    			if(_lineIndex == _lineImages.size()) {
    				_state = State.ReadCharacters;
    			} else {
    				previewImage.setImage(_lineImages.get(_lineIndex));
    			}
    			_lineIndex++;
    			break;
    		case ReadCharacters:
    			_docImage.GenerateCharacterImages();
    			_state = State.Complete;
    			break;
    		case Complete:
    			break;
    	}
    }

    @FXML
    void initialize() {
        assert nextButton != null : "fx:id=\"binaryButton\" was not injected: check your FXML file 'main.fxml'.";
        assert previewImage != null : "fx:id=\"previewImage\" was not injected: check your FXML file 'main.fxml'.";
        
        _docImage = new DocumentImage("document.png");
        _state = State.GreyScale;
        previewImage.setImage(_docImage.Image);
    }
    
    private enum State {
    	GreyScale,
    	Binarize,
    	ViewLines,
    	ReadCharacters,
    	Complete;
    };
}

package ca.willenborg.annocr.gui.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.Pair;

import ca.willenborg.annocr.DocumentImage;
import ca.willenborg.annocr.OCRController;
import ca.willenborg.annocr.Utilities;
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
    private State _state;
    private int _charIndex = 0;
    private OCRController _ocrController;

    @FXML
    void nextButtonPressed(ActionEvent event) {
    	switch(_state) {
    		case GreyScale:
    			previewImage.setImage(_docImage.GenerateGreyscale());
    			_state = State.Binarize;
    			break;
    		case Binarize:
    			previewImage.setImage(_docImage.GenerateBinary());
    			_state = State.ReadCharacters;
    			break;
    		case ReadCharacters:
    			previewImage.setImage(_docImage.CharacterImages.get(_charIndex++));
    			if(_charIndex == _docImage.CharacterImages.size() - 1) _state = State.Complete;
    			break;
    		case Complete:
    			for(Pair<Boolean[], Integer> pair : _docImage.BinaryCharacterImages) {
    				System.out.print(_ocrController.Ocr.Recognize(Utilities.BooleanObjectArrayToPrimitive(pair.left), pair.right) + " ");
    			}
    			break;
    	}
    }

    @FXML
    void initialize() {
        assert nextButton != null : "fx:id=\"binaryButton\" was not injected: check your FXML file 'main.fxml'.";
        assert previewImage != null : "fx:id=\"previewImage\" was not injected: check your FXML file 'main.fxml'.";
        
        _docImage = new DocumentImage("sample\\sample1.png");
        _state = State.GreyScale;
        previewImage.setImage(_docImage.Image);
        _ocrController = OCRController.getInstance();
    }
    
    private enum State {
    	GreyScale,
    	Binarize,
    	ReadCharacters,
    	Complete;
    };
}

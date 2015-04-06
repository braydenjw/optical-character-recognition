package ca.willenborg.annocr;

import ca.willenborg.annocr.ocr.OpticalCharacterRecognition;
import ca.willenborg.annocr.ocr.TrainOCR;

public class OCRController {
	
	public OpticalCharacterRecognition Ocr;
	
	private static OCRController instance = null;
	   
	/********************************************************************************
	 * Constructors
	********************************************************************************/
	   
	protected OCRController() {
		Ocr = TrainOCR.GetTrainedOCR();
	}
	   
	public static OCRController getInstance() {
		if(instance == null) {
			instance = new OCRController();
		}
		return instance;
	}
   
	/********************************************************************************
	 * Control Methods
	********************************************************************************/
	
}

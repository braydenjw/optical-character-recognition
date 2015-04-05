package ca.willenborg.annocr.tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.File;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang3.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.willenborg.annocr.OpticalCharacterRecognition;
import ca.willenborg.annocr.TrainOCR;
import ca.willenborg.annocr.Utilities;

public class TrainOCRTest {
	
	private Map<Integer, Pair<Integer, Boolean[]>> _trainingCharacters = new HashMap<Integer, Pair<Integer, Boolean[]>>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOCRTraining() {
		int correct = 0, incorrect = 0;
		OpticalCharacterRecognition ocr = TrainOCR.GetTrainedOCR();
		ocr.TrainNeuralNetwork();
		getTrainingCharacters();
		
		Iterator<Map.Entry<Integer, Pair<Integer, Boolean[]>>> entries = _trainingCharacters.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry<Integer, Pair<Integer, Boolean[]>> entry = entries.next();
		    char character = (char)((int) entry.getKey());
		    if(character == ocr.Recognize(TrainOCR.BooleanObjectArrayToPrimitive(entry.getValue().right), entry.getValue().left)) {
		    	correct++;
		    } else {
		    	incorrect++;
		    }
		}
		
		Assert.assertTrue("At least 90% accurate.", ((double) correct / (double) (correct + incorrect)) > 0.9);
	}
	
	private void getTrainingCharacters()
	{
		File dir = new File(System.getProperty("user.dir") + TrainOCR.trainingDir);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				Pair<Integer, Boolean[]> image = TrainOCR.BinaryImageFileToBooleanArray(child);	
				char character = '?';
				
				if(child.getName().length() == 6) {
					character = Character.toUpperCase(child.getName().charAt(1));
					_trainingCharacters.put((int) character, image);
				} else if (child.getName().length() == 5) {
					character = Character.toLowerCase(child.getName().charAt(0));
					_trainingCharacters.put((int) character, image);
				}
			}
		}
	}
}

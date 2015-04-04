package ca.willenborg.annocr.tests;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.willenborg.annocr.OpticalCharacterRecognition;
import ca.willenborg.annocr.TrainingCharacter;

public class OCRAddTest {

	private OpticalCharacterRecognition _ocr;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_ocr = new OpticalCharacterRecognition();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void TestAddCharacter() 
	{
		_ocr.Add('1', new boolean[] {false}, 1);
		
		List<TrainingCharacter> trainingCharacters = _ocr.GetTrainingCharacters();
		Assert.assertEquals(1, trainingCharacters.size());
		Assert.assertEquals('1', trainingCharacters.get(0).GetCharacter());
	}
	
	@Test
	public void TestAddDuplicateCharacter() 
	{
		_ocr.Add('1', new boolean[] {false}, 1);
		_ocr.Add('1', new boolean[] {true}, 1);
		
		List<TrainingCharacter> trainingCharacters = _ocr.GetTrainingCharacters();
		Assert.assertEquals(1, trainingCharacters.size());
		Assert.assertEquals('1', trainingCharacters.get(0).GetCharacter());
	}
	
	@Test
	public void TestAlphabeticalAddCharacter() 
	{	
		_ocr.Add('b', new boolean[] {false}, 1);
		_ocr.Add('a', new boolean[] {true}, 1);
		_ocr.Add('y', new boolean[] {true}, 1);
		_ocr.Add('x', new boolean[] {true}, 1);
		_ocr.Add('z', new boolean[] {true}, 1);
		_ocr.Add('c', new boolean[] {false}, 1);
		
		List<TrainingCharacter> trainingCharacters = _ocr.GetTrainingCharacters();
		Assert.assertEquals(6, trainingCharacters.size());
		Assert.assertEquals('a', trainingCharacters.get(0).GetCharacter());
		Assert.assertEquals('b', trainingCharacters.get(1).GetCharacter());
		Assert.assertEquals('c', trainingCharacters.get(2).GetCharacter());
		Assert.assertEquals('x', trainingCharacters.get(3).GetCharacter());
		Assert.assertEquals('y', trainingCharacters.get(4).GetCharacter());
		Assert.assertEquals('z', trainingCharacters.get(5).GetCharacter());
	}

}

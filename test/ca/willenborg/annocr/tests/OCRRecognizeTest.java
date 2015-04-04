package ca.willenborg.annocr.tests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.willenborg.annocr.OpticalCharacterRecognition;

public class OCRRecognizeTest {

	private OpticalCharacterRecognition _ocr;
	
	private boolean[] _charPattern1 = new boolean[] {
			true, 	false, 	false, 	false,	false,
			false, 	true, 	false, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	false, 	true,	false,
			false, 	false, 	false, 	false,	true,
			false, 	false, 	false, 	true,	false,
			false, 	false, 	true, 	false,	false};
	
	private boolean[] _charPattern1Alt = new boolean[] {
			true, 	false, 	false, 	false,	false,
			false, 	true, 	false, 	false,	false,
			false, 	false, 	true, 	true,	false,
			false, 	false, 	false, 	false,	false,
			false, 	false, 	false, 	false,	true,
			false, 	false, 	false, 	true,	false,
			false, 	false, 	true, 	false,	false};
	
	private boolean[] _charPattern2 = new boolean[] {
			false, 	false, 	false, 	false,	true,
			false, 	false, 	false, 	true,	false,
			false, 	false, 	true, 	false,	false,
			false, 	true, 	false, 	false,	false,
			true, 	false, 	false, 	false,	false,
			false, 	true, 	false, 	false,	false,
			false, 	false, 	true, 	false,	false};
	
	private boolean[] _charPattern2Alt = new boolean[] {
			false, 	false, 	false, 	false,	true,
			false, 	false, 	false, 	true,	false,
			false, 	false, 	true, 	false,	false,
			false, 	true, 	false, 	false,	false,
			true, 	false, 	false, 	false,	false,
			false, 	true, 	false, 	false,	false,
			false, 	true, 	false, 	false,	false};
	
	private boolean[] _charPattern3 = new boolean[] {
			true,	true,	true,	true,	true,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false};
	
	private boolean[] _charPattern3Alt = new boolean[] {
			true,	false,	true,	true,	true,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	false, 	false,	false};
	
	private boolean[] _charPattern4 = new boolean[] {
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			true,	true,	true,	true,	true};
	
	private boolean[] _charPattern4Alt = new boolean[] {
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	false,
			false, 	false, 	true, 	false,	true,
			true,	true,	true,	true,	true};
	
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
	public void TestRecognize() {
		_ocr.Add('1', _charPattern1, 5);
		_ocr.Add('2', _charPattern2, 5);
		_ocr.Add('3', _charPattern3, 5);
		_ocr.Add('4', _charPattern4, 5);
		
		_ocr.TrainNeuralNetwork();
		
		Assert.assertEquals('1', _ocr.Recognize(_charPattern1Alt, 5));
		Assert.assertEquals('2', _ocr.Recognize(_charPattern2Alt, 5));
		Assert.assertEquals('3', _ocr.Recognize(_charPattern3Alt, 5));
		Assert.assertEquals('4', _ocr.Recognize(_charPattern4Alt, 5));
	}

}

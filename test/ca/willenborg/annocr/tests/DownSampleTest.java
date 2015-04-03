package ca.willenborg.annocr.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.binding.BindingHelperObserver;

import ca.willenborg.annocr.CharacterBounds;
import ca.willenborg.annocr.DocumentImage;
import ca.willenborg.annocr.LabeledBinaryImage;

public class DownSampleTest
{
	private LabeledBinaryImage _labeledBinaryImageBlank;
	private LabeledBinaryImage _labeledBinaryImage10by5a;
	private LabeledBinaryImage _labeledBinaryImage10by5b;
	private LabeledBinaryImage _labeledBinaryImage10by10a;
	private LabeledBinaryImage _labeledBinaryImage10by10b;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// Create blank labeled binary image
		_labeledBinaryImageBlank = new LabeledBinaryImage(10, 10);
		
		boolean[] binaryImage10by5a = new boolean[] {
				false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	true,
				false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	true, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	true};
		_labeledBinaryImage10by5a = new LabeledBinaryImage(binaryImage10by5a, 10, 5);
		
		boolean[] binaryImage10by5b = new boolean[] {
				false, 	false, 	false, 	true, 	true, 	true, 	true, 	false, 	false, 	false,
				false, 	false, 	false, 	true, 	true, 	true, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				true, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false};
		_labeledBinaryImage10by5b = new LabeledBinaryImage(binaryImage10by5b, 10, 5);
		
		boolean[] binaryImage10by10a = new boolean[] {
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	true,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false};
		_labeledBinaryImage10by10a = new LabeledBinaryImage(binaryImage10by10a, 10, 10);
		
		boolean[] binaryImage10by10b = new boolean[] {
				false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	true, 	false, 	false, 	false, 	false, 	true,
				true, 	true, 	false, 	false, 	true, 	false, 	false, 	false, 	false, 	true,
				true, 	true, 	false, 	false, 	false, 	true, 	false, 	false, 	true, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	true, 	true, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	true, 	true, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false};
		_labeledBinaryImage10by10b = new LabeledBinaryImage(binaryImage10by10b, 10, 10);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDownSample1() 
	{	
		DocumentImage docImage = new DocumentImage(_labeledBinaryImage10by5a, 10, 5);
		
		boolean[] expectedResult = new boolean[] {
				true,	false,	true,
				false,	true,	true};
		boolean[] actualResult = docImage.DownSample(new CharacterBounds(0, 0, 4, 9), 3, 2);
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test
	public void testDownSample2() 
	{
		DocumentImage docImage = new DocumentImage(_labeledBinaryImage10by5b, 10, 5);
		
		boolean[] expectedResult = new boolean[] {
				false,	true,	true,
				true,	false,	false};
		boolean[] actualResult = docImage.DownSample(new CharacterBounds(0, 0, 4, 9), 3, 2);
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test
	public void testDownSample3() {
		DocumentImage docImage = new DocumentImage(_labeledBinaryImage10by10a, 10, 10);
		
		boolean[] expectedResult = new boolean[] {
				false,	true,
				true,	false};
		boolean[] actualResult = docImage.DownSample(new CharacterBounds(0, 0, 9, 9), 2, 2);
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test
	public void testDownSample4() {
		DocumentImage docImage = new DocumentImage(_labeledBinaryImage10by10b, 10, 10);
		
		boolean[] expectedResult = new boolean[] {
				true,	false,	true,	false,	true,
				true,	false,	true,	false,	true,
				false,	false,	false,	false,	false,
				false,	false,	false,	true,	true,
				false,	false,	false,	true,	true};
		boolean[] actualResult = docImage.DownSample(new CharacterBounds(0, 0, 9, 9), 5, 5);
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}

}

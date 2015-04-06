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
import ca.willenborg.annocr.ocr.OpticalCharacterRecognition;

public class DownSampleTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDownSample1() 
	{
		boolean[] binaryImage10by5a = new boolean[] {
				false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	true,
				false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	true, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	true};
		boolean[] expectedResult = new boolean[] {
				true,	false,	true,
				false,	true,	true};
		boolean[] actualResult = OpticalCharacterRecognition.DownSample(binaryImage10by5a, 10, 3, 2);
		
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test
	public void testDownSample2() 
	{
		boolean[] binaryImage10by5b = new boolean[] {
				false, 	false, 	false, 	true, 	true, 	true, 	true, 	false, 	false, 	false,
				false, 	false, 	false, 	true, 	true, 	true, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false,
				true, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false};
		boolean[] expectedResult = new boolean[] {
				false,	true,	true,
				true,	false,	false};
		boolean[] actualResult = OpticalCharacterRecognition.DownSample(binaryImage10by5b, 10, 3, 2);
		
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test
	public void testDownSample3() 
	{
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
		boolean[] expectedResult = new boolean[] {
				false,	true,
				true,	false};
		boolean[] actualResult = OpticalCharacterRecognition.DownSample(binaryImage10by10a, 10, 2, 2);
		
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test
	public void testDownSample4() 
	{
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
		boolean[] expectedResult = new boolean[] {
				true,	false,	true,	false,	true,
				true,	false,	true,	false,	true,
				false,	false,	false,	false,	false,
				false,	false,	false,	true,	true,
				false,	false,	false,	true,	true};
		boolean[] actualResult = OpticalCharacterRecognition.DownSample(binaryImage10by10b, 10, 5, 5);
		
		Assert.assertTrue(Arrays.equals(expectedResult, actualResult));
	}

}

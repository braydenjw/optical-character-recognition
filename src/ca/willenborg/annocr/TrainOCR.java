package ca.willenborg.annocr;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.commons.lang3.Pair;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class TrainOCR 
{
	public static final String trainingDir = "\\bin\\training";
	
	public static OpticalCharacterRecognition GetTrainedOCR()
	{
		OpticalCharacterRecognition ocr = new OpticalCharacterRecognition();
		
		File dir = new File(System.getProperty("user.dir") + trainingDir);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				Pair<Integer, Boolean[]> image = BinaryImageFileToBooleanArray(child);	
				char character = '?';
				
				if(child.getName().length() == 6) {
					character = Character.toUpperCase(child.getName().charAt(1));
					ocr.Add(character, BooleanObjectArrayToPrimitive(image.right), (int) image.left);
				} else if (child.getName().length() == 5) {
					character = Character.toLowerCase(child.getName().charAt(0));
					ocr.Add(character, BooleanObjectArrayToPrimitive(image.right), (int) image.left);
				}
				
				System.out.println("Character: " + character + " was added to the neural network training queue.");
			}
		} else {
			System.out.println("Training directory not found.");
		}
		
		return ocr;
	}
	
	public static Pair<Integer, Boolean[]> BinaryImageFileToBooleanArray(File file)
	{
		try {
			InputStream iStream = new FileInputStream(file);	
			Image image = new Image(iStream);
			int width = (int) image.getWidth();
			int height = (int) image.getHeight();
			Boolean[] booleanImage = new Boolean[width * height];
			PixelReader pixelReader = image.getPixelReader();
			
			for(Point point = new Point(0, 0); point.y < height; point.y++) {
				for(point.x = 0; point.x < width; point.x++) {
					booleanImage[point.y * width + point.x] = pixelReader.getColor(point.x, point.y).equals(Color.BLACK) ? true : false;
				}
			}
			
			return new Pair<Integer, Boolean[]>(width, booleanImage);
		} catch (FileNotFoundException e) {
			System.out.println("Training image seems to be invalid.");
			return null;
		}
		
	}
	
	public static boolean[] BooleanObjectArrayToPrimitive(Boolean[] original)
	{
		boolean[] primitiveArray = new boolean[original.length];
		
		for(int i = 0; i < original.length; i++) {
			primitiveArray[i] = original[i].booleanValue();
		}
		
		return primitiveArray;
	}

}

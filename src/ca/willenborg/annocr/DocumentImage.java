package ca.willenborg.annocr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class DocumentImage {
	
	private static final double BRIGHTNESS_THRESHOLD = 0.5;
	private static final int MIN_LINE_HEIGHT = 5;
	private static final int MIN_LINE_SPACE_HEIGHT = 3;
	
	public Image Image;
	public boolean[][] BinaryImage;
	
	private int[] _histogram;
	private int _width;
	private int _height;
	
	public DocumentImage(String imageUrl) 
	{
		Image = new Image(imageUrl);
		
		if (Image != null) {
			_width = (int) Image.getWidth();
			_height = (int) Image.getHeight();
		}
	}
	
	public Image GenerateGreyscale(PixelReader colourPixelReader) 
	{
		WritableImage greyscaleImage = new WritableImage(_width, _height);
		PixelWriter pixelWriter = greyscaleImage.getPixelWriter();
		Color colour;
		
		for (int x = 0; x < _width; x++) {
			for (int y = 0; y < _height; y++) {
				colour = colourPixelReader.getColor(x, y);
				pixelWriter.setColor(x, y, colour.grayscale());
			}
		}
		
		return greyscaleImage;
	}
	
	private boolean[][] GenerateBinary(PixelReader greyscalePixelReader) 
	{
		boolean[][] binary = new boolean[_height][_width];
		
		for (int x = 0; x < _width; x++) {
			for (int y = 0; y < _height; y++) {
				double brightness = greyscalePixelReader.getColor(x, y).getBrightness();
				if (brightness < BRIGHTNESS_THRESHOLD) {
					binary[y][x] = true;
				} else {
					binary[y][x] = false;
				}
			}
		}
		
		return binary;
	}
	
	/**
	 * Desc:	Generates a histogram of number of true values in each row of 2D boolean array
	 * Params:	2D boolean array
	 * Return:	Int array containing number of true values in each row of the 2D boolean array given.
	 * SideFX:	None.
	 */
	private int[] GenerateHistogram(boolean[][] binary)
	{
		int[] histogram = new int[_height];
		
		for (int y = 0; y < _height; y++) {
			histogram[y] = 0;
			for (int x = 0; x < _width; x++) {
				if (binary[y][x] == true) {
					histogram[y]++;
				}
			}
		}
		
		return histogram;
	}
	
	private List<boolean[][]> GenerateLineSegments(int[] histogram, boolean[][] binaryImage) {
		List<boolean[][]> lines = new ArrayList<boolean[][]>();
		int firstNonZero = -1;
		int blankLineCount = 0;
		
		for(int i = 0; i < histogram.length; i++) {
			if(firstNonZero == -1) {
				if(histogram[i] > 0) {
					firstNonZero = i;
				}
			} else {
				if ((histogram[i] == 0) || (i == histogram.length - 1)) {
					if(blankLineCount < MIN_LINE_SPACE_HEIGHT) {
						blankLineCount++;
					} else {
						boolean[][] line = new boolean[i - firstNonZero - MIN_LINE_SPACE_HEIGHT][_width];
						for(int j = firstNonZero; j < i - MIN_LINE_SPACE_HEIGHT; j++) {
							line[j - firstNonZero] = binaryImage[j].clone();
						}
						lines.add(line);
						
						firstNonZero = -1;
					}
				} else {
					blankLineCount = 0;
				}
			}	
		}
		
		return lines;
	}
	
	public List<Image> GenerateCharacterImages()
	{
		Image greyscaleImage = this.GenerateGreyscale(Image.getPixelReader());
		boolean[][] binaryImage = this.GenerateBinary(greyscaleImage.getPixelReader());
		int[] histogram = this.GenerateHistogram(binaryImage);
		List<boolean[][]> lineImages = this.GenerateLineSegments(histogram, binaryImage);
		List<Image> imageList = new ArrayList<Image>();
		
		System.out.println("histogram = " + ArrayUtils.toString(histogram));
		
		for(boolean[][] line : lineImages) {
			int width = line[0].length;
			int height = line.length;
			
			WritableImage writImage = new WritableImage(width, height);
			PixelWriter pixelWriter = writImage.getPixelWriter();
			
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					if(line[y][x] == true) {
						pixelWriter.setColor(x, y, Color.BLACK);
					} else {
						pixelWriter.setColor(x, y, Color.WHITE);
					}
				}
			}
			
			imageList.add((Image) writImage);
		}
		
		return imageList;
	}
	
	
}

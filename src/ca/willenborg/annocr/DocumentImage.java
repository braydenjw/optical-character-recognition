package ca.willenborg.annocr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class DocumentImage {
	
	private /*static final*/ double BRIGHTNESS_THRESHOLD = 0.8;
	private /*static final*/ int MIN_LINE_HEIGHT = 5;
	private /*static final*/ int MIN_LINE_SPACE_HEIGHT = 2;
	
	public Image Image;
	public boolean[][] BinaryImage;
	
	private Image _greyscaleImage;
	private boolean[][] _binaryImage;
	private int[] _histogram;
	private int _width;
	private int _height;
	
	/********************************************************************************
	 * Constructors & Destructors
	 ********************************************************************************/
	
	public DocumentImage(String imageUrl) 
	{
		Image = new Image(imageUrl);
		
		if (Image != null) {
			_width = (int) Image.getWidth();
			_height = (int) Image.getHeight();
		}
	}
	
	/********************************************************************************
	 * Control Methods
	 ********************************************************************************/
	
	public Image GenerateGreyscale() 
	{
		_greyscaleImage = GenerateGreyscale(Image.getPixelReader());
		return _greyscaleImage;
	}
	
	public Image GenerateBinary()
	{
		if(_greyscaleImage == null) GenerateGreyscale();
		_binaryImage = GenerateBinary(_greyscaleImage.getPixelReader());
		return Boolean2dToImage(_binaryImage);
	}
	
	/********************************************************************************
	 * Helper Methods
	 ********************************************************************************/
	
	private Image GenerateGreyscale(PixelReader colourPixelReader) 
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
	
	private Image Boolean2dToImage(boolean[][] line)
	{
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
		
		return (Image) writImage;
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
	
	private List<Pair<Integer, Integer>> GenerateLineLocations(int[] histogram, int minLineSpaceHeight, int startLocation, int length)
	{
		List<Pair<Integer, Integer>> lineLocations = new ArrayList<Pair<Integer, Integer>>();
		if (startLocation < 0) startLocation = 0;
		if (length < 0) length = histogram.length;
		
		int firstNonZero = -1;
		int blankLineCount = 0;
		
		for(int i = startLocation; (i < histogram.length) && (i < length + startLocation); i++) {
			if(firstNonZero == -1) {
				if(histogram[i] > 0) {
					firstNonZero = i;
				}
			} else {
				if((i == histogram.length - 1) || (i == length + startLocation - 1)) {
					if(i > firstNonZero) {
						lineLocations.add(new Pair<Integer, Integer>(firstNonZero, i - firstNonZero));	
					}
				} else if (histogram[i] == 0) {
					if(blankLineCount < minLineSpaceHeight) {
						blankLineCount++;
					} else {
						lineLocations.add(new Pair<Integer, Integer>(firstNonZero, i - firstNonZero - minLineSpaceHeight));						
						firstNonZero = -1;
					}
				} else {
					blankLineCount = 0;
				}
			}	
		}
		
		return lineLocations;
	}
	
	private List<boolean[][]> GenerateLinesFromLocations(boolean[][] orig, List<Pair<Integer, Integer>> lineLocations)
	{
		List<boolean[][]> lines = new ArrayList<boolean[][]>();
		
		for(Pair<Integer, Integer> lineLocation : lineLocations) {
			boolean[][] line = new boolean[lineLocation.getValue()][_width];
			for(int i = 0; i < lineLocation.getValue(); i++) {
				line[i] = orig[lineLocation.getKey() + i].clone();
			}
			lines.add(line);
		}
		
		return lines;		
	}
	
	private List<boolean[][]> GenerateLineSegments(int[] histogram, boolean[][] binaryImage) {
		List<Pair<Integer, Integer>> lineLocations = new ArrayList<Pair<Integer, Integer>>();
		List<boolean[][]> lines = new ArrayList<boolean[][]>();
		
		// Make the minimum height of spaces to be a percentile of the size of all spaces
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int contiguous = 0;
		for(int i : histogram) {
			if(i == 0) {
				contiguous++;
			} else if (contiguous > 0){
				stats.addValue(contiguous);
				contiguous = 0;
			}
		}
		int minLineSpaceHeight = (int) stats.getPercentile(20);
		
		lineLocations = GenerateLineLocations(histogram, minLineSpaceHeight, -1, -1);
		
		// Make the minimum height of spaces to be a percentile of the size of all spaces
		stats.clear();
		for(Pair<Integer, Integer> lineLocation : lineLocations) {
			stats.addValue(lineLocation.getValue());
		}
		int maxLineHeight = (int) stats.getPercentile(60);
		
		for(int i = minLineSpaceHeight - 1; i > 0; i--) {
			for(int j = 0; j < lineLocations.size(); j++) {
				if(lineLocations.get(j).getValue() > maxLineHeight) {
					List<Pair<Integer, Integer>> subLineLocations = GenerateLineLocations(histogram, i, lineLocations.get(j).getKey(), lineLocations.get(j).getValue());
					if(subLineLocations.size() > 1) {
						for(int k = 0; k < subLineLocations.size(); k++) {
							lineLocations.add(k + j + 1, subLineLocations.get(k));
						}
						lineLocations.remove(j);
						j += subLineLocations.size() - 1;
					}
				}
			}
		}
		
		lines = GenerateLinesFromLocations(binaryImage, lineLocations);
		
		return lines;
	}
	
	public List<Image> GenerateCharacterImages()
	{
		List<Image> imageList = new ArrayList<Image>();
		int[] histogram = this.GenerateHistogram(_binaryImage);
		
		for(boolean[][] line : GenerateLineSegments(histogram, _binaryImage)) {
			imageList.add(Boolean2dToImage(line));
		}
		
		return imageList;
	}
	
	
}

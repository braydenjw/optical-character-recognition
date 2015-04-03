package ca.willenborg.annocr;

import java.awt.Point;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class DocumentImage {
	
	private static final double BRIGHTNESS_THRESHOLD = 0.8;
	private int MIN_LINE_HEIGHT = 5;
	private int MIN_LINE_SPACE_HEIGHT = 2;
	
	public Image Image;
	public List<Image> CharacterImages;
	
	private Image _greyscaleImage;
	private LabeledBinaryImage _labeledBinaryImage;
	private List<boolean[][]> lineBinaryImages;
	private int _width, _height;
	
	/********************************************************************************
	 * Constructors
	 ********************************************************************************/
	
	public DocumentImage(LabeledBinaryImage lbi, final int width, final int height) 
	{
		_labeledBinaryImage = lbi;
		_width = width;
		_height = height;
	}
	
	public DocumentImage(String imageUrl) 
	{
		Image = new Image(imageUrl);
		_height = (int) Image.getHeight();
		_width = (int) Image.getWidth();
	}
	
	/********************************************************************************
	 * Control Methods
	 ********************************************************************************/
	
	public Image GenerateGreyscale() 
	{
		_greyscaleImage = GenerateGreyscale(Image.getPixelReader(), (int) Image.getWidth(), (int) Image.getHeight());
		return _greyscaleImage;
	}
	
	public Image GenerateBinary()
	{
		CharacterImages = new ArrayList<Image>();
		
		if(_greyscaleImage == null) GenerateGreyscale();
		boolean[] binaryImage = GenerateBinary(_greyscaleImage.getPixelReader(), (int)_greyscaleImage.getWidth(), (int)_greyscaleImage.getHeight());
		_labeledBinaryImage = new LabeledBinaryImage(binaryImage, _width, _height);
		TwoPassConnectedComponent(_labeledBinaryImage);
		Map<Integer, CharacterBounds> charBounds = _labeledBinaryImage.FindLabelBounds();
		for (CharacterBounds charImage : charBounds.values()) {
		    CharacterImages.add(_labeledBinaryImage.GenerateImage(charImage));
		}
		return _labeledBinaryImage.GetImage();
	}
	
//	public List<Image> GenerateLineImages()
//	{
//		List<Image> imageList = new ArrayList<Image>();
//		int[] histogram = this.GenerateHistogram(_binaryImage, false);
//		
//		lineBinaryImages = GenerateLineSegments(histogram, _binaryImage);
//		
//		for(boolean[][] line : lineBinaryImages) {
//			imageList.add(Boolean2dToImage(line));
//		}
//		
//		return imageList;
//	}
//	
//	public List<CharacterImage> GenerateCharacterImages() {
//		List<CharacterImage> characterImages = new ArrayList<CharacterImage>();
//		
//		int[] histogram = this.GenerateHistogram(lineBinaryImages.get(0), true);
//		System.out.println(ArrayUtils.toString(histogram));
//		
//		return characterImages;
//	}
	
	private static void TwoPassConnectedComponent(LabeledBinaryImage document)
	{
		List<Pair<Integer, Integer>> equivalenceSet = new ArrayList<Pair<Integer,Integer>>();
		int currentLabel = 1;
		
		// First pass
		for(int y = 0; y < document.GetHeight(); y++) {
			for(int x = 0; x < document.GetWidth(); x++) {
				Point point = new Point(x, y);
				
				// If the pixel is background, skip
				if(document.GetPixel(point) == false) continue;
					
				// Else, label the pixel with the lowest neighbouring pixel or a new label
				// If there are 2 neighbours with different values, add them to the equivalence set
				List<Integer> neighbours = document.GetNeighbouringLabels(point);
				Set<Integer> equivalenceList = new HashSet<Integer>();
				
				if(neighbours.isEmpty()) {
					document.SetLabel(point, currentLabel++);
				} else {
					int min = neighbours.get(0);
					equivalenceList.add(min);
					for(int i = 1; i < neighbours.size(); i++) {
						equivalenceList.add(neighbours.get(i));
						min = Math.min(min, neighbours.get(i));
					}
					document.SetLabel(point, min);					
					
					if(point.x == 7 && point.y == 2) {
						System.out.println("here");
					}
					
					// Add and update all equivalences
					equivalenceList.remove((Integer) min);
					for(int equivalence : equivalenceList) {
						boolean found = false;
						for(int i = 0; i < equivalenceSet.size(); i++) {
							if(equivalenceSet.get(i).getKey() == equivalence) {
								equivalenceSet.set(i, new Pair<Integer, Integer>(equivalence, min));
								found = true;
								break;
							}
						}
						if (found == false) {
							equivalenceSet.add(new Pair<Integer, Integer>(equivalence, min));
						}
					}
				}
			}
		}
		
		// Second pass
		for(int y = 0; y < document.GetHeight(); y++) {
			for(int x = 0; x < document.GetWidth(); x++) {
				Point point = new Point(x, y);
				
				// If the pixel is background, skip
				if(document.GetPixel(point) == false) continue;
				
				for(Pair<Integer, Integer> pair : equivalenceSet) {
					if(document.GetLabel(point) == pair.getKey()) {
						document.SetLabel(point, pair.getValue());
					}
				}
			}
		}
	}
	
	/**
	 * Called to downsample the image and store it in the down sample component.
	 */
	public boolean[] DownSample(CharacterBounds characterBounds, final int dsWidth, final int dsHeight)
	{
		boolean[] downSample = new boolean[dsWidth * dsHeight];
		int top = 0, left = 0, bottom = 0, right = 0;
		int xRegionSize = characterBounds.GetWidth() / dsWidth;
		int yRegionSize = characterBounds.GetHeight() / dsHeight;
		boolean xRegionRemainder = characterBounds.GetWidth() % dsWidth != 0;
		boolean yRegionRemainder = characterBounds.GetHeight() % dsHeight != 0;
		
		for(int y = 0; y < dsHeight; y++) {
			bottom = (y == dsHeight - 1) && (yRegionRemainder == true) ? top + yRegionSize : top + yRegionSize - 1;
			for(int x = 0; x < dsWidth; x++) {
				right = (x == dsWidth - 1) && (xRegionRemainder == true) ? left + xRegionSize : left + xRegionSize - 1;
				downSample[y * dsWidth + x] = DownSampleHelper(top, left, bottom, right);
				left = right + 1;
			}
			left = right = 0;
			top = bottom + 1;
		}
		
		return downSample;
	}

	private boolean DownSampleHelper(int top, int left, int bottom, int right)
	{
		for(int y = top; y <= bottom; y++) {
			for(int x = left; x <= right; x++) {
				if(_labeledBinaryImage.GetPixel(new Point(x, y)) == true) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/********************************************************************************
	 * Helper Methods
	 ********************************************************************************/
	
	private static Image GenerateGreyscale(PixelReader colourPixelReader, int width, int height) 
	{
		WritableImage greyscaleImage = new WritableImage(width, height);
		PixelWriter pixelWriter = greyscaleImage.getPixelWriter();
		Color colour;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				colour = colourPixelReader.getColor(x, y);
				pixelWriter.setColor(x, y, colour.grayscale());
			}
		}
		
		return greyscaleImage;
	}
	
	private static boolean[] GenerateBinary(PixelReader greyscalePixelReader, int width, int height) 
	{
		boolean[] binary = new boolean[height * width];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double brightness = greyscalePixelReader.getColor(x, y).getBrightness();
				if (brightness < BRIGHTNESS_THRESHOLD) {
					binary[y * width + x] = true;
				} else {
					binary[y * width + x] = false;
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
	private int[] GenerateHistogram(boolean[][] binary, boolean column)
	{
		int width, height;
		int[] histogram;
		
		if (column == true) {
			width = binary[0].length;
			height = binary.length;
			histogram = new int[width];
		} else {
			width = binary.length;
			height = binary[0].length;
			histogram = new int[height];
		}
		
		for (int y = 0; y < width; y++) {
			histogram[y] = 0;
			for (int x = 0; x < height; x++) {
				if (column == true) {
					if (binary[x][y] == true) {
						histogram[y]++;
					}
				} else {
					if (binary[y][x] == true) {
						histogram[y]++;
					}
				}
			}
		}
		
		return histogram;
	}
	
	private static List<Pair<Integer, Integer>> GenerateLineLocations(int[] histogram, int minLineSpaceHeight, int startLocation, int length)
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
	
	private static List<boolean[][]> GenerateLinesFromLocations(boolean[][] orig, List<Pair<Integer, Integer>> lineLocations)
	{
		List<boolean[][]> lines = new ArrayList<boolean[][]>();
		
		for(Pair<Integer, Integer> lineLocation : lineLocations) {
			boolean[][] line = new boolean[lineLocation.getValue()][orig.length];
			for(int i = 0; i < lineLocation.getValue(); i++) {
				line[i] = orig[lineLocation.getKey() + i].clone();
			}
			lines.add(line);
		}
		
		return lines;		
	}
	
	private static List<boolean[][]> GenerateLineSegments(int[] histogram, boolean[][] binaryImage) {
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
	
	private static List<Pair<Integer, Integer>> GenerateCharacterLocations(int[] histogram)
	{
		List<Pair<Integer, Integer>> characterLocations = new ArrayList<Pair<Integer, Integer>>();
		int firstNonZero = -1;
		
		for(int i = 0; i < histogram.length; i++) {
			if(firstNonZero == -1) {
				if(histogram[i] > 0) {
					firstNonZero = i;
				}
			} else {
				if(i == histogram.length - 1) {
					if(i > firstNonZero) {
						characterLocations.add(new Pair<Integer, Integer>(firstNonZero, i - firstNonZero));	
					}
				} else if (histogram[i] == 0) {
					characterLocations.add(new Pair<Integer, Integer>(firstNonZero, i - firstNonZero));						
					firstNonZero = -1;
				}
			}	
		}
		
		return characterLocations;
	}
	
}

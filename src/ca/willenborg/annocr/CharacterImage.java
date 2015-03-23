package ca.willenborg.annocr;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class CharacterImage {
	private static final double BRIGHTNESS_THRESHOLD = 0.5;
	
	private boolean[][] _binaryImage;

	public CharacterImage(boolean[][] binaryImage) {
		_binaryImage = binaryImage;
	}
	
	
	/**
	 * Desc:	Finds the bounding box of image (first position of character darker than BRIGHTNESS_THRESHOLD).
	 * Params:	None.
	 * Return:	Int array of size 4. Top bound, right bound, bottom bound, left bound.
	 * SideFX:	None.
	 *
	public int[] boundingBox() {
		int top = -1;
		int right = -1;
		int bottom = -1;
		int left = -1;
		
		int width = (int)image.getWidth();
	    int height = (int)image.getHeight();
	    
		PixelReader pixelReader = image.getPixelReader();
		
		// Iterate through every row starting at the top row
		// For each row, iterate through every column
		// Stop when top != -1
		for (int y = 0; y < height && top == -1; y++) {
			for (int x = 0; x < width && top == -1; x++) {
				Color colour = pixelReader.getColor(x,y);
				if ((colour != null) && (colour.getBrightness() < BRIGHTNESS_THRESHOLD)) {
					top = y;
				}
			}
		}
		
		// Iterate through every column starting at the right column
		// For each row, iterate through every column
		// Stop when right != -1
		for (int x = 0; x < width && right == -1; x++) {
			for (int y = 0; y < height && right == -1; y++) {
				Color colour = pixelReader.getColor(x,y);
				if ((colour != null) && (colour.getBrightness() < BRIGHTNESS_THRESHOLD)) {
					right = x;
				}
			}
		}
		
		// Iterate through every row starting at the bottom row
		// For each row, iterate through every column
		// Stop when bottom != -1
		for (int y = height - 1; y >= 0 && bottom == -1; y--) {
			for (int x = 0; x < width && bottom == -1; x++) {
				Color colour = pixelReader.getColor(x,y);
				if ((colour != null) && (colour.getBrightness() < BRIGHTNESS_THRESHOLD)) {
					bottom = y;
				}
			}
		}
		
		// Iterate through every column starting at the left column
		// For each column, iterate through every row
		// Stop when left != -1
		for (int x = width - 1; x >= 0 && left == -1; x--) {
			for (int y = 0; y < height && left == -1; y++) {
				Color colour = pixelReader.getColor(x,y);
				if ((colour != null) && (colour.getBrightness() < BRIGHTNESS_THRESHOLD)) {
					left = x;
				}
			}
		}
		
		int ret[] = {top, right, bottom, left};
		return ret;
	}*/
}

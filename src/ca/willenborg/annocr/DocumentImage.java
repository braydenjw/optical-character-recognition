package ca.willenborg.annocr;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class DocumentImage {
	
	private static final double BRIGHTNESS_THRESHOLD = 0.5;
	private static final int MIN_LINE_HEIGHT = 5;
	
	public Image image;
	
	private int width;
	private int height;
	
	public DocumentImage(String imageUrl) {
		image = new Image(imageUrl);
		
		if (image != null) {
			width = (int) image.getWidth();
			height = (int) image.getHeight();
		}
	}
	
	public void convertToGrayscale () {
		PixelReader pixelReader = image.getPixelReader();
		WritableImage modifiedImage = new WritableImage(width, height);
		PixelWriter pixelWriter = modifiedImage.getPixelWriter();
		Color colour;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				colour = pixelReader.getColor(x, y);
				pixelWriter.setColor(x, y, colour.grayscale());
			}
		}
		
		image = (Image) modifiedImage;
	}
	
	public void convertToBinary() {
		PixelReader pixelReader = image.getPixelReader();
		WritableImage modifiedImage = new WritableImage(width, height);
		PixelWriter pixelWriter = modifiedImage.getPixelWriter();
		Color colour;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double brightness = pixelReader.getColor(x, y).getBrightness();
				if (brightness < BRIGHTNESS_THRESHOLD) {
					colour = Color.BLACK;
				} else {
					colour = Color.WHITE;
				}
				pixelWriter.setColor(x, y, colour);
			}
		}
		
		image = (Image) modifiedImage;
	}
	
	public void lineSegment() {
		PixelReader pixelReader = image.getPixelReader();
		int[] histogram = new int[height];
		int lineHeight;
		List<Image> lines;
		
		for (int y = 0; y < height; y++) {
			histogram[y] = 0;
			for (int x = 0; x < width; x++) {
				if (pixelReader.getColor(x, y).hashCode() == 255) {
					histogram[y]++;
				}
			}
		}
		
		for (int i = 0; i < height; i++) {
			lineHeight = 0;
			
			while (histogram[i] > 0) {
				lineHeight++;
				i++;
			}
			
			if (lineHeight > MIN_LINE_HEIGHT) {
				WritableImage line = new WritableImage(width, lineHeight);
				PixelWriter lineWriter = line.getPixelWriter();
				int offset = i - lineHeight;
				
				for (int j = i - lineHeight; j <= i; j++) {
					for (int k = 0; k < width; k++) {
						//lineWriter.setColor(k, i - lineHeight - offset , c);
					}
				}
			}
			
		}
	}
	
	
}

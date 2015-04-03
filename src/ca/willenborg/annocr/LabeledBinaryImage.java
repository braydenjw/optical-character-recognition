package ca.willenborg.annocr;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class LabeledBinaryImage extends Object {

	private boolean[]	_binaryImage;
	private int[]		_binaryImageLabels;
	private int _width;
	private int _height;
	
	/************************************************************
	 * Constructors
	*************************************************************/
	
	public LabeledBinaryImage(int width, int height) 
	{
		_width = width;
		_height = height;
		
		_binaryImage = new boolean[_width * _height];
		_binaryImageLabels = new int[_binaryImage.length];
		
		for(int i = 0; i < _binaryImage.length; i++) {
			_binaryImage[i] = false;
			_binaryImageLabels[i] = 0;
		}
	}
	
	public LabeledBinaryImage(boolean[] image, int width, int height) 
	{
		_binaryImage = image;
		_binaryImageLabels = new int[_binaryImage.length];
		_width = width;
		_height = height;
		
		for(int i = 0; i < _binaryImage.length; i++) {
			_binaryImageLabels[i] = 0;
		}
	}
	
	/************************************************************
	 * Control Methods
	*************************************************************/
	public Map<Integer, CharacterBounds> FindLabelBounds()
	{
		Map<Integer, CharacterBounds> labelBounds = new HashMap<Integer, CharacterBounds>();
		
		for(int y = 0; y < this.GetHeight(); y++) 
		{
			for(int x = 0; x < this.GetWidth(); x++)
			{
				Point point = new Point(x, y);
				int currentLabel = this.GetLabel(point);
				if(currentLabel == 0) continue;
				CharacterBounds character = labelBounds.get(currentLabel);
				if(character != null) {
					character.SetTop(Math.min(character.GetTop(), point.y));
					character.SetLeft(Math.min(character.GetLeft(), point.x));
					character.SetBottom(Math.max(character.GetBottom(), point.y));
					character.SetRight(Math.max(character.GetRight(), point.x));
					labelBounds.put(currentLabel, character);
				} else {
					labelBounds.put(currentLabel, new CharacterBounds(point.y, point.x, point.y, point.x));
				}
			}
		}
		
		return labelBounds;
	}
	
	public Image GenerateImage(CharacterBounds charImage)
	{
		WritableImage writImage = new WritableImage(charImage.GetRight() - charImage.GetLeft() + 1, charImage.GetBottom() - charImage.GetTop() + 1);
		PixelWriter pixelWriter = writImage.getPixelWriter();
		
		for(int y = charImage.GetTop(); y <= charImage.GetBottom(); y++) {
			for(int x = charImage.GetLeft(); x <= charImage.GetRight(); x++) {
				if(this.GetPixel(new Point(x, y)) == true) {
					pixelWriter.setColor(x - charImage.GetLeft(), y - charImage.GetTop(), Color.BLACK);
				} else {
					pixelWriter.setColor(x - charImage.GetLeft(), y - charImage.GetTop(), Color.WHITE);
				}
			}
		}
		
		return (Image) writImage;
	}
	
	/************************************************************
	 * Helper Methods
	*************************************************************/
	
	public List<Integer> GetNeighbouringLabels(Point point)
	{
		List<Integer> neighbouringLabels = new ArrayList<Integer>();
		
		// pixel to the left
		if ((point.x - 1 >= 0) && (GetLabel(new Point(point.x - 1, point.y)) > 0)) {
			neighbouringLabels.add(GetLabel(new Point(point.x - 1, point.y)));
		}
		
		if(point.y - 1 >= 0) {
			// pixel to the top-left
			if ((point.x - 1 >= 0) && (GetLabel(new Point(point.x - 1, point.y - 1)) > 0)) {
				neighbouringLabels.add(GetLabel(new Point(point.x - 1, point.y - 1)));
			}
			
			// pixel to the top
			if (GetLabel(new Point(point.x, point.y - 1)) > 0) {
				neighbouringLabels.add(GetLabel(new Point(point.x, point.y - 1)));
			}
			
			// pixel to the top-right
			if ((point.x + 1 < _width) && (GetLabel(new Point(point.x + 1, point.y - 1)) > 0)) {
				neighbouringLabels.add(GetLabel(new Point(point.x + 1, point.y - 1)));
			}
		}
		
		return neighbouringLabels;
	}
	
	private Image GenerateImage()
	{
		WritableImage writImage = new WritableImage(_width, _height);
		PixelWriter pixelWriter = writImage.getPixelWriter();
		
		for(int y = 0; y < _height; y++) {
			for(int x = 0; x < _width; x++) {
				if(this.GetPixel(new Point(x, y)) == true) {
					pixelWriter.setColor(x, y, Color.BLACK);
				} else {
					pixelWriter.setColor(x, y, Color.WHITE);
				}
			}
		}
		
		return (Image) writImage;
	}
	
	/************************************************************
	 * Getters and Setters
	*************************************************************/
	
	public int GetWidth()
	{
		return _width;
	}
	
	public int GetHeight()
	{
		return _height;
	}
	
	public boolean GetPixel(Point point)
	{
		return _binaryImage[point.y * _width + point.x];
	}
	
	public void SetPixel(Point point, boolean pixel)
	{
		_binaryImage[point.y * _width + point.x] = pixel;
	}
	
	public int GetLabel(Point point)
	{
		return _binaryImageLabels[point.y * _width + point.x];
	}
	
	public void SetLabel(Point point, int label)
	{
		_binaryImageLabels[point.y * _width + point.x] = label;
	}
	
	public Image GetImage() 
	{
		return this.GenerateImage();
	}
	
	/************************************************************
	 * Override Methods
	*************************************************************/
	
	public String toString()
	{
		String string = "";
		
		string += "Image";
		for(int i = 0; i < _binaryImage.length; i++) {
			if(i % _width == 0) {
				string += System.lineSeparator();
			}
			string += _binaryImage[i] == true ? "1" : "0";
			string += " ";
		}
		string += System.lineSeparator();
		
		string += "Labels";
		for(int i = 0; i < _binaryImageLabels.length; i++) {
			if(i % _width == 0) {
				string += System.lineSeparator();
			}
			string += _binaryImageLabels[i] + " ";
		}
		
		return string;
	}
}

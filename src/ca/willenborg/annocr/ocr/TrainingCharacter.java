package ca.willenborg.annocr.ocr;

import java.awt.Point;

public class TrainingCharacter implements Comparable<TrainingCharacter>, Cloneable{

	private boolean[] _binaryImage;
	private int _width = 0;
	private int _height = 0;
	private char _character;
	
	/********************************************************************************
	 * Constructors
	 ********************************************************************************/
	
	public TrainingCharacter(final char character, final int width, final int height) 
	{
		_binaryImage = new boolean[width * height];
		_width = width;
		_height = height;
		_character = character;
	}
	
	public TrainingCharacter(final char character, boolean[] image, final int width, final int height) 
	{
		_binaryImage = image;
		_width = width;
		_height = height;
		_character = character;
	}
	
	/********************************************************************************
	 * Control Methods
	 ********************************************************************************/
	
	public void Clear() 
	{
		for (int x = 0; x < _width; x++) {
			for (int y = 0; y < _height; y++) {
				_binaryImage[y * _width + x] = false;
			}
		}
	}
	
	/********************************************************************************
	 * Getters and Setter
	 ********************************************************************************/

	public boolean GetData(final Point point)
	{
		return _binaryImage[point.y * _width + point.x];
	}
	
	public void SetData(final Point point, final boolean value)
	{
		_binaryImage[point.y * _width + point.x] = value;
	}
	
	public char GetCharacter() 
	{
		return _character;
	}
	
	public void SetCharacter(char character)
	{
		_character = character;
	}

	public int GetWidth() 
	{
		return _width;
	}
	
	public int GetHeight() 
	{
		return _height;
	}
	
	/********************************************************************************
	 * Overrides
	 ********************************************************************************/
	
	@Override
	public int compareTo(TrainingCharacter o)
	{
		final TrainingCharacter obj = o;
		if (_character > obj.GetCharacter()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof TrainingCharacter)) return false;
		
		final TrainingCharacter obj = (TrainingCharacter) o;
		if (_character == obj.GetCharacter()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public Object clone()
	{
		final TrainingCharacter obj = new TrainingCharacter(_character, GetWidth(), GetHeight());
		
		for (Point point = new Point(0, 0); point.y < GetHeight(); point.y++) {
			for (point.x = 0; point.x < GetWidth(); point.x++) {
				obj.SetData(point, GetData(point));
			}
		}
		
		return obj;
	}
	
	@Override
	public String toString() 
	{
		return "" +  _character;
	}

}

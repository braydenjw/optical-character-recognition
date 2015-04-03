package ca.willenborg.annocr;

import org.encog.examples.neural.gui.ocr.SampleData;

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

	public boolean GetData(final int x, final int y)
	{
		return _binaryImage[y * _width + x];
	}
	
	public void SetData(final int x, final int y, final boolean value)
	{
		_binaryImage[y * _width + x] = value;
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
	public Object clone()
	{
		final TrainingCharacter obj = new TrainingCharacter(_character, GetWidth(), GetHeight());
		
		for (int y = 0; y < GetHeight(); y++) {
			for (int x = 0; x < GetWidth(); x++) {
				obj.SetData(x, y, GetData(x, y));
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

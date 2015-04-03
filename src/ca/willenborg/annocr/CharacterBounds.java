package ca.willenborg.annocr;

public class CharacterBounds
{
	private int _top, _left, _bottom, _right;
	private int _width, _height;
	
	/********************************************************************************
	 * Constructors
	 ********************************************************************************/
	
	public CharacterBounds()
	{
		_top = _left = _bottom = _right = -1;
		_width = _height = 0;
	}
	
	public CharacterBounds(int top, int left, int bottom, int right)
	{
		_top = top;
		_left = left;
		_bottom = bottom;
		_right = right;
	}
	
	/********************************************************************************
	 * Getters and Setters
	 ********************************************************************************/
	
	public void SetLeft(int left)
	{
		_left = left;
		_width = _right - _left;
	}
	
	public int GetLeft()
	{
		return _left;
	}
	
	public void SetTop(int top)
	{
		_top = top;
		_height = _bottom - _top;
	}
	
	public int GetTop()
	{
		return _top;
	}
	
	public void SetRight(int right)
	{
		_right = right;
		_width = _right - _left;
	}
	
	public int GetRight()
	{
		return _right;
	}
	
	public void SetBottom(int bottom)
	{
		_bottom = bottom;
		_height = _bottom - _top;
	}
	
	public int GetBottom()
	{
		return _bottom;
	}
	
	public int GetWidth()
	{
		return _width;
	}
	
	public int GetHeight()
	{
		return _height;
	}
	
}
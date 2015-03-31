package ca.willenborg.annocr;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class CharacterImage
{
	private int _top, _left, _bottom, _right;
	
	public CharacterImage()
	{
		_top = _left = _bottom = _right = -1;
	}
	
	public CharacterImage(int top, int left, int bottom, int right)
	{
		_top = top;
		_left = left;
		_bottom = bottom;
		_right = right;
	}
	
	public void SetLeft(int left)
	{
		_left = left;
	}
	
	public int GetLeft()
	{
		return _left;
	}
	
	public void SetTop(int top)
	{
		_top = top;
	}
	
	public int GetTop()
	{
		return _top;
	}
	
	public void SetRight(int right)
	{
		_right = right;
	}
	
	public int GetRight()
	{
		return _right;
	}
	
	public void SetBottom(int bottom)
	{
		_bottom = bottom;
	}
	
	public int GetBottom()
	{
		return _bottom;
	}
	
}
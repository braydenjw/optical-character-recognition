package ca.willenborg.annocr;

import java.awt.Point;

public class Pixel {

    private Point _position;
    private boolean _value;

    /************************************************************
   	 * Constructors
   	*************************************************************/
    
    public Pixel(Point position, boolean value)
    {
        _position = position;
        _value = value;
    }
    
    /************************************************************
	 * Getters and Setters
	*************************************************************/
    
    public Point GetPosition()
    {
    	return _position;
    }
    
    public void SetPosition(Point position)
    {
    	_position = position;
    }
    
    public boolean GetValue()
    {
    	return _value;
    }
    
    public void SetValue(boolean value)
    {
    	_value = value;
    }
}

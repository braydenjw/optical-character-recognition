package ca.willenborg.annocr;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class LabeledBinaryImage {

	private boolean[][] _binaryImage;
	private int[][]		_binaryImageLabels;
	
	/************************************************************
	 * Constructors
	*************************************************************/
	
	public LabeledBinaryImage(int x, int y) 
	{
		_binaryImage = new boolean[y][x];
		_binaryImageLabels = new int[y][x];
		
		for(int i = 0; i < this.GetWidth(); i++) {
			for(int j = 0; j < this.GetHeight(); j++) {
				_binaryImage[j][i] = false;
				_binaryImageLabels[j][i] = 0;
			}
		}
	}
	
	/************************************************************
	 * Control Methods
	*************************************************************/
	
	/************************************************************
	 * Helper Methods
	*************************************************************/
	
	public List<Integer> GetNeighbouringLabels(Point point)
	{
		List<Integer> neighbouringLabels = new ArrayList<Integer>();
		
		for(int i = point.y - 1; (i < this.GetHeight() - 1) && (i <= point.y + 2); i++) {
            for (int j = point.x - 1; (j < this.GetWidth() - 1) && (j <= point.x + 2); j++) {
            	Point curPoint = new Point(i, j);
                if ((i > -1) && (j > -1) && (this.GetLabel(curPoint) != 0)) {
                    neighbouringLabels.add(this.GetLabel(curPoint));
                }
            }
        }
		
		return neighbouringLabels;
	}
	
	/************************************************************
	 * Getters and Setters
	*************************************************************/
	
	public int GetWidth()
	{
		return _binaryImage.length;
	}
	
	public int GetHeight()
	{
		return _binaryImage[0].length;
	}
	
	public boolean GetPixel(Point point)
	{
		return _binaryImage[point.y][point.x];
	}
	
	public void SetPixel(Point point, boolean pixel)
	{
		_binaryImage[point.y][point.x] = pixel;
	}
	
	public int GetLabel(Point point)
	{
		return _binaryImageLabels[point.y][point.x];
	}
	
	public void SetLabel(Point point, int label)
	{
		_binaryImageLabels[point.y][point.x] = label;
	}
}

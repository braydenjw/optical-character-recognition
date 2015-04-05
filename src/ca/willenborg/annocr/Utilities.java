package ca.willenborg.annocr;
import java.awt.Point;


public class Utilities {

	public static void printImage(Boolean[] image, int width)
	{
		for(Point point = new Point(0, 0); point.y < image.length / width; point.y++) {
			for(point.x = 0; point.x < width; point.x++) {
				char character = image[point.y * width + point.x] == true ? '1' : '0';
				System.out.print(character);
			}
			System.out.println(" ");
		}
	}
	
	public static void printImage(boolean[] image, int width)
	{
		for(Point point = new Point(0, 0); point.y < image.length / width; point.y++) {
			for(point.x = 0; point.x < width; point.x++) {
				char character = image[point.y * width + point.x] == true ? '1' : '0';
				System.out.print(character);
			}
			System.out.println(" ");
		}
	}
	
}

package ca.willenborg.annocr;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.clustercopy.SOMClusterCopyTraining;

public class OpticalCharacterRecognition {

	public static final int DOWNSAMPLE_WIDTH = 5;
	public static final int DOWNSAMPLE_HEIGHT = 7;
	
	private List<TrainingCharacter> _trainingCharacters;	
	private SOM _neuralNetwork;
	
	/********************************************************************************
	 * Constructors
	 ********************************************************************************/
	
	public OpticalCharacterRecognition()
	{
		_trainingCharacters = new ArrayList<TrainingCharacter>();
	}
	
	/********************************************************************************
	 * Control Methods
	 ********************************************************************************/
	
	public void Add(char character, boolean[] characterImage, int width) 
	{
		boolean downSample[] = DownSample(characterImage, width);
		
		final TrainingCharacter trainingCharacter = new TrainingCharacter(character, downSample, DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT);

		for (int i = 0; i < _trainingCharacters.size(); i++) {
			final Comparable<TrainingCharacter> compare = (Comparable<TrainingCharacter>) _trainingCharacters.get(i);
			if (compare.equals(character)) {
				System.out.println("This character has alread been assigned.");
				return;
			}

			if (compare.compareTo(trainingCharacter) > 0) {
				_trainingCharacters.add(i, trainingCharacter);
				return;
			}
		}
		
		_trainingCharacters.add(trainingCharacter);
	}
	
	/**
	 * Desc:	Trains the neural network will all the training characters in @_trainingCharacters
	 * Params:	None.
	 * SideFx:	@_neuralNetwork is trained.
	 * Return:	None.
	 */
	public void TrainNeuralNetwork() 
	{
		try {
			final int input = DOWNSAMPLE_HEIGHT * DOWNSAMPLE_WIDTH;
			final int output = _trainingCharacters.size();
			final MLDataSet trainingDataSet = new BasicMLDataSet();
			
			for (int i = 0; i < _trainingCharacters.size(); i++) {
				final MLData item = new BasicMLData(input);
				int index = 0;
				final TrainingCharacter currentCharacter = _trainingCharacters.get(i);
				for (int y = 0; y < currentCharacter.GetHeight(); y++) {
					for (int x = 0; x < currentCharacter.GetWidth(); x++) {
						item.setData(index++, currentCharacter.GetData(x, y) ? 0.5 : -0.5);
					}
				}

				trainingDataSet.add(new BasicMLDataPair(item, null));
			}

			_neuralNetwork = new SOM(input, output);
			_neuralNetwork.reset();

			SOMClusterCopyTraining train = new SOMClusterCopyTraining(_neuralNetwork, trainingDataSet);
			train.iteration();

			System.out.println("Training successful.");
		} catch (final Exception e) {
			System.out.println("Error during training.");
		}
	}
	
	/********************************************************************************
	 * Helper Methods
	 ********************************************************************************/
	
	public static boolean[] DownSample(boolean image[], final int width)
	{
		return DownSample(image, width, DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT);
	}
	
	public static boolean[] DownSample(boolean[] image, final int width, final int dsWidth, final int dsHeight)
	{
		boolean[] downSample = new boolean[dsWidth * dsHeight];
		int height = image.length / width;
		int top = 0, left = 0, bottom = 0, right = 0;
		int xRegionSize = width / dsWidth;
		int yRegionSize = height / dsHeight;
		boolean xRegionRemainder = width % dsWidth != 0;
		boolean yRegionRemainder = height % dsHeight != 0;
		
		for(int y = 0; y < dsHeight; y++) {
			bottom = (y == dsHeight - 1) && (yRegionRemainder == true) ? top + yRegionSize : top + yRegionSize - 1;
			for(int x = 0; x < dsWidth; x++) {
				right = (x == dsWidth - 1) && (xRegionRemainder == true) ? left + xRegionSize : left + xRegionSize - 1;
				downSample[y * dsWidth + x] = DownSampleHelper(image, width, top, left, bottom, right);
				left = right + 1;
			}
			left = right = 0;
			top = bottom + 1;
		}
		
		return downSample;
	}

	private static boolean DownSampleHelper(boolean[] image, int width, int top, int left, int bottom, int right)
	{
		for(int y = top; y <= bottom; y++) {
			for(int x = left; x <= right; x++) {
				if(image[y * width + x] == true) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}

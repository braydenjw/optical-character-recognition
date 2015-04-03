package ca.willenborg.annocr;

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
	
//	@SuppressWarnings("unchecked")
//	public void Add(char character, boolean[] characterImage) 
//	{
//		// must downsmaple the image here
//		this.entry.downSample();
//		
//		final TrainingCharacter trainingCharacter = (SampleData) this.sample.getData().clone();
//		trainingCharacter.SetCharacter(character);
//
//		for (int i = 0; i < _trainingCharacters.size(); i++) {
//			final Comparable<TrainingCharacter> compare = (Comparable<TrainingCharacter>) _trainingCharacters.get(i);
//			if (compare.equals(character)) {
//				System.out.println("This character has alread been assigned.");
//				return;
//			}
//
//			if (compare.compareTo(trainingCharacter) > 0) {
//				_trainingCharacters.add(i, trainingCharacter);
//				return;
//			}
//		}
//		
//		_trainingCharacters.add(trainingCharacter);
//	}
	
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
	
}

package data_mining.p2.drivers;

import java.io.IOException;
import java.util.Scanner;

import data_mining.p2.miners.NeuralNetwork;
import data_mining.p2.util.ConfigurationObject;

/**
 * User application for testing S&P info
 * 
 * @author eddie
 *
 */
public class Section3Part2UserApp {
	private static final int QUESTION_NUM = 2;
	private static final int SECTION_NUM = 3;

	public static void main(String[] args) throws IOException {
		ConfigurationObject config = ConfigurationObject.getInstance();
		NeuralNetwork classifier = new NeuralNetwork(
				config.getConverter(SECTION_NUM, QUESTION_NUM));

		final String trainingFile =
				config.getFileTraining(SECTION_NUM, QUESTION_NUM);

		// initialize and train net
		classifier.loadTrainingData(trainingFile);
		classifier.setParameters(4, 75_000, 53467, .65);
		classifier.train();

		// user interface follows here:
		Scanner kb = new Scanner(System.in);
		String testFile, destFile;

		System.out.println("Enter test file: ");
		testFile = kb.nextLine();

		System.out.println("Enter file destination: ");
		destFile = kb.nextLine();

	}

}

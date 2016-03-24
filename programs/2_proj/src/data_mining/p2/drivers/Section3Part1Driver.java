package data_mining.p2.drivers;

import java.io.IOException;

import data_mining.p2.miners.NeuralNetwork;
import data_mining.p2.util.ConfigurationObject;

/**
 * Driver for section 3 problem 1, converts, displays, classifies all the data
 *
 * @author eddie
 *
 */
public class Section3Part1Driver {
	private static final int SECTION_NUM = 3;

	public static void main(String[] args) throws IOException {
		ConfigurationObject config = ConfigurationObject.getInstance();
		int questionNumber = 5;
		// this question offset is a hack so that i can get 5 different files
		// from a method that should only return one
		final int questionOffset = 11;

		for (int i = 0; i < questionNumber; i++) {
			NeuralNetwork classifier = new NeuralNetwork(
					config.getConverter(SECTION_NUM, i + questionOffset));

			final String trainingFile =
					config.getFileTraining(SECTION_NUM, i + questionOffset);
			final String testingFile =
					config.getFileTesting(SECTION_NUM, i + questionOffset);
			final String classifiedFile =
					config.getFileClassified(SECTION_NUM, i + questionOffset);
			final String validationFile =
					config.getFileValidation(SECTION_NUM, i + questionOffset);

			classifier.loadTrainingData(trainingFile);

			classifier.setParameters(6, 75_000, 53467, .65);

			classifier.train();

			classifier.testData(testingFile, classifiedFile);
			// classifier.displayWeightsAndThetas();

			System.out.printf("training%d error:   %7.4f%%%n", (i + 1),
				classifier.computeValidationError(trainingFile) * 100);
			System.out.printf("validation%d error: %7.4f%%%n", (i + 1),
				(classifier.computeValidationError(validationFile) * 100));
		}

	}
}

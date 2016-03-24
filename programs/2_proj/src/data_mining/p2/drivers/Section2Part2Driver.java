package data_mining.p2.drivers;

import java.io.IOException;

import data_mining.p2.miners.NeuralNetwork;
import data_mining.p2.util.ConfigurationObject;

/**
 * Drive for section 2 question 2, best properties are 10 hidden nodes, 100_000
 * iterations, and .85 learning rate
 *
 * @author eddie
 *
 */
public class Section2Part2Driver {
	private static final int QUESTION_NUM = 2;
	private static final int SECTION_NUM = 2;

	public static void main(String[] args) throws IOException {
		ConfigurationObject config = ConfigurationObject.getInstance();

		NeuralNetwork classifier = new NeuralNetwork(
				config.getConverter(SECTION_NUM, QUESTION_NUM));

		final String trainingFile =
				config.getFileTraining(SECTION_NUM, QUESTION_NUM);
		final String testingFile =
				config.getFileTesting(SECTION_NUM, QUESTION_NUM);
		final String classifiedFile =
				config.getFileClassified(SECTION_NUM, QUESTION_NUM);
		final String validationFile =
				config.getFileValidation(SECTION_NUM, QUESTION_NUM);

		classifier.loadTrainingData(trainingFile);

		classifier.setParameters(10, 100_000, 53467, .85);

		classifier.train();

		classifier.testData(testingFile, classifiedFile);
		classifier.displayWeightsAndThetas();

		System.out.printf("training error:   %7.4f%%%n",
			classifier.computeTrainingError());
		System.out.printf("validation error: %7.4f%%%n",
			(classifier.computeValidationError(validationFile) * 100));
	}
}

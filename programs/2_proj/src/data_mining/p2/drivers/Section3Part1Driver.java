package data_mining.p2.drivers;
import java.io.IOException;

import data_mining.p2.miners.NeuralNetwork;
import data_mining.p2.util.ConfigurationObject;

public class Section3Part1Driver {
	private static final int SECTION_NUM = 3;

	public static void main(String[] args) throws IOException {
		ConfigurationObject config = ConfigurationObject.getInstance();
		int questionNumber = 5;
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

			classifier.setParameters(4, 50_000, 53467, .65);

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

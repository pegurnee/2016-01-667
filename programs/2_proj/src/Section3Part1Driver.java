import java.io.IOException;

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

			classifier.setParameters(5, 10_000, 53467, .85);

			classifier.train();

			classifier.testData(testingFile, classifiedFile);
			// classifier.displayWeightsAndThetas();

			System.out.printf("training error:   %7.4f%%%n",
				classifier.computeTrainingError());
			System.out.printf("validation error: %7.4f%%%n",
				(classifier.computeValidationError(validationFile) * 100));
		}

	}
}

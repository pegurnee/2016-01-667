import java.io.IOException;

public class Section1Part3Driver {
	private static final int QUESTION_NUM = 3;
	private static final int SECTION_NUM = 1;

	public static void main(String[] args) throws IOException {
		ConfigurationObject config = ConfigurationObject.getInstance();

		BayesClassifier classifier = new BayesClassifier(
				config.getConverter(SECTION_NUM, QUESTION_NUM));

		final String trainingFile =
				config.getFileTraining(SECTION_NUM, QUESTION_NUM);
		final String testingFile =
				config.getFileTesting(SECTION_NUM, QUESTION_NUM);
		final String classifiedFile =
				config.getFileClassified(SECTION_NUM, QUESTION_NUM);
		classifier.loadTrainingData(trainingFile);

		classifier.buildModel();

		classifier.classifyData(testingFile, classifiedFile);

		System.out.println(classifier.computeTrainingError());
		System.out.println(classifier.validateWithLeaveOneOut(trainingFile));

		classifier.displayProbabilityTables();
	}
}

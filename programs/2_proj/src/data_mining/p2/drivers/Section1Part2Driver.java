package data_mining.p2.drivers;

import java.io.IOException;

import data_mining.p2.miners.BayesClassifier;
import data_mining.p2.util.ConfigurationObject;

public class Section1Part2Driver {
	private static final int QUESTION_NUM = 2;
	private static final int SECTION_NUM = 1;

	public static void main(String[] args) throws IOException {
		BayesClassifier classifier = new BayesClassifier();
		ConfigurationObject config = ConfigurationObject.getInstance();

		final String trainingFile =
				config.getFileTraining(SECTION_NUM, QUESTION_NUM);
		final String testingFile =
				config.getFileTesting(SECTION_NUM, QUESTION_NUM);
		final String classifiedFile =
				config.getFileClassified(SECTION_NUM, QUESTION_NUM);
		classifier.loadTrainingData(trainingFile);

		classifier.buildModel();

		classifier.classifyData(testingFile, classifiedFile);

		System.out.println(
			"training error: " + classifier.computeTrainingError());
		System.out.println("validation error: "
							+ classifier.validateWithLeaveOneOut(trainingFile));

		classifier.displayProbabilityTables();

	}
}

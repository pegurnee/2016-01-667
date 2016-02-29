package data_mining.p1.drivers;
import java.io.IOException;

import data_mining.p1.classifiers.NearestNeighbor;
import data_mining.p1.converters.BankLoanNearestNeighborDataConverter;

public class BankLoanClassification {
	public static void main(String[] args) throws IOException {
		NearestNeighbor moore =
				new NearestNeighbor(new BankLoanNearestNeighborDataConverter());

		String inFolder = "in/", outFolder = "out/";
		String trainingFile = inFolder + "train4",
				testingFile = inFolder + "test4",
				classifiedFile = outFolder + "classified4";

		moore.loadTrainingData(trainingFile);

		moore.classifyData(testingFile, classifiedFile);

		moore.determineTrainingError(trainingFile);
		System.out.println(moore.validateWithLeaveOneOut(trainingFile));

		moore.optimizeNeighbors(trainingFile);

	}
}

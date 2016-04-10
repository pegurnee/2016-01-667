package data_mining.p3.drivers;

import java.io.IOException;

import data_mining.p3.miners.Kmeans;
import data_mining.p3.util.ConfigurationObject;

public class Part1Question4Driver {
	private static final int PART = 1;
	private static final int QUESTION = 3;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ConfigurationObject config = ConfigurationObject.getInstance();
		String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);
		int numberClusters = 9, randomSeed = 54269;

		Kmeans clusterer = new Kmeans();
		clusterer.load(inputFile);

		// TODO: Discover the best number of clusters
		clusterer.setParameters(numberClusters, randomSeed);
		clusterer.cluster();

		clusterer.display(outputFile);
	}
}

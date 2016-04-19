package data_mining.p3.drivers;

import java.io.IOException;

import data_mining.p3.miners.Graph;
import data_mining.p3.util.ConfigurationObject;

public class Part2Question2Driver {
	private static final int PART = 2;
	private static final int QUESTION = 2;

	public static void main(String[] args) throws IOException {
		ConfigurationObject config = ConfigurationObject.getInstance();
		String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);
		double distance = 3;

		Graph clusterer = new Graph();
		clusterer.load(inputFile);

		clusterer.setParameters(distance);
		clusterer.cluster();

		clusterer.display(outputFile);

		// repeat with different set of distance thresholds
		int[] distances = { 1, 5, 10 };
		for (int i = 0; i < distances.length; i++) {
			clusterer = new Graph();
			clusterer.load(inputFile);
			clusterer.setParameters(distances[i]);

			clusterer.cluster();

			clusterer.display(outputFile + "distance" + distances[i]);
		}
	}
}

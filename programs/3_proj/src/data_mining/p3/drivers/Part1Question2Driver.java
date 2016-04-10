package data_mining.p3.drivers;

import java.io.IOException;

import data_mining.p3.miners.Kmeans;
import data_mining.p3.util.ConfigurationObject;

public class Part1Question2Driver {
	private static final int PART = 1;
	private static final int QUESTION = 2;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ConfigurationObject config = ConfigurationObject.getInstance();
		String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);
		int numberClusters = 3, randomSeed = 54269;
		boolean trace = true;
		boolean ordered = true;

		Kmeans clusterer = new Kmeans();
		clusterer.load(inputFile);
		clusterer.setParameters(numberClusters, randomSeed, trace);
		clusterer.cluster();

		// output centroids at end of clustering

		for (int cluster = 0; cluster < numberClusters; cluster++) {
			System.out.println(String.format("Error rate for cluster %d: %.5f", cluster + 1,
					clusterer.computeSumSquaredErrorOfCluster(cluster)));
		}

		// trace the centroid changes throughout the clustering

		clusterer.display(outputFile, ordered);

		// repeat with different set of initial centroids
		long[] seeds = { 0, 20_000, 10271990 };
		for (int i = 0; i < seeds.length; i++) {
			clusterer = new Kmeans();
			clusterer.load(inputFile);
			clusterer.setParameters(numberClusters, seeds[i]);

			clusterer.cluster();

			clusterer.display(outputFile + "seed" + seeds[i], ordered);
		}

		// repeat with cluster number of 2, 5, 10
		int[] clusterNums = { 2, 5, 10 };
		for (int i = 0; i < seeds.length; i++) {
			clusterer = new Kmeans();
			clusterer.load(inputFile);
			clusterer.setParameters(clusterNums[i], randomSeed);

			clusterer.cluster();

			clusterer.display(outputFile + "numclusters" + clusterNums[i], ordered);
		}

	}

}

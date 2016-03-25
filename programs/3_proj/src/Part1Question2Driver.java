import java.io.IOException;

public class Part1Question2Driver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = "in/somefile", outputFile = "out/somefile";
		int numberClusters = 3, randomSeed = 54269;

		Kmeans clusterer = new Kmeans();
		clusterer.load(inputFile);
		clusterer.setParameters(numberClusters, randomSeed);
		clusterer.cluster();

		// TODO: output centroids at end of clustering

		for (int cluster = 0; cluster < numberClusters; cluster++) {
			System.out.println(String.format("Error rate for cluster %d: %.5f",
				cluster, clusterer.computeSumSquaredErrorOfCluster(cluster)));
		}

		// TODO: trace the centroid changes throughout the clustering

		clusterer.display(outputFile);

		// TODO: repeat with different set of initial centroids

		// TODO: repeat with cluster number of 2, 5, 10
	}

}

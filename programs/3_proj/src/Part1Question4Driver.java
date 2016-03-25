import java.io.IOException;

public class Part1Question4Driver {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = "in/somefile", outputFile = "out/somefile";
		int numberClusters = 3, randomSeed = 54269;

		Kmeans clusterer = new Kmeans();
		clusterer.load(inputFile);

		// TODO: Discover the best number of clusters
		clusterer.setParameters(numberClusters, randomSeed);
		clusterer.cluster();

		clusterer.display(outputFile);
	}
}

import java.io.IOException;

public class Part3Question1Driver {
	private static final int PART = 3;
	private static final int QUESTION = 1;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ConfigurationObject config = ConfigurationObject.getInstance();
		String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);
		int numClusters = 8, seed = 54269;

		Kmeans clusterer = new Kmeans();
		clusterer.loadImage(inputFile, 2);

		// TODO: Was a distance specced?
		clusterer.setParameters(numClusters, seed);
		clusterer.cluster();

		clusterer.display(outputFile);
	}
}

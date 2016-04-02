import java.io.IOException;

public class Part2Question4Driver {
	private static final int PART = 2;
	private static final int QUESTION = 3;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ConfigurationObject config = ConfigurationObject.getInstance();
		String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);
		double distance = 3.25;

		Graph clusterer = new Graph();
		clusterer.load(inputFile);

		// TODO: Was a distance specced?
		clusterer.setParameters(distance);
		clusterer.cluster();

		clusterer.display(outputFile);
	}
}

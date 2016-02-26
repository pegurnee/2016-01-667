import java.io.IOException;

public class JavaResumeClassification {
	public static void main(String[] args) throws IOException {
		DecisionTree tree =
				new DecisionTree(new JavaResumeDecisionTreeDataConverter());

		boolean buildTrace = false;
		String inFolder = "in/", outFolder = "out/";
		String trainingFile = inFolder + "train2",
				testingFile = inFolder + "test2",
				classifiedFile = outFolder + "classified2";

		tree.loadTrainingData(trainingFile);
		tree.buildTree(buildTrace);

		System.out.println("decision tree: " + tree);
		tree.classifyData(testingFile, classifiedFile);

		tree.runFullAnalytics(trainingFile);
	}
}

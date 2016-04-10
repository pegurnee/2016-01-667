import java.io.IOException;

public class Part3Question2Driver {
    private static final int PART = 3;
    private static final int QUESTION = 2;

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        final ConfigurationObject config = ConfigurationObject.getInstance();
        final String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);

        final int[] numClusters = { 2, 4, 8, 16, 32, 64, 128 };
        for (int i = 0; i < numClusters.length; i++) {
            final double compressionRatio =
                    Kmeans.compressionRatio(inputFile, outputFile + ".min." + numClusters[i]) * 100.0;

            final double loss = Kmeans.loss(inputFile, outputFile + ".min." + numClusters[i]) * 100.0;

            System.out.println(
                    String.format("Using %d clusters, the compression ratio was: %.5f%%%nWith a %.5f%% data loss",
                            numClusters[i], compressionRatio, loss));
        }

    }
}

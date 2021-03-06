package data_mining.p3.drivers;

import java.io.IOException;

import data_mining.p3.miners.Kmeans;
import data_mining.p3.util.ConfigurationObject;

public class Part3Question1Driver {
    private static final int PART = 3;
    private static final int QUESTION = 1;

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        final ConfigurationObject config = ConfigurationObject.getInstance();
        final String inputFile = config.getInFile(PART, QUESTION), outputFile = config.getOutFile(PART, QUESTION);
        final int numClusters = 128, seed = 54269;

        final Kmeans clusterer = new Kmeans();

        clusterer.setParameters(numClusters, seed);

        clusterer.compress(inputFile, outputFile + ".min");
        clusterer.decompress(outputFile + ".min", outputFile + ".fix");

        System.out.println(Kmeans.compressionRatio(inputFile, outputFile + ".min"));
    }
}

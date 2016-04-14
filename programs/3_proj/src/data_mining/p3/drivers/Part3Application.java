package data_mining.p3.drivers;

import java.io.IOException;
import java.util.Scanner;

import data_mining.p3.miners.Kmeans;

public class Part3Application {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        final String inputFile, outputFile;
        int numClusters = 128;
        final int seed = 54269;

        final Scanner kb = new Scanner(System.in);

        System.out.print("Please enter a filename: ");
        inputFile = kb.nextLine();
        outputFile = inputFile + ".min";

        System.out.print("Please enter a desired number of cluster [default 128]: ");
        final String nextLine = kb.nextLine();
        if (nextLine.trim().isEmpty()) {
            numClusters = 128;
        } else {
            numClusters = Integer.parseInt(nextLine);
        }

        final Kmeans clusterer = new Kmeans();

        clusterer.setParameters(numClusters, seed);
        clusterer.compress(inputFile, outputFile);
        clusterer.decompress(outputFile, outputFile + ".restored");

        System.out
                .println("Compression ratio: " + (Kmeans.compressionRatio(inputFile, outputFile + ".min") * 100) + "%");

        kb.close();
    }
}

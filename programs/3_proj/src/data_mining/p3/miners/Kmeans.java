package data_mining.p3.miners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import data_mining.p3.util.ProjectThreeTools;

/**
 * K-means clusterer, original code presented by Dr Suchindran Maniccam.
 * Modified and expanded by Eddie Gurnee for use in Data Mining project 3.
 *
 * @author smaniccam
 * @author eddie
 *
 */
public class Kmeans {
    /**
     * Record class that holds basic record information.
     *
     * @author eddie
     *
     */
    private class Record {
        private final double[] attributes;

        private Record(double[] attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            return Arrays.toString(this.attributes);
        }
    }

    public static double compressionRatio(String realFile, String compressedFile) {
        final long realFileSize = new File(realFile).length(), compressedFileSize = new File(compressedFile).length();
        return compressedFileSize / (double) realFileSize;
    }

    public static double loss(String realFile, String restoredFile) throws FileNotFoundException {
        final Scanner inReal = new Scanner(new File(realFile)), inRestored = new Scanner(new File(restoredFile));

        int numpoints = 0;
        double distance = 0.0;
        while (inReal.hasNext() && inRestored.hasNext()) {
            distance += ProjectThreeTools.distance(inReal.nextDouble(), inRestored.nextDouble());
            numpoints += 1;
        }

        inReal.close();
        inRestored.close();

        return distance / (numpoints * 256);
    }

    private ArrayList<Record> centroids;

    private int[] clusters;
    private int numberAttributes;
    private int numberClusters;

    private int numberRecords;
    private Random rand;
    private int[][] ranges;

    private ArrayList<Record> records;

    private boolean trace;

    public Kmeans() {
        this.numberRecords = 0;
        this.numberAttributes = 0;
        this.numberClusters = 0;

        this.records = null;
        this.centroids = null;
        this.clusters = null;
        this.rand = null;

        this.trace = false;
    }

    /**
     * The actual algorithm that does the clustering.
     */
    public void cluster() {
        // first initialize the clusters and centroids
        this.initializeClusters();
        this.initializeCentroids();

        boolean stopCondition = false;
        // continue until the exit condition is reached
        while (!stopCondition) {

            // get cluster changes and update new centroid location
            final int clusterChanges = this.assignClusters();
            this.updateCentroids();

            stopCondition = clusterChanges == 0;
        }
    }

    /**
     * @param outputFile
     * @throws IOException
     */
    public void compress(String inputFile, String outputFile) throws IOException {
        final int recordSize = 2;
        final int imgSize = this.loadImage(inputFile, recordSize);

        this.cluster();

        final PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        final Record[] centroids = this.centroids.toArray(new Record[this.centroids.size()]);

        outFile.println(this.numberAttributes + " " + (imgSize / recordSize));
        outFile.println(centroids.length);

        for (int i = 0; i < centroids.length; i++) {
            for (int j = 0; j < this.numberAttributes; j++) {
                final double currAtt = centroids[i].attributes[j];
                centroids[i].attributes[j] = Math.round(currAtt);
                outFile.print((int) centroids[i].attributes[j] + " ");
            }
        }

        for (int i = 0; i < this.numberRecords; i++) {
            outFile.print(this.clusters[i] + " ");
            if (0 == ((i + 1) % (imgSize / recordSize))) {
                outFile.println();
            }
        }

        outFile.close();
    }

    /**
     * Computes the sum squared error of the values in a cluster compared to its
     * centroid.
     *
     * @param centroidNumber
     *            of the centroid to compute the error
     * @return the total sum squared distance of the given cluster
     */
    public double computeSumSquaredErrorOfCluster(int centroidNumber) {
        double sum = 0.0;

        // go through each record and check cluster
        for (int i = 0; i < this.clusters.length; i++) {
            if (this.clusters[i] == centroidNumber) {
                // get the distance from the centroid to each record in the
                // cluster, and add it to the running distance total
                final double distance = this.distance(this.records.get(i), this.centroids.get(centroidNumber));
                sum += distance * distance;
            }
        }

        return sum;
    }

    public void decompress(String inputFile, String outputFile) throws IOException {
        final Scanner inFile = new Scanner(new File(inputFile));

        final int numAttributes = inFile.nextInt();
        final int numPerRow = inFile.nextInt();
        final int numberOfCentroids = inFile.nextInt();

        final ArrayList<Record> centroids = new ArrayList<>();

        for (int i = 0; i < numberOfCentroids; i++) {
            final double[] attributes = new double[numAttributes];

            for (int j = 0; j < attributes.length; j++) {
                attributes[j] = inFile.nextDouble();
            }

            centroids.add(new Record(attributes));
        }

        final ArrayDeque<Integer> points = new ArrayDeque<>();
        while (inFile.hasNext()) {
            points.add(inFile.nextInt());
        }

        inFile.close();

        final PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        int j = 0;
        Integer curPoint;
        while ((curPoint = points.poll()) != null) {
            final Record centroid = centroids.get(curPoint.intValue());
            for (int i = 0; i < numAttributes; i++) {
                outFile.print((int) centroid.attributes[i] + " ");
            }
            if ((++j % numPerRow) == 0) {
                outFile.println();
            }
        }

        outFile.close();
    }

    /**
     * Prints the information regarding the clustering to a file
     *
     * @param outputFile
     * @throws IOException
     */
    public void display(String outputFile) throws IOException {
        final PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        this.writeToFileOrdered(outFile);

        outFile.close();
    }

    /**
     * Prints the information regarding the clustering to a file, with an
     * optional ordering
     *
     * @param outputFile
     * @param ordered
     * @throws IOException
     */
    public void display(String outputFile, boolean ordered) throws IOException {
        final PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        if (!ordered) {
            this.writeToFileUnordered(outFile);
        } else {
            this.writeToFileOrdered(outFile);
        }

        outFile.close();
    }

    /**
     * Loads data from a file into the k-means clusterer
     *
     * @param inputFile
     * @throws FileNotFoundException
     */
    public void load(String inputFile) throws FileNotFoundException {
        final Scanner inFile = new Scanner(new File(inputFile));

        this.numberRecords = inFile.nextInt();
        this.numberAttributes = inFile.nextInt();

        final boolean normal = "normal".equals(inFile.next());

        if (!normal) {
            this.ranges = new int[this.numberAttributes][2];

            for (int i = 0; i < this.numberAttributes; i++) {
                final String[] range = inFile.next().split("-");
                for (int j = 0; j < range.length; j++) {
                    this.ranges[i][j] = Integer.parseInt(range[j]);
                }
            }

        }

        this.records = new ArrayList<Record>();

        for (int i = 0; i < this.numberRecords; i++) {
            final double[] attributes = new double[this.numberAttributes];
            for (int j = 0; j < this.numberAttributes; j++) {
                double input = inFile.nextDouble();

                if (null != this.ranges) {
                    input = ProjectThreeTools.normalize(input, this.ranges[j][0], this.ranges[j][1]);
                }
                attributes[j] = input;
            }

            final Record record = new Record(attributes);

            this.records.add(record);
        }

        inFile.close();
    }

    /**
     * Sets parameters for the clusterer (defaults to no trace)
     *
     * @param numberClusters
     * @param seed
     */
    public void setParameters(int numberClusters, long seed) {
        this.setParameters(numberClusters, seed, false);
    }

    /**
     * Sets parameters for the clusterer (only options are the number of
     * clusters, the random number generator seed, and whether or not to trace
     * cluster changes).
     *
     * @param numberClusters
     * @param seed
     */
    public void setParameters(int numberClusters, long seed, boolean trace) {
        this.numberClusters = numberClusters;

        this.rand = new Random(seed);

        this.trace = trace;
    }

    /**
     * Goes through each record, discovers the distance to each centroid, and
     * sets the cluster number for that record to the cluster number of the
     * nearest centroid.
     *
     * @return the number of records that changed clusters
     */
    private int assignClusters() {
        int clusterChanges = 0;

        for (int i = 0; i < this.numberRecords; i++) {
            // gets the record to compare cluster distances
            final Record record = this.records.get(i);

            double minDistance = this.distance(record, this.centroids.get(0));
            int minIndex = 0;

            // discover all the distances, remember smallest one
            for (int j = 0; j < this.numberClusters; j++) {
                final double distance = this.distance(record, this.centroids.get(j));

                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = j;
                }
            }

            // if there is a new closest centroid, change it and record change
            if (this.clusters[i] != minIndex) {
                this.clusters[i] = minIndex;
                clusterChanges++;
            }
        }

        return clusterChanges;
    }

    /**
     * Private method to compute euclidean distance between two records.
     *
     * @param u
     *            record one
     * @param v
     *            record two
     * @return distance between records
     */
    private double distance(Record u, Record v) {
        double sum = 0;

        for (int i = 0; i < u.attributes.length; i++) {
            sum += (u.attributes[i] - v.attributes[i]) * (u.attributes[i] - v.attributes[i]);
        }

        return sum;
    }

    /**
     * Initializes centroids to a random record in the set of records
     */
    private void initializeCentroids() {
        this.centroids = new ArrayList<Record>();

        for (int i = 0; i < this.numberClusters; i++) {
            final int index = this.rand.nextInt(this.numberRecords);

            this.centroids.add(this.records.get(index));
        }
    }

    /**
     * Initializes all of the cluster values for the records to be '-1'; aka, no
     * records have a cluster value at the start
     */
    private void initializeClusters() {
        this.clusters = new int[this.numberRecords];

        for (int i = 0; i < this.numberRecords; i++) {
            this.clusters[i] = -1;
        }
    }

    private int loadImage(String inputFile, int recordSize) throws IOException {
        final Scanner inFile = new Scanner(new File(inputFile));

        this.records = new ArrayList<Record>();

        while (inFile.hasNext()) {
            final double[] vals = new double[recordSize];
            for (int i = 0; i < vals.length; i++) {
                vals[i] = inFile.nextDouble();
            }
            this.records.add(new Record(vals));
        }

        this.numberAttributes = recordSize;
        this.numberRecords = this.records.size();

        inFile.close();

        return (int) Files.lines(Paths.get(inputFile)).count();
    }

    /**
     * Private method to apply a scalar to a record.
     *
     * @param u
     *            record
     * @param k
     *            scalar
     * @return the record with a scalar applied
     */
    private Record scale(Record u, double k) {
        final double[] result = new double[u.attributes.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = k * u.attributes[i];
        }

        return new Record(result);
    }

    /**
     * Private method to compute the in-position sums of two records.
     *
     * @param u
     *            record one
     * @param v
     *            record two
     * @return a record that is the sums of two records
     */
    private Record sum(Record u, Record v) {
        final double[] result = new double[u.attributes.length];

        for (int i = 0; i < u.attributes.length; i++) {
            result[i] = u.attributes[i] + v.attributes[i];
        }

        return new Record(result);
    }

    /**
     * Updates all centroid records to the average value of all of the clustered
     * records.
     */
    private void updateCentroids() {
        final ArrayList<Record> clusterSum = new ArrayList<Record>();

        // initialize an arraylist of records to hold the averages
        for (int i = 0; i < this.numberClusters; i++) {
            final double[] attributes = new double[this.numberAttributes];
            for (int j = 0; j < this.numberAttributes; j++) {
                attributes[j] = 0;
            }

            clusterSum.add(new Record(attributes));
        }

        final int[] clusterSize = new int[this.numberClusters];

        for (int i = 0; i < this.numberClusters; i++) {
            clusterSize[i] = 0;
        }

        // get the summation of all of the cluster records
        for (int i = 0; i < this.numberRecords; i++) {
            final int cluster = this.clusters[i];

            final Record sum = this.sum(clusterSum.get(cluster), this.records.get(i));
            clusterSum.set(cluster, sum);

            clusterSize[cluster] += 1;
        }

        // divide each cluster sum by the number of records in the cluster
        for (int i = 0; i < this.numberClusters; i++) {
            final Record average = this.scale(clusterSum.get(i), 1.0 / clusterSize[i]);

            if (this.trace) {
                System.out.println("Cluster " + (i + 1) + ": " + average);
            }
            this.centroids.set(i, average);
        }
    }

    /**
     * Writes the error rates for each cluster
     *
     * @param outFile
     */
    private void writeErrorRates(PrintWriter outFile) {
        for (int cluster = 0; cluster < this.numberClusters; cluster++) {
            outFile.println(String.format("Error rate for cluster %d: %.5f", cluster + 1,
                    this.computeSumSquaredErrorOfCluster(cluster)));
        }
    }

    /**
     * Writes to the given PrintWriter the records clustered, ordered by cluster
     * label
     *
     * @param outFile
     */
    private void writeToFileOrdered(PrintWriter outFile) {
        for (int i = 0; i < this.numberClusters; i++) {
            outFile.println(String.format("==== Cluster %d ==========: %s", (i + 1), this.centroids.get(i)));
            for (int j = 0; j < this.numberRecords; j++) {
                if (i == this.clusters[j]) {
                    outFile.println(this.records.get(j));
                }
            }
            outFile.println();
        }

        this.writeErrorRates(outFile);
    }

    /**
     * Writes to a given PrintWriter the clustered data
     *
     * @param outFile
     */
    private void writeToFileUnordered(PrintWriter outFile) {
        for (int i = 0; i < this.numberRecords; i++) {
            for (int j = 0; j < this.numberAttributes; j++) {
                outFile.print(this.records.get(i).attributes[j] + " ");
            }

            outFile.println(this.clusters[i] + 1);
        }

        this.writeErrorRates(outFile);
    }
}

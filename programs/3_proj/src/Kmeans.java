
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

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
			return Arrays.toString(attributes);
		}
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
			int clusterChanges = this.assignClusters();
			this.updateCentroids();

			stopCondition = clusterChanges == 0;
		}
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

	/**
	 * Prints the information regarding the clustering to a file
	 *
	 * @param outputFile
	 * @throws IOException
	 */
	public void display(String outputFile) throws IOException {
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		writeToFileOrdered(outFile);

		outFile.close();
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

		writeErrorRates(outFile);
	}

	/**
	 * @param outputFile
	 * @throws IOException
	 */
	public void compress(String outputFile) throws IOException {
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		Record[] centroids = this.centroids.toArray(new Record[this.centroids.size()]);

		outFile.println(this.numberAttributes);
		outFile.println(centroids.length);

		for (int i = 0; i < centroids.length; i++) {
			for (int j = 0; j < this.numberAttributes; j++) {
				double currAtt = centroids[i].attributes[j];
				centroids[i].attributes[j] = (int) Math.round(currAtt);
				outFile.print(centroids[i].attributes[j] + " ");
			}
		}

		for (int i = 0; i < numberRecords; i++) {
			outFile.print(this.clusters[i] + " ");
			if ((i + 1) % 32 == 0)
				outFile.println();
		}

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
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		if (!ordered)
			writeToFileUnordered(outFile);
		else {

			writeToFileOrdered(outFile);
		}

		outFile.close();
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
				if (i == this.clusters[j])
					outFile.println(this.records.get(j));
			}
			outFile.println();
		}

		writeErrorRates(outFile);
	}

	/**
	 * Writes the error rates for each cluster
	 * 
	 * @param outFile
	 */
	private void writeErrorRates(PrintWriter outFile) {
		for (int cluster = 0; cluster < numberClusters; cluster++) {
			outFile.println(String.format("Error rate for cluster %d: %.5f", cluster + 1,
					this.computeSumSquaredErrorOfCluster(cluster)));
		}
	}

	/**
	 * Loads data from a file into the k-means clusterer
	 *
	 * @param inputFile
	 * @throws FileNotFoundException
	 */
	public void load(String inputFile) throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(inputFile));

		this.numberRecords = inFile.nextInt();
		this.numberAttributes = inFile.nextInt();

		boolean normal = "normal".equals(inFile.next());

		if (!normal) {
			ranges = new int[this.numberAttributes][2];

			for (int i = 0; i < this.numberAttributes; i++) {
				String[] range = inFile.next().split("-");
				for (int j = 0; j < range.length; j++) {
					ranges[i][j] = Integer.parseInt(range[j]);
				}
			}

		}

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			double[] attributes = new double[this.numberAttributes];
			for (int j = 0; j < this.numberAttributes; j++) {
				double input = inFile.nextDouble();

				if (null != ranges) {
					input = ProjectThreeTools.normalize(input, ranges[j][0], ranges[j][1]);
				}
				attributes[j] = input;
			}

			Record record = new Record(attributes);

			this.records.add(record);
		}

		inFile.close();
	}

	public void loadImage(String inputFile, int recordSize) throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(inputFile));

		this.records = new ArrayList<Record>();

		while (inFile.hasNext()) {
			double[] vals = new double[recordSize];
			for (int i = 0; i < vals.length; i++) {
				vals[i] = inFile.nextDouble();
			}
			this.records.add(new Record(vals));
		}

		this.numberAttributes = recordSize;
		this.numberRecords = this.records.size();

		// for (int i = 0; i < this.numberRecords; i++) {
		// double[] attributes = new double[this.numberAttributes];
		// for (int j = 0; j < this.numberAttributes; j++) {
		// double input = inFile.nextDouble();
		//
		// if (null != ranges) {
		// input = ProjectThreeTools.normalize(input, ranges[j][0],
		// ranges[j][1]);
		// }
		// attributes[j] = input;
		// }
		//
		// Record record = new Record(attributes);
		//
		// this.records.add(record);
		// }

		inFile.close();
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
	 * Sets parameters for the clusterer (defaults to no trace)
	 * 
	 * @param numberClusters
	 * @param seed
	 */
	public void setParameters(int numberClusters, long seed) {
		this.setParameters(numberClusters, seed, false);
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
			Record record = this.records.get(i);

			double minDistance = this.distance(record, this.centroids.get(0));
			int minIndex = 0;

			// discover all the distances, remember smallest one
			for (int j = 0; j < this.numberClusters; j++) {
				double distance = this.distance(record, this.centroids.get(j));

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
			int index = this.rand.nextInt(this.numberRecords);

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
		double[] result = new double[u.attributes.length];

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
		double[] result = new double[u.attributes.length];

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
		ArrayList<Record> clusterSum = new ArrayList<Record>();

		// initialize an arraylist of records to hold the averages
		for (int i = 0; i < this.numberClusters; i++) {
			double[] attributes = new double[this.numberAttributes];
			for (int j = 0; j < this.numberAttributes; j++) {
				attributes[j] = 0;
			}

			clusterSum.add(new Record(attributes));
		}

		int[] clusterSize = new int[this.numberClusters];

		for (int i = 0; i < this.numberClusters; i++) {
			clusterSize[i] = 0;
		}

		// get the summation of all of the cluster records
		for (int i = 0; i < this.numberRecords; i++) {
			int cluster = this.clusters[i];

			Record sum = this.sum(clusterSum.get(cluster), this.records.get(i));
			clusterSum.set(cluster, sum);

			clusterSize[cluster] += 1;
		}

		// divide each cluster sum by the number of records in the cluster
		for (int i = 0; i < this.numberClusters; i++) {
			Record average = this.scale(clusterSum.get(i), 1.0 / clusterSize[i]);

			if (trace) {
				System.out.println("Cluster " + (i + 1) + ": " + average);
			}
			this.centroids.set(i, average);
		}
	}
}

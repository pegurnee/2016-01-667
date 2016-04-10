
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Graph clusterer, original code presented by Dr Suchindran Maniccam. Modified
 * and expanded by Eddie Gurnee for use in Data Mining project 3.
 *
 * @author smaniccam
 * @author eddie
 *
 */
public class Graph {
	/**
	 * A private record class for recording a record of attributes.
	 *
	 * @author eddie
	 *
	 */
	private class Record {
		private final double[] attributes;

		private Record(double[] attributes) {
			this.attributes = attributes;
		}

		public String toString() {
			return Arrays.toString(attributes);
		}
	}

	private int[] clusters;

	private double delta;

	private int[][] matrix;

	private int numberAttributes;
	private int numberRecords;

	private int[][] ranges;

	private ArrayList<Record> records;

	/**
	 * Null argument constructor, initializes everything to zero or null.
	 */
	public Graph() {
		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.delta = 0;

		this.records = null;
		this.matrix = null;
		this.clusters = null;
	}

	/**
	 * Applies the graph clustering algorithm to the data that has been loaded.
	 */
	public void cluster() {
		// creates neighbor matrix based on distances, and initializes clusters
		// to empty values
		this.createMatrix();
		this.initializeClusters();

		int index = 0;
		int clusterName = 0;

		// for each record, if it is not already part of a cluster (signified
		// with a '-1'), then assign it to a cluster
		while (index < this.numberRecords) {
			if (this.clusters[index] == -1) {
				this.assignCluster(index, clusterName);
				clusterName = clusterName + 1;
			}

			index = index + 1;
		}
	}

	/**
	 * Computes the sum squared error of the values in a cluster compared to its
	 * centroid.
	 *
	 * @param clusterNumber
	 *            of the cluster to compute the error
	 * @return the total sum squared distance of the given cluster
	 */
	public double computeSumSquaredErrorOfCluster(int clusterNumber) {
		ArrayList<Record> clusteredRecords = new ArrayList<>();

		// go through each record and check cluster
		for (int i = 0; i < this.clusters.length; i++) {
			if (this.clusters[i] == clusterNumber) {
				// gather together each of the records to compute the centroid
				clusteredRecords.add(this.records.get(i));
			}
		}

		Record centroid = this.getCentroid(clusteredRecords);

		double sum = 0.0;

		// find the squared sum of the distances
		for (Record record : clusteredRecords) {
			final double distance = this.distance(record, centroid);
			sum += distance * distance;
		}

		return sum;
	}

	/**
	 * Prints the clustered records along with cluster name to an output file.
	 *
	 * @param outputFile
	 * @throws IOException
	 */
	public void display(String outputFile) throws IOException {
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		writeRecordsOrderedByCluster(outFile);

		outFile.close();
	}

	/**
	 * Prints the clustered records along with cluster name to an output file.
	 *
	 * @param outputFile
	 * @throws IOException
	 */
	public void display(String outputFile, boolean ordered) throws IOException {
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		if (ordered)
			writeRecordsOrderedByCluster(outFile);
		else
			writeRecordsUnordered(outFile);

		outFile.close();
	}

	/**
	 * Writes each record individually, ordered by each cluster where it appears
	 * 
	 * @param outFile
	 */
	private void writeRecordsOrderedByCluster(PrintWriter outFile) {
		for (int i = 0; i < this.getNumClusters(); i++) {
			outFile.println(String.format("==== Cluster %d ==========", (i + 1)));

			for (int j = 0; j < clusters.length; j++) {
				if (i == clusters[j])
					outFile.println(this.records.get(j));
			}
			outFile.println();
		}

		this.writeErrorRates(outFile);
	}

	/**
	 * Writes each record and its cluster label
	 * 
	 * @param outFile
	 */
	private void writeRecordsUnordered(PrintWriter outFile) {
		for (int i = 0; i < this.numberRecords; i++) {
			for (int j = 0; j < this.numberAttributes; j++) {
				outFile.print(this.records.get(i).attributes[j] + " ");
			}

			outFile.println(this.clusters[i] + 1);
		}

		this.writeErrorRates(outFile);
	}

	/**
	 * Writes the error rates for each cluster
	 * 
	 * @param outFile
	 */
	private void writeErrorRates(PrintWriter outFile) {
		int numClusters = this.getNumClusters();
		for (int cluster = 0; cluster < numClusters; cluster++) {
			outFile.println(String.format("Error rate for cluster %d: %.5f", cluster + 1,
					this.computeSumSquaredErrorOfCluster(cluster)));
		}
	}

	/**
	 * Gets the number of clusters that a given clustering produced.
	 *
	 * @return the number of clusters
	 */
	public int getNumClusters() {
		Set<Integer> clusterOptions = new HashSet<>();

		for (int i = 0; i < this.clusters.length; i++) {
			clusterOptions.add(this.clusters[i]);
		}

		return clusterOptions.size();
	}

	/**
	 * Loads from a given input file a number of records to cluster.
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
				attributes[j] = inFile.nextDouble();
			}

			Record record = new Record(attributes);

			this.records.add(record);
		}

		inFile.close();
	}

	/**
	 * Sets the default parameters for the clusterer, the only optional
	 * parameter is the delta
	 *
	 * @param delta
	 *            the least acceptable distance to be considered neighbors
	 */
	public void setParameters(double delta) {
		this.delta = delta;
	}

	/**
	 * Private method to assign the cluster name to a record and all of that
	 * record's neighbors (and all of those record's neighbors, etc).
	 *
	 * @param index
	 *            the location of the record to assign a cluster
	 * @param clusterName
	 *            the cluster name to be assigned to a record
	 */
	private void assignCluster(int index, int clusterName) {
		this.clusters[index] = clusterName;

		// create a queue of records to be assigned a cluster name
		LinkedList<Integer> list = new LinkedList<Integer>();

		list.addLast(index);

		while (!list.isEmpty()) {
			int i = list.removeFirst();

			// for each record
			for (int j = 0; j < this.numberRecords; j++) {
				// if the record is a neighbor and the record doesn't have a
				// cluster name yet, set the cluster name and add the record to
				// the queue
				if ((this.matrix[i][j] == 1) && (this.clusters[j] == -1)) {
					this.clusters[j] = clusterName;
					list.addLast(j);
				}
			}
		}
	}

	/**
	 * Initializes and creates the matrix of neighbor recognition.
	 */
	private void createMatrix() {
		this.matrix = new int[this.numberRecords][this.numberRecords];

		for (int i = 0; i < this.numberRecords; i++) {
			for (int j = 0; j < this.numberRecords; j++) {
				this.matrix[i][j] = this.neighbor(this.records.get(i), this.records.get(j));
			}
		}
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
	 * Computes the centroid from a list of record objects.
	 *
	 * @param records
	 *            a list of record objects
	 * @return the centroid (or average) record of all the given records
	 */
	private Record getCentroid(List<Record> records) {
		double[] attributes = new double[this.numberAttributes];

		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = 0.0;
		}

		// compute the sum of each attribute
		for (Record record : records) {
			for (int i = 0; i < record.attributes.length; i++) {
				attributes[i] += record.attributes[i];
			}
		}

		// divide by the number of records for average
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] /= records.size();
		}

		return new Record(attributes);
	}

	/**
	 * Initializes all the clusters names to '-1'; aka, currently unclustered.
	 */
	private void initializeClusters() {
		this.clusters = new int[this.numberRecords];

		for (int i = 0; i < this.numberRecords; i++) {
			this.clusters[i] = -1;
		}
	}

	/**
	 * Based on the delta value, determines if the distance between two records
	 * is small enough to be considered neighbors.
	 *
	 * @param u
	 *            record one
	 * @param v
	 *            record two
	 * @return 1 if the two records are close enough to be neighbors, 0 if not
	 */
	private int neighbor(Record u, Record v) {
		double distance = 0.0;

		for (int i = 0; i < u.attributes.length; i++) {
			distance += (u.attributes[i] - v.attributes[i]) * (u.attributes[i] - v.attributes[i]);
		}

		distance = Math.sqrt(distance);

		if (distance <= this.delta) {
			return 1;
		} else {
			return 0;
		}
	}
}

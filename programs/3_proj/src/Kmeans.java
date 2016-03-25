
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Kmeans {
	private class Record {
		private final double[] attributes;

		private Record(double[] attributes) {
			this.attributes = attributes;
		}
	}

	private ArrayList<Record> centroids;

	private int[] clusters;

	private int numberAttributes;
	private int numberClusters;
	private int numberRecords;

	private Random rand;
	private ArrayList<Record> records;

	public Kmeans() {
		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClusters = 0;

		this.records = null;
		this.centroids = null;
		this.clusters = null;
		this.rand = null;
	}

	public void cluster() {
		this.initializeClusters();
		this.initializeCentroids();

		boolean stopCondition = false;

		while (!stopCondition) {
			int clusterChanges = this.assignClusters();

			this.updateCentroids();

			stopCondition = clusterChanges == 0;
		}
	}

	public void display(String outputFile) throws IOException {
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		for (int i = 0; i < this.numberRecords; i++) {
			for (int j = 0; j < this.numberAttributes; j++) {
				outFile.print(this.records.get(i).attributes[j] + " ");
			}

			outFile.println(this.clusters[i] + 1);
		}

		outFile.close();
	}

	public void load(String inputFile) throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(inputFile));

		this.numberRecords = inFile.nextInt();
		this.numberAttributes = inFile.nextInt();

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

	public void setParameters(int numberClusters, int seed) {
		this.numberClusters = numberClusters;

		this.rand = new Random(seed);
	}

	private int assignClusters() {
		int clusterChanges = 0;

		for (int i = 0; i < this.numberRecords; i++) {
			Record record = this.records.get(i);

			double minDistance = this.distance(record, this.centroids.get(0));
			int minIndex = 0;

			for (int j = 0; j < this.numberClusters; j++) {
				double distance = this.distance(record, this.centroids.get(j));

				if (distance < minDistance) {
					minDistance = distance;
					minIndex = j;
				}
			}

			if (this.clusters[i] != minIndex) {
				this.clusters[i] = minIndex;
				clusterChanges++;
			}
		}

		return clusterChanges;
	}

	private double distance(Record u, Record v) {
		double sum = 0;

		for (int i = 0; i < u.attributes.length; i++) {
			sum += (u.attributes[i] - v.attributes[i])
					* (u.attributes[i] - v.attributes[i]);
		}

		return sum;
	}

	private void initializeCentroids() {
		this.centroids = new ArrayList<Record>();

		for (int i = 0; i < this.numberClusters; i++) {
			int index = this.rand.nextInt(this.numberRecords);

			this.centroids.add(this.records.get(index));
		}
	}

	private void initializeClusters() {
		this.clusters = new int[this.numberRecords];

		for (int i = 0; i < this.numberRecords; i++) {
			this.clusters[i] = -1;
		}
	}

	private Record scale(Record u, double k) {
		double[] result = new double[u.attributes.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = k * u.attributes[i];
		}

		return new Record(result);
	}

	private Record sum(Record u, Record v) {
		double[] result = new double[u.attributes.length];

		for (int i = 0; i < u.attributes.length; i++) {
			result[i] = u.attributes[i] + v.attributes[i];
		}

		return new Record(result);
	}

	private void updateCentroids() {
		ArrayList<Record> clusterSum = new ArrayList<Record>();

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

		for (int i = 0; i < this.numberRecords; i++) {
			int cluster = this.clusters[i];

			Record sum = this.sum(clusterSum.get(cluster), this.records.get(i));
			clusterSum.set(cluster, sum);

			clusterSize[cluster] += 1;
		}

		for (int i = 0; i < this.numberClusters; i++) {
			Record average =
					this.scale(clusterSum.get(i), 1.0 / clusterSize[i]);

			this.centroids.set(i, average);
		}
	}
}

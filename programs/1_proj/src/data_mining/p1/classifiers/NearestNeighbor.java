package data_mining.p1.classifiers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import data_mining.p1.converters.NearestNeighborDataConverterInterface;
import data_mining.p1.converters.StudentQualityNearestNeighborDataConverter;

/**
 * Decision tree class, original code presented by Dr Suchindran Maniccam.
 * Modified and expanded by Eddie Gurnee for use in Data Mining project 1.
 *
 * @author smaniccam
 * @author eddie
 *
 */
public class NearestNeighbor {

	private class Record {
		private final double[] attributes;
		private final int className;

		private Record(double[] attributes, int className) {
			this.attributes = attributes;
			this.className = className;
		}

		@Override
		public String toString() {
			return "Record [attributes="+ Arrays.toString(this.attributes)
					+ ", className=" + this.className + "]";
		}

	}

	public static void main(String[] args) throws IOException {
		String inFolder = "in/", outFolder = "out/";
		String trainingFile = inFolder + "train3",
				testingFile = inFolder + "test3",
				validationFile = inFolder + "valid3",
				classifiedFile = outFolder + "classified3";

		NearestNeighbor moore = new NearestNeighbor();

		moore.loadTrainingData(trainingFile);

		moore.classifyData(testingFile, classifiedFile);
		System.out.println("The training error: "
							+ moore.determineTrainingError(trainingFile) + "%");

		System.out.println("Leave one out validation error: "
							+ moore.validateWithLeaveOneOut(trainingFile)
							+ "%");
	}

	private String[] attributeTypes;

	private final NearestNeighborDataConverterInterface converter;
	private String distanceMeasure;
	private String majorityRule;

	private int numberAttributes;
	private int numberClasses;
	private int numberNeighbors;

	private int numberRecords;

	private ArrayList<Record> records;

	private boolean traceBuild;

	/**
	 * Response to question 1 part a
	 */
	public NearestNeighbor() {
		this(new StudentQualityNearestNeighborDataConverter());
	}

	public NearestNeighbor(NearestNeighborDataConverterInterface converter) {
		this.records = null;
		this.attributeTypes = null;

		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClasses = 0;

		this.numberNeighbors = 0;
		this.distanceMeasure = null;
		this.majorityRule = null;

		this.traceBuild = false;

		this.converter = converter;
	}

	/**
	 * Response to question 1 part c
	 *
	 * @param testFile
	 * @param classifiedFile
	 * @throws IOException
	 */
	public void classifyData(String testFile, String classifiedFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));

		// lets watch the build
		this.traceBuild = true;

		int numberRecords = inFile.nextInt();
		for (int i = 0; i < numberRecords; i++) {
			double[] attributeArray = new double[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			int className = this.classify(attributeArray);

			String label = this.convert(className);
			outFile.println(label);
		}

		inFile.close();
		outFile.close();

		// lets stop tracing
		this.traceBuild = false;
	}

	/**
	 * Determines the level of errors in classifying the training records. Funny
	 * story, but with a weighted majority rule, there will probably never be
	 * any training errors.
	 *
	 * @param trainingFile
	 * @return
	 * @throws IOException
	 */
	public double determineTrainingError(String trainingFile)
			throws IOException {
		if (this.records.isEmpty()) {
			this.loadTrainingData(trainingFile);
		}
		Scanner inFile = new Scanner(new File(trainingFile));
		int numberOfIncorrectlyClassifiedRecords = 0, numberOfHeaderLines = 3;

		for (int i = 0; i < numberOfHeaderLines; i++) {
			inFile.nextLine(); // ignore the first few lines (headers)
		}

		for (int i = 0; i < this.numberRecords; i++) {
			double[] attributeArray = new double[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			// retrieve the actual class name
			String label = inFile.next();
			double expectedClassName =
					this.convert(label, this.numberAttributes + 1);

			// determine what the class name would have been
			int actualClassName = this.classify(attributeArray);

			if (expectedClassName != actualClassName) {
				numberOfIncorrectlyClassifiedRecords++;
			}
		}

		inFile.close();

		double trainingError = (numberOfIncorrectlyClassifiedRecords * 100)
								/ (double) this.numberRecords;
		return trainingError;
	}

	/**
	 * Response to question 1 part b
	 *
	 * @param trainingFile
	 * @throws IOException
	 */
	public void loadTrainingData(String trainingFile) throws IOException {
		Scanner inFile = new Scanner(new File(trainingFile));

		this.numberRecords = inFile.nextInt();
		this.numberAttributes = inFile.nextInt();
		this.numberClasses = inFile.nextInt();

		this.numberNeighbors = inFile.nextInt();
		this.distanceMeasure = inFile.next();
		this.majorityRule = inFile.next();

		this.attributeTypes = new String[this.numberAttributes];
		for (int i = 0; i < this.numberAttributes; i++) {
			this.attributeTypes[i] = inFile.next();
		}

		// need to bring in all of the metadata about the different attributes
		// oh wait, no i don't that's if the header files looked different and I
		// wanted to construct the converters in realtime

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			double[] attributeArray = new double[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			String label = inFile.next();
			int className =
					(int) this.convert(label, this.numberAttributes + 1);

			Record record = new Record(attributeArray, className);
			this.records.add(record);
		}

		inFile.close();
	}

	/**
	 * This is what it has come to. This monstrosity.
	 *
	 * @param trainingFile
	 * @throws IOException
	 */
	public void optimizeNeighbors(String trainingFile) throws IOException {
		this.loadTrainingData(trainingFile);

		final int size = this.records.size();
		double[] rates = new double[size];
		for (int i = 0; i < size; i++) {
			this.numberNeighbors = i;

			rates[i] = this.validateWithLeaveOneOut();
		}

		int minIndex = 0;
		for (int i = 1; i < rates.length; i++) {
			if (rates[i] < rates[minIndex]) {
				minIndex = i;
			}
		}

		System.out.println("The optimal number for weighted is: "+ minIndex
							+ "\n\tat a validation error of "
							+ rates[minIndex]);
	}

	public void validate(String validationFile) throws IOException {
		Scanner inFile = new Scanner(new File(validationFile));

		int numberRecords = inFile.nextInt();
		int numberErrors = 0;

		for (int i = 0; i < numberRecords; i++) {
			double[] attributeArray = new double[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			int predictedClass = this.classify(attributeArray);
			String label = inFile.next();
			int actualClass =
					(int) this.convert(label, this.numberAttributes + 1);

			if (predictedClass != actualClass) {
				numberErrors += 1;
			}
		}

		double errorRate = (100.0 * numberErrors) / numberRecords;
		System.out.println(errorRate + " percent error");

		inFile.close();
	}

	/**
	 * Given the training file, runs the LeaveOneOut validation method on the
	 * data
	 *
	 * @param trainingFile
	 * @return
	 * @throws IOException
	 */
	public double validateWithLeaveOneOut(String trainingFile)
			throws IOException {

		this.loadTrainingData(trainingFile);

		return this.validateWithLeaveOneOut();
	}

	private int classify(double[] attributes) {
		double[] distance = new double[this.numberRecords];
		int[] id = new int[this.numberRecords];

		if (this.traceBuild) {
			System.out.println(
				"Classifying record: " + Arrays.toString(attributes));
			System.out.println("Comparative distances: ");
		}
		for (int i = 0; i < this.records.size(); i++) {
			distance[i] =
					this.distance(attributes, this.records.get(i).attributes);
			id[i] = i;
			if (this.traceBuild) {
				System.out.println(String.format("  %-14.14s | %s", distance[i],
					Arrays.toString(this.records.get(i).attributes)));
			}
		}

		this.nearestNeighbor(distance, id);
		if (this.traceBuild) {
			System.out.println("The neighbors that are nearest: ");
			for (int i = 0; i < this.numberNeighbors; i++) {
				System.out.println(String.format("  %-14.14s | %s", distance[i],
					Arrays.toString(this.records.get(id[i]).attributes)));
			}
		}
		int className = this.majority(id, attributes);

		if (this.traceBuild) {
			System.out.println(
				"The class assigned is: " + this.convert(className) + "\n");
		}

		return className;
	}

	private String convert(int value) {
		return this.converter.convertFromNumericalValue(value);
	}

	private double convert(String label, int column) {
		return this.converter.convertToNumericalValue(label, column);
	}

	private double distance(double[] u, double[] v) {
		double distance = 0;

		if (this.distanceMeasure.equals("euclidean")) {
			double sum = 0;
			for (int i = 0; i < u.length; i++) {
				sum = sum + ((u[i] - v[i]) * (u[i] - v[i]));
			}

			distance = Math.sqrt(sum);
		} else if (this.distanceMeasure.equals("matching")) {
			int matches = 0;
			for (int i = 0; i < u.length; i++) {
				if ((int) u[i] == (int) v[i]) {
					matches = matches + 1;
				}
			}

			distance = 1 - ((double) matches / u.length);
		} else if (this.distanceMeasure.equals("heterogeneous")) {
			double sum = 0;
			double dist = 0;

			for (int i = 0; i < u.length; i++) {
				if (this.attributeTypes[i].equals("binary")
					|| this.attributeTypes[i].equals("nominal")) {
					if ((int) u[i] == (int) v[i]) {
						dist = 0;
					} else {
						dist = 1;
					}
				}
				if (this.attributeTypes[i].equals("ordinal")
					|| this.attributeTypes[i].equals("continuous")) {
					dist = u[i] - v[i];
				}

				sum = sum + (dist * dist);
			}

			distance = Math.sqrt(sum);
		}

		return distance;
	}

	private int majority(int[] id, double[] attributes) {
		double[] frequency = new double[this.numberClasses];
		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] = 0;
		}

		if (this.majorityRule.equals("unweighted")) {
			for (int i = 0; i < this.numberNeighbors; i++) {
				frequency[this.records.get(id[i]).className - 1] += 1;
			}
		} else {
			for (int i = 0; i < this.numberNeighbors; i++) {
				double d = this.distance(this.records.get(id[i]).attributes,
					attributes);
				frequency[this.records.get(id[i]).className - 1] +=
						1 / (d + 0.001);
			}
		}

		int maxIndex = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			if (frequency[i] > frequency[maxIndex]) {
				maxIndex = i;
			}
		}

		return maxIndex + 1;
	}

	private void nearestNeighbor(double[] distance, int[] id) {
		for (int i = 0; i < this.numberNeighbors; i++) {
			for (int j = i; j < this.numberRecords; j++) {
				if (distance[i] > distance[j]) {
					double tempDistance = distance[i];
					distance[i] = distance[j];
					distance[j] = tempDistance;

					int tempId = id[i];
					id[i] = id[j];
					id[j] = tempId;
				}
			}
		}
	}

	private double validateWithLeaveOneOut() {
		int numberInvalid = 0;
		for (int i = 0; i < this.numberRecords; i++) {
			Record theOneBeingLeftOut = this.records.remove(0);

			int actualClassName = this.classify(theOneBeingLeftOut.attributes);
			if (actualClassName != theOneBeingLeftOut.className) {
				numberInvalid++;
			}

			this.records.add(theOneBeingLeftOut);
		}

		return ((numberInvalid * 100.0) / (this.numberRecords - 1));
	}
}

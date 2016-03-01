package data_mining.p1.classifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import data_mining.p1.converters.NearestImageDataConverter;

/**
 * Decision tree class, original code presented by Dr Suchindran Maniccam.
 * Modified and expanded by Eddie Gurnee for use in Data Mining project 1.
 *
 * @author smaniccam
 * @author eddie
 *
 */
public class NearestImageNeighbor {

	private class Record {
		private final boolean[][] attributes;
		private final int className;

		private Record(boolean[][] attributes, int className) {
			this.attributes = attributes;
			this.className = className;
		}

		@Override
		public String toString() {
			return "Record [attributes="+ Arrays.toString(this.attributes)
					+ ", className=" + this.className + "]";
		}

	}

	private final static int SIZE_OF_IMAGE = 20;

	public static void main(String[] args) throws IOException {
		String inFolder = "in/", outFolder = "out/";
		String trainingFile = inFolder + "formatted.txt",
				testingFile = inFolder + "test3",
				classifiedFile = outFolder + "imagey";
		String[] testingFiles = { "i1", "i2", "i3", "i4", "i5", "i6", "i7",
				"i8", "i9", "i10", "i11", "i12", "i13", "i14", "i15", "i16",
				"i17", "i18", "i19", "i20", "i21", "i22", "i23", "i24", "i25" };

		NearestNeighbor moore = new NearestNeighbor();

		moore.loadTrainingData(trainingFile);

		moore.classifyData(testingFile, classifiedFile);

		System.out.println("Leave one out validation error: "
							+ moore.validateWithLeaveOneOut(trainingFile)
							+ "%");
	}

	private final NearestImageDataConverter converter;
	private final String distanceMeasure;
	private final String majorityRule;

	private final int numberAttributes;
	private final int numberClasses;
	private int numberNeighbors;

	private final int numberRecords;

	private ArrayList<Record> records;

	private boolean traceBuild;

	/**
	 * Response to question 1 part a
	 */
	public NearestImageNeighbor() {
		this(new NearestImageDataConverter());
	}

	public NearestImageNeighbor(NearestImageDataConverter converter) {
		this.records = null;

		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClasses = 0;

		this.numberNeighbors = 0;
		this.distanceMeasure = null;
		this.majorityRule = null;

		this.traceBuild = false;

		this.converter = converter;
	}

	public void classifyData(String testFile, String classifiedFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));

		// lets watch the build
		this.traceBuild = true;

		boolean[][] imageArray = this.getImageArray(inFile);

		int className = this.classify(imageArray);

		outFile.println(className);

		inFile.close();
		outFile.close();

		// lets stop tracing
		this.traceBuild = false;
	}

	public void loadImageTrainingData(String trainingFile)
			throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(trainingFile));

		this.records = new ArrayList<Record>();

		while (inFile.hasNext()) {
			int className = inFile.nextInt();
			boolean[][] newImage = this.getImageArray(inFile);

			Record record = new Record(newImage, className);
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
		this.loadImageTrainingData(trainingFile);

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

		this.loadImageTrainingData(trainingFile);

		return this.validateWithLeaveOneOut();
	}

	private int classify(boolean[][] attributes) {
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
			System.out.println("The class assigned is: " + className + "\n");
		}

		return className;
	}

	private boolean convert(char value) {
		return this.converter.convertFromCharacterValue(value);
	}

	private double distance(boolean[][] u, boolean[][] v) {
		double distance = 0;

		if (this.distanceMeasure.equals("jacard")) {
			int matches = 0, counted = 0;

			for (int r = 0; r < v.length; r++) {
				for (int c = 0; c < v[r].length; c++) {
					if (v[r][c] || u[r][c]) {
						counted++;
					}
					if (v[r][c] && u[r][c]) {
						matches++;
					}
				}
			}

			distance = 1 - (matches / (double) counted);

		} else if (this.distanceMeasure.equals("matching")) {
			int matches = 0;
			for (int r = 0; r < v.length; r++) {
				for (int c = 0; c < v[r].length; c++) {
					if (u[r][c] == v[r][c]) {
						matches++;
					}
				}
			}

			distance = 1 - ((double) matches / u.length);
		} else {

		}

		return distance;
	}

	private boolean[][] getImageArray(Scanner inFile) {
		boolean[][] imageArray = new boolean[SIZE_OF_IMAGE][SIZE_OF_IMAGE];

		for (int r = 0; r < SIZE_OF_IMAGE; r++) {
			String line = inFile.next();
			for (int c = 0; c < line.length(); c++) {
				imageArray[r][c] = this.convert(line.charAt(c));
			}
		}
		return imageArray;
	}

	private int majority(int[] id, boolean[][] attributes) {
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

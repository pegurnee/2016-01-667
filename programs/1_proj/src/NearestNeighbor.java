import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
	}

	public static void main(String[] args) throws IOException {
		String inFolder = "in/", outFolder = "out/";
		String trainingFile = inFolder + "train3",
				testingFile = inFolder + "test3",
				classifiedFile = outFolder + "classified3";

		NearestNeighbor moore = new NearestNeighbor();

		moore.loadTrainingData(trainingFile);

		moore.classifyData(testingFile, classifiedFile);

		moore.validate(trainingFile);
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

	/**
	 * Response to question 1 part a
	 */
	public NearestNeighbor() {
		this(new ExLoanRiskNearestNeighborDataConverter());

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

	private int classify(double[] attributes) {
		double[] distance = new double[this.numberRecords];
		int[] id = new int[this.numberRecords];

		for (int i = 0; i < this.numberRecords; i++) {
			distance[i] =
					this.distance(attributes, this.records.get(i).attributes);
			id[i] = i;
		}

		this.nearestNeighbor(distance, id);
		int className = this.majority(id, attributes);

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
}


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Naive bayes classifier, original code presented by Dr Suchindran Maniccam.
 * Modified and expanded by Eddie Gurnee for use in Data Mining project 2.
 *
 * @author smaniccam
 * @author eddie
 *
 */
public class BayesClassifier {
	private class Record {
		private final int[] attributes;
		private final int className;

		private Record(int[] attributes, int className) {
			this.attributes = attributes;
			this.className = className;
		}
	}

	private int[] attributeValues;

	private int numberAttributes;
	private int numberClasses;
	private int numberRecords;

	private ArrayList<Record> records;

	double[] classTable;
	double[][][] table;

	public BayesClassifier() {
		this.records = null;
		this.attributeValues = null;

		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClasses = 0;

		this.classTable = null;
		this.table = null;
	}

	public void buildModel() {
		this.fillClassTable();

		this.fillProbabilityTable();
	}

	public void classifyData(String testFile, String classifiedFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));

		int numberRecords = inFile.nextInt();

		for (int i = 0; i < numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

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

	public void loadTrainingData(String trainingFile) throws IOException {
		Scanner inFile = new Scanner(new File(trainingFile));

		this.numberRecords = inFile.nextInt();
		this.numberAttributes = inFile.nextInt();
		this.numberClasses = inFile.nextInt();

		this.attributeValues = new int[this.numberAttributes];
		for (int i = 0; i < this.numberAttributes; i++) {
			this.attributeValues[i] = inFile.nextInt();
		}

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			String label = inFile.next();
			int className = this.convert(label, this.numberAttributes);

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
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			int predictedClass = this.classify(attributeArray);

			String label = inFile.next();
			int actualClass = this.convert(label, this.numberAttributes + 1);

			if (predictedClass != actualClass) {
				numberErrors += 1;
			}
		}

		double errorRate = (100.0 * numberErrors) / numberRecords;
		System.out.println(errorRate + " percent error");

		inFile.close();
	}

	private int classify(int[] attributes) {
		double maxProbability = 0.0;
		int maxClass = -1;

		for (int i = 0; i < this.numberClasses; i++) {
			double probability = this.findProbability(i + 1, attributes);

			if (probability > maxProbability) {
				maxProbability = probability;
				maxClass = i;
			}
		}

		return maxClass + 1;
	}

	private String convert(int value) {
		String label;

		if (value == 1) {
			label = "highrisk";
		} else if (value == 2) {
			label = "mediumrisk";
		} else if (value == 3) {
			label = "lowrisk";
		} else {
			label = "undetermined";
		}

		return label;
	}

	private int convert(String label, int column) {
		int value;

		if (column == 1) {
			if (label.equals("college")) {
				value = 1;
			} else {
				value = 2;
			}
		} else if (column == 2) {
			if (label.equals("smoker")) {
				value = 1;
			} else {
				value = 2;
			}
		} else if (column == 3) {
			if (label.equals("married")) {
				value = 1;
			} else {
				value = 2;
			}
		} else if (column == 4) {
			if (label.equals("male")) {
				value = 1;
			} else {
				value = 2;
			}
		} else if (column == 5) {
			if (label.equals("works")) {
				value = 1;
			} else {
				value = 2;
			}
		} else if (label.equals("highrisk")) {
			value = 1;
		} else if (label.equals("mediumrisk")) {
			value = 2;
		} else if (label.equals("lowrisk")) {
			value = 3;
		} else {
			value = 4;
		}

		return value;
	}

	private void fill(int attribute) {
		int attributeValues = this.attributeValues[attribute - 1];

		this.table[attribute - 1] =
				new double[this.numberClasses][attributeValues];

		for (int i = 0; i < this.numberClasses; i++) {
			for (int j = 0; j < attributeValues; j++) {
				this.table[attribute - 1][i][j] = 0;
			}
		}

		for (int k = 0; k < this.numberRecords; k++) {
			int i = this.records.get(k).className - 1;
			int j = this.records.get(k).attributes[attribute - 1] - 1;
			this.table[attribute - 1][i][j] += 1;
		}

		for (int i = 0; i < this.numberClasses; i++) {
			for (int j = 0; j < attributeValues; j++) {
				double value = (this.table[attribute - 1][i][j] + 1)
								/ ((this.classTable[i] * this.numberRecords)
									+ attributeValues);
				this.table[attribute - 1][i][j] = value;
			}
		}
	}

	private void fillClassTable() {
		this.classTable = new double[this.numberClasses];

		for (int i = 0; i < this.numberClasses; i++) {
			this.classTable[i] = 0;
		}

		for (int i = 0; i < this.numberRecords; i++) {
			this.classTable[this.records.get(i).className - 1] += 1;
		}

		for (int i = 0; i < this.numberClasses; i++) {
			this.classTable[i] /= this.numberRecords;
		}
	}

	private void fillProbabilityTable() {
		this.table = new double[this.numberAttributes][][];

		for (int i = 0; i < this.numberAttributes; i++) {
			this.fill(i + 1);
		}
	}

	private double findProbability(int className, int[] attributes) {
		double value;
		double product = 1;

		for (int i = 0; i < this.numberAttributes; i++) {
			value = this.table[i][className - 1][attributes[i] - 1];
			product = product * value;
		}

		return product * this.classTable[className - 1];
	}
}

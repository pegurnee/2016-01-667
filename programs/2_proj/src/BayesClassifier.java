
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

	public static void main(String[] args) throws IOException {
		String inFolder = "in/part1/", outFolder = "out/part1/";
		String trainingFile = inFolder + "train1",
				testingFile = inFolder + "test1",
				classifiedFile = outFolder + "classified1";
		BayesClassifier classifier = new BayesClassifier();
		classifier.loadTrainingData(trainingFile);
		for (Record r : classifier.records) {
			System.out.println(
				Arrays.toString(r.attributes) + ": " + r.className);
		}
		classifier.buildModel();

		classifier.displayProbabilityTables();

		classifier.classifyData(testingFile, classifiedFile);

	}

	private int[] attributeValues;
	private final DataConverterInterface converter;
	private int numberAttributes;

	private int numberClasses;

	private int numberRecords;

	private ArrayList<Record> records;
	double[] classTable;

	double[][][] table;

	/**
	 * answer to section 1 question 1 part a
	 */
	public BayesClassifier() {
		this(new IntegerDataConverter());
	}

	public BayesClassifier(DataConverterInterface converter) {
		this.records = null;
		this.attributeValues = null;

		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClasses = 0;

		this.classTable = null;
		this.table = null;

		this.converter = converter;
	}

	/**
	 * answer to section 1 question 1 part c
	 */
	public void buildModel() {
		this.fillClassTable();

		this.fillProbabilityTable();
	}

	/**
	 * answer to section 1 question 1 part d
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
			int[] attributeArray = this.loadIndividualRecordData(inFile);

			int className = this.classify(attributeArray);
			double confidence = this.confidenceLevel(className, attributeArray);

			String label = this.convert(className);
			outFile.println(String.format("%-20s %f", label, confidence));
		}

		inFile.close();
		outFile.close();
	}

	/**
	 * answer to section 1 question 1 part a
	 *
	 * @param trainingFile
	 * @return
	 * @throws IOException
	 */
	public double computeTrainingError() {
		int numberErrors = 0;

		for (Record r : this.records) {
			int predictedClass = this.classify(r.attributes);

			if (r.className != predictedClass) {
				numberErrors += 1;
			}
		}

		return (100.0 * numberErrors) / this.records.size();
	}

	/**
	 * answer for section 1 question 2 part d
	 */
	public void displayProbabilityTables() {
		StringBuilder response = new StringBuilder();
		if ((this.table == null) || (this.classTable == null)) {
			response.append("Model not yet built");
		} else {
			for (int att = 0; att < this.table.length; att++) {
				response.append("\tAttribute column: " + (att + 1) + "\n");
				for (int cla = 0; cla < this.table[att].length; cla++) {
					for (int val =
							0; val < this.table[att][cla].length; val++) {
						response.append(String.format("%-20s", String.format(
							"P(%s|%s):", val, this.convert(cla + 1))));
						response.append(String.format("%10.6f ",
							this.table[att][cla][val]));
					}
					response.append("\n");
				}
				response.append("\n");
			}
			response.append("\tClass probabilities:\n");
			for (int i = 0; i < this.classTable.length; i++) {
				response.append(String.format("%-10s%-10f\n",
					String.format("P(%s):", this.convert(i + 1)),
					this.classTable[i]));
			}
		}
		System.out.println(response.toString());
	}

	/**
	 * answer to section 1 question 1 part b
	 *
	 * @param trainingFile
	 * @throws IOException
	 */
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
			int[] attributeArray = this.loadIndividualRecordData(inFile);

			String label = inFile.next();
			int className = this.convert(label, this.numberAttributes + 1);

			Record record = new Record(attributeArray, className);

			this.records.add(record);
		}

		inFile.close();
	}

	public void validate(String validationFile) throws IOException {
		double errorRate = this.computeValidationError(validationFile);
		System.out.println(errorRate + " percent error");
	}

	/**
	 * answer to section 1 question 2 part b
	 *
	 * @param trainingFile
	 * @return
	 * @throws IOException
	 */
	public double validateWithLeaveOneOut(String trainingFile)
			throws IOException {
		this.loadTrainingData(trainingFile);

		int numberInvalid = 0;
		for (int i = 0; i < this.numberRecords; i++) {
			Record theOneBeingLeftOut = this.records.remove(0);

			this.buildModel();

			int actualClassName = this.classify(theOneBeingLeftOut.attributes);
			if (actualClassName != theOneBeingLeftOut.className) {
				numberInvalid++;
			}

			this.records.add(theOneBeingLeftOut);
		}
		return (numberInvalid / (double) (this.numberRecords - 1));
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

	private double computeValidationError(String validationFile)
			throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(validationFile));

		int numberRecords = inFile.nextInt();

		System.out.println(inFile.nextLine());
		inFile.next();
		System.out.println(inFile.nextLine());

		int numberErrors = 0;

		for (int i = 0; i < numberRecords; i++) {
			int[] attributeArray = this.loadIndividualRecordData(inFile);

			int predictedClass = this.classify(attributeArray);

			String label = inFile.next();
			int actualClass = this.convert(label, this.numberAttributes + 1);

			if (predictedClass != actualClass) {
				numberErrors += 1;
			}
		}

		double errorRate = (100.0 * numberErrors) / numberRecords;

		inFile.close();
		return errorRate;
	}

	private double confidenceLevel(int className, int[] attributes) {
		// report confidence of classification decision which is measured
		// by the conditional probability of the class given a record as percent
		// of the total conditional probabilities of all classes given that
		// record
		// p(chosen class) / p(any class)
		double pClassChoosen = this.findProbability(className, attributes);
		double pAnyClass = 0.0;

		for (int i = 0; i < this.numberClasses; i++) {
			pAnyClass += this.findProbability(i + 1, attributes);
		}
		return pClassChoosen / pAnyClass;
	}

	private String convert(int value) {
		return this.converter.convertFromNumericalValue(value, -1);
	}

	private int convert(String label, int column) {
		return this.converter.convertToNumericalValue(label, column);
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

		for (int k = 0; k < this.records.size(); k++) {
			int i = this.records.get(k).className - 1;
			int j = this.records.get(k).attributes[attribute - 1] - 1;
			this.table[attribute - 1][i][j] += 1;
		}

		for (int i = 0; i < this.numberClasses; i++) {
			for (int j = 0; j < attributeValues; j++) {
				// this is computing the laplace adjusted value
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

		for (int i = 0; i < this.records.size(); i++) {
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

	private int[] loadIndividualRecordData(Scanner inFile) {
		int[] attributeArray = new int[this.numberAttributes];

		for (int j = 0; j < this.numberAttributes; j++) {
			String label = inFile.next();
			attributeArray[j] = this.convert(label, j + 1);
		}
		return attributeArray;
	}
}

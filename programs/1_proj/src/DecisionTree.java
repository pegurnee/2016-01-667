import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Decision tree class, original code presented by Dr Suchindran Maniccam.
 * Modified and expanded by Eddie Gurnee for use in Data Mining project 1.
 *
 * @author smaniccam
 * @author eddie
 *
 */

public class DecisionTree {
	private enum MethodOfEntropy {
		CLASSERROR, GINI, SHANNONS
	}

	private enum MethodOfValidation {
		LEAVEONEOUT, RANDOM
	}

	private class Node {
		private int className;
		private int condition;
		private ArrayList<Record> heldRecords;
		private Node left;
		private final String nodeType;
		private Node right;

		private Node(String nodeType, int value, Node left, Node right) {
			this.nodeType = nodeType;
			this.left = left;
			this.right = right;
			this.heldRecords = null;

			if (nodeType.equals("internal")) {
				this.className = -1;
				this.condition = value;
			} else {
				this.className = value;
				this.condition = -1;
			}
		}

		@Override
		public String toString() {
			return this.buildChildString(this, 0, -1);
		}

		/**
		 * Will return a beautifully formatted String of beauty
		 *
		 * @param node
		 * @param depth
		 * @param value
		 * @return
		 */
		private String buildChildString(Node node, int depth, int value) {
			String toReturn = "\n";
			for (int i = 0; i <= depth; i++) {
				toReturn += "   ";
			}
			if (value >= 0) {
				toReturn += value + ": ";
			}
			if ("internal" == node.nodeType) {
				toReturn += "attribute " + node.condition + "?";

				toReturn += this.buildChildString(node.left, depth + 1, 0);
				toReturn += this.buildChildString(node.right, depth + 1, 1);
			} else {
				toReturn += "class " + node.className;
			}
			return toReturn;
		}

		private void setRecords(ArrayList<Record> heldRecords) {
			this.heldRecords = heldRecords;
		}
	}

	private class Record {
		private final int[] attributes;
		private final int className;

		private Record(int[] attributes, int className) {
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
		boolean buildTrace = false;
		String inFolder = "in/", outFolder = "out/";
		String trainingFile = inFolder + "train1",
				testingFile = inFolder + "test1",
				classifiedFile = outFolder + "classified1";
		DecisionTree tree = new DecisionTree();
		tree.loadTrainingData(trainingFile);
		tree.buildTree(buildTrace);

		System.out.println("decision tree:" + tree.root);
		tree.classifyData(testingFile, classifiedFile);

		System.out.println(
			"Training Error: " + tree.determineTrainingError(trainingFile));

		System.out.println("Validation Error (random): " + tree.validate(
			trainingFile, MethodOfValidation.RANDOM));
		System.out.println("Validation Error (leave-one-out): " + tree.validate(
			trainingFile, MethodOfValidation.LEAVEONEOUT));
	}

	private ArrayList<Integer> attributes;
	private final MethodOfEntropy entropyStyle;
	private int numberAttributes;
	private int numberClasses;
	private int numberRecords;
	private ArrayList<Record> records;

	private Node root;

	private boolean traceBuild;

	/**
	 * answers to question 1 part a
	 */
	public DecisionTree() {
		this.root = null;
		this.records = null;
		this.attributes = null;
		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClasses = 0;

		this.entropyStyle = MethodOfEntropy.GINI;
		this.traceBuild = false;

	}

	public void buildTree(boolean traceBuild) {
		this.traceBuild = traceBuild;
		this.buildTree();
	}

	/**
	 * answer to question 1 part d
	 *
	 * @param testFile
	 * @param classifiedFile
	 * @throws IOException
	 */
	public void classifyData(String testFile, String classifiedFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));

		String classifyFileRecordFormat = "%-7s %-10s %-10s",
				classifyFileNumberFormat = "%2d / %-2d";

		outFile.println(
			String.format(classifyFileRecordFormat, "", "node", "class"));
		outFile.println(String.format(classifyFileRecordFormat, "class",
			"chance", "chance"));

		int numberRecords = inFile.nextInt();
		for (int i = 0; i < numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			Node destination = this.classifyGetNode(attributeArray);

			ArrayList<Record> destinationRecordSet = destination.heldRecords;
			double[] destinationFrequency =
					this.getFrequencyOfClasses(destinationRecordSet);

			// Convert to the class name
			String label = this.convert(destination.className,
				this.numberAttributes + 1);
			outFile.println(String.format(classifyFileRecordFormat, label,
				String.format(classifyFileNumberFormat,
					destinationRecordSet.size(), this.numberRecords),
				String.format(classifyFileNumberFormat,
					(int) destinationFrequency[destination.className - 1],
					destinationRecordSet.size())));
		}

		inFile.close();
		outFile.close();
	}

	/**
	 * Determines the training error of a given file.
	 *
	 * @param trainingFile
	 * @return
	 * @throws IOException
	 */
	public double determineTrainingError(String trainingFile)
			throws IOException {
		if (this.root == null) {
			this.loadTrainingData(trainingFile);
		}

		Scanner inFile = new Scanner(new File(trainingFile));
		int numberOfIncorrectlyClassifiedRecords = 0;

		inFile.nextLine(); // ignore the first line (headers)

		for (int i = 0; i < this.numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			// retrieve the actual class name
			String label = inFile.next();
			int expectedClassName =
					this.convert(label, this.numberAttributes + 1);

			// determine what the class name would have been
			int actualClassName = this.classify(attributeArray);

			if (expectedClassName != actualClassName) {
				numberOfIncorrectlyClassifiedRecords++;
			}
		}

		inFile.close();

		double trainingError = numberOfIncorrectlyClassifiedRecords
								/ (double) this.numberRecords;
		return trainingError;
	}

	/**
	 * Answers question 1 part b
	 *
	 * @param trainingFile
	 * @throws IOException
	 */
	public void loadTrainingData(String trainingFile) throws IOException {
		Scanner inFile = new Scanner(new File(trainingFile));

		this.numberRecords = inFile.nextInt();
		this.numberAttributes = inFile.nextInt();
		this.numberClasses = inFile.nextInt();

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			String label = inFile.next();
			int className = this.convert(label, this.numberAttributes + 1);

			Record record = new Record(attributeArray, className);

			this.records.add(record);
		}

		this.attributes = new ArrayList<Integer>();
		for (int i = 0; i < this.numberAttributes; i++) {
			this.attributes.add(i + 1);
		}

		inFile.close();
	}

	@Override
	public String toString() {
		return "DecisionTree [\nattributes="+ this.attributes
				+ ", \nnumberAttributes=" + this.numberAttributes
				+ ", \nnumberClasses=" + this.numberClasses
				+ ", \nnumberRecords=" + this.numberRecords + ", \nrecords="
				+ this.records + ", \nroot=" + this.root + "]";
	}

	public double validate(String trainingFile, MethodOfValidation choice)
			throws IOException {
		double validationError;

		switch (choice) {
			case RANDOM:
				int defaultSampleSize = 30;
				validationError = this.validateWithRandomSampling(trainingFile,
					defaultSampleSize);
				break;
			case LEAVEONEOUT:
				validationError = this.validateWithLeaveOneOut(trainingFile);
				break;
			default:
				validationError = -1;
		}

		return validationError;
	}

	private int bestCondition(ArrayList<Record> records,
			ArrayList<Integer> attributes) {
		double minValue = this.evaluate(records, attributes.get(0));
		int minIndex = 0;

		for (int i = 0; i < attributes.size(); i++) {
			double value = this.evaluate(records, attributes.get(i));

			if (value < minValue) {
				minValue = value;
				minIndex = i;
			}
		}

		return attributes.get(minIndex);
	}

	private Node build(ArrayList<Record> records,
			ArrayList<Integer> attributes) {
		Node node = null;

		if (this.traceBuild) {
			System.out.println("\n" + this.getState());
		}

		if (this.sameClass(records)) {
			int className = records.get(0).className;

			node = new Node("leaf", className, null, null);
			node.setRecords(records);
		} else if (attributes.isEmpty()) {
			int className = this.majorityClass(records);

			node = new Node("leaf", className, null, null);
			node.setRecords(records);
		} else {
			int condition = this.bestCondition(records, attributes);

			ArrayList<Record> leftRecords = this.collect(records, condition, 0);
			ArrayList<Record> rightRecords =
					this.collect(records, condition, 1);

			if (leftRecords.isEmpty() || rightRecords.isEmpty()) {
				int className = this.majorityClass(records);

				node = new Node("leaf", className, null, null);
				node.setRecords(records);
			} else {
				ArrayList<Integer> leftAttributes =
						this.copyAttributes(attributes);
				ArrayList<Integer> rightAttributes =
						this.copyAttributes(attributes);

				leftAttributes.remove(new Integer(condition));
				rightAttributes.remove(new Integer(condition));

				node = new Node("internal", condition, null, null);

				if (this.traceBuild) {
					// best condition when an internal node is created
					System.out.println(
						"Best condition on attribute #"+ node.condition
										+ ", chosen from " + attributes);

					// records and attributes that are passed on to subtrees
					// when an internal node is created
					System.out.println(
						"Records with value "+ this.convert(0, node.condition)
										+ ": " + leftRecords);
					System.out.println(
						"Records with value "+ this.convert(1, node.condition)
										+ ": " + rightRecords);
				}
				node.left = this.build(leftRecords, leftAttributes);
				node.right = this.build(rightRecords, rightAttributes);
			}
		}

		if (this.traceBuild && ("leaf" == node.nodeType)) {
			// class name when a leaf is created
			System.out.println("Leaf node with classname: " + this.convert(
				node.className, this.numberAttributes + 1));

		}
		return node;
	}

	private void buildTree() {
		this.root = this.build(this.records, this.attributes);
	}

	private int classify(int[] attributes) {
		return this.classifyGetNode(attributes).className;
	}

	private Node classifyGetNode(int[] attributes) {
		Node current = this.root;

		while (current.nodeType.equals("internal")) {
			if (attributes[current.condition - 1] == 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		}

		return current;
	}

	private ArrayList<Record> collect(ArrayList<Record> records, int condition,
			int value) {
		ArrayList<Record> result = new ArrayList<Record>();

		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).attributes[condition - 1] == value) {
				result.add(records.get(i));
			}
		}

		return result;
	}

	/**
	 * Altered as the specific attributes are not being used
	 *
	 * @param value
	 * @param column
	 * @return
	 */
	private String convert(int value, int column) {
		String label;

		label = Integer.toString(value);

		// removed as this convert is not used in the standard mode
		// if (column == 1) {
		// if (value == 0) {
		// label = "highschool";
		// } else {
		// label = "college";
		// }
		// } else if (column == 2) {
		// if (value == 0) {
		// label = "smoker";
		// } else {
		// label = "nonsmoker";
		// }
		// } else if (column == 3) {
		// if (value == 0) {
		// label = "married";
		// } else {
		// label = "notmarried";
		// }
		// } else if (column == 4) {
		// if (value == 0) {
		// label = "male";
		// } else {
		// label = "female";
		// }
		// } else if (column == 5) {
		// if (value == 0) {
		// label = "works";
		// } else {
		// label = "retired";
		// }
		// } else if (value == 1) {
		// label = "highrisk";
		// } else if (value == 2) {
		// label = "mediumrisk";
		// } else if (value == 3) {
		// label = "lowrisk";
		// } else {
		// label = "undetermined";
		// }

		return label;
	}

	/**
	 * Altered to see if the label passed in could have been in int. If it was,
	 * return that value.
	 *
	 * @param label
	 * @param column
	 * @return
	 */
	private int convert(String label, int column) {
		int value;

		try {
			value = Integer.parseInt(label);
		} catch (NumberFormatException e) {
			if (column == 1) {
				if (label.equals("highschool")) {
					value = 0;
				} else {
					value = 1;
				}
			} else if (column == 2) {
				if (label.equals("smoker")) {
					value = 0;
				} else {
					value = 1;
				}
			} else if (column == 3) {
				if (label.equals("married")) {
					value = 0;
				} else {
					value = 1;
				}
			} else if (column == 4) {
				if (label.equals("male")) {
					value = 0;
				} else {
					value = 1;
				}
			} else if (column == 5) {
				if (label.equals("works")) {
					value = 0;
				} else {
					value = 1;
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
		}

		return value;
	}

	private void convertFrequencyToProbabilities(double[] frequency) {
		double sum = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			sum += frequency[i];
		}

		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] /= sum;
		}
	}

	private ArrayList<Integer> copyAttributes(ArrayList<Integer> attributes) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < attributes.size(); i++) {
			result.add(attributes.get(i));
		}

		return result;
	}

	private double entropy(ArrayList<Record> records) {
		double entropy;
		switch (this.entropyStyle) {
			case CLASSERROR:
				entropy = this.entropyFromClassError(records);
				break;
			case GINI:
				entropy = this.entropyFromGini(records);
				break;
			case SHANNONS:
				entropy = this.entropyFromShannon(records);
				break;
			default:
				entropy = -1;
				break;
		}
		return entropy;
	}

	private double entropyFromClassError(ArrayList<Record> records) {
		double[] frequency = this.getFrequencyOfClasses(records);

		this.convertFrequencyToProbabilities(frequency);

		int maxLocation = 0;
		for (int i = 1; i < this.numberClasses; i++) {
			if (frequency[i] > frequency[maxLocation]) {
				maxLocation = i;
			}
		}

		return 1 - frequency[maxLocation];
	}

	private double entropyFromGini(ArrayList<Record> records) {
		double[] frequency = this.getFrequencyOfClasses(records);

		this.convertFrequencyToProbabilities(frequency);

		double sum = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			sum += (frequency[i] * frequency[i]);
		}

		return 1 - sum;
	}

	private double entropyFromShannon(ArrayList<Record> records) {
		double[] frequency = this.getFrequencyOfClasses(records);

		this.convertFrequencyToProbabilities(frequency);

		double sum = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			sum += (frequency[i] * this.log2(frequency[i]));
		}

		return -sum;
	}

	private double evaluate(ArrayList<Record> records, int attribute) {
		ArrayList<Record> leftRecords = this.collect(records, attribute, 0);
		ArrayList<Record> rightRecords = this.collect(records, attribute, 1);

		double entropyLeft = this.entropy(leftRecords);
		double entropyRight = this.entropy(rightRecords);

		double average = ((entropyLeft * leftRecords.size()) / records.size())
							+ ((entropyRight * rightRecords.size())
								/ records.size());

		return average;
	}

	private double[] getFrequencyOfClasses(ArrayList<Record> records) {
		double[] frequency = new double[this.numberClasses];

		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] = 0;
		}

		for (int i = 0; i < records.size(); i++) {
			frequency[records.get(i).className - 1] += 1;
		}
		return frequency;
	}

	private String getState() {
		// current records
		String toReturn = "Remaining Records:" + this.records.toString();

		// current attributes
		toReturn += "\nRemaining Attributes: " + this.attributes;

		return toReturn;
	}

	private double log2(double a) {
		return Math.log(a) / Math.log(2);
	}

	private int majorityClass(ArrayList<Record> records) {
		int[] frequency = new int[this.numberClasses];

		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] = 0;
		}

		for (int i = 0; i < records.size(); i++) {
			frequency[records.get(i).className - 1] += 1;
		}

		int maxIndex = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			if (frequency[i] > frequency[maxIndex]) {
				maxIndex = i;
			}
		}

		return maxIndex + 1;
	}

	private boolean sameClass(ArrayList<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).className != records.get(0).className) {
				return false;
			}
		}

		return true;
	}

	private double validateWithLeaveOneOut(String trainingFile)
			throws IOException {
		this.loadTrainingData(trainingFile);

		int numberInvalid = 0;
		for (int i = 0; i < this.numberRecords; i++) {
			Record theOneBeingLeftOut = this.records.remove(0);

			this.buildTree();

			int actualClassName = this.classify(theOneBeingLeftOut.attributes);
			if (actualClassName != theOneBeingLeftOut.className) {
				numberInvalid++;
			}

			this.records.add(theOneBeingLeftOut);
		}
		return (numberInvalid / (double) (this.numberRecords - 1));
	}

	/**
	 * Given a training file and a percent of records to hold aside to validate,
	 * evaluate the validation error with a randomly selected set of records
	 * from the set of training records.
	 *
	 * @param trainingFile
	 * @param percentSizeOfValidationSet
	 * @return
	 * @throws IOException
	 */
	private double validateWithRandomSampling(String trainingFile,
			double percentSizeOfValidationSet) throws IOException {
		this.loadTrainingData(trainingFile);

		if (percentSizeOfValidationSet <= 0) {
			// this shouldn't happen
		} else if (percentSizeOfValidationSet >= 1) {
			// this is if the number was not in decimal format i.e. 80 when they
			// mean 80%
			percentSizeOfValidationSet /= 100.0;
		}

		final int numRecords =
				(int) (percentSizeOfValidationSet * this.records.size());
		Collections.shuffle(this.records);
		ArrayList<Record> validationRecords =
				new ArrayList<>(this.records.subList(0, numRecords));
		this.records.removeAll(validationRecords);

		this.buildTree();
		int numberInvalid = 0;

		for (int i = 0; i < validationRecords.size(); i++) {
			int actualClassName =
					this.classify(validationRecords.get(i).attributes);

			if (actualClassName != validationRecords.get(i).className) {
				numberInvalid++;
			}
		}

		return numberInvalid / (double) validationRecords.size();
	}
}

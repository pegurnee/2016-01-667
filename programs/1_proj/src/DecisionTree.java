import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

	private class Node {
		private int className;
		private int condition;
		private Node left;
		private final String nodeType;
		private Node right;

		private Node(String nodeType, int value, Node left, Node right) {
			this.nodeType = nodeType;
			this.left = left;
			this.right = right;

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
			return "\nNode [className="+ this.className + ", condition="
					+ this.condition + ", left=" + this.left + ", nodeType="
					+ this.nodeType + ", right=" + this.right + "]";
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
			return "\nRecord [attributes="+ Arrays.toString(this.attributes)
					+ ", className=" + this.className + "]";
		}
	}

	public static void main(String[] args) throws IOException {
		String inFolder = "in/", outFolder = "out/";
		DecisionTree tree = new DecisionTree();
		tree.loadTrainingData(inFolder + "train1");
		tree.buildTree();

		System.out.println(tree);
		tree.classifyData(inFolder + "test1", outFolder + "classified1");
	}

	private ArrayList<Integer> attributes;
	private int numberAttributes;
	private int numberClasses;
	private int numberRecords;
	private ArrayList<Record> records;

	private Node root;

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
	}

	public void buildTree() {
		this.root = this.build(this.records, this.attributes);
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

		int numberRecords = inFile.nextInt();
		for (int i = 0; i < numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[j] = this.convert(label, j + 1);
			}

			int className = this.classify(attributeArray);
			// removed this because we don't need to be printing out all the
			// attributes
			/*
			 * for (int j = 0; j < this.numberAttributes; j++) { String label =
			 * this.convert(attributeArray[j], j + 1); outFile.print(label + " "
			 * ); }
			 */

			String label = this.convert(className, this.numberAttributes + 1);
			outFile.println(label);
		}

		inFile.close();
		outFile.close();
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

		if (this.sameClass(records)) {
			int className = records.get(0).className;

			node = new Node("leaf", className, null, null);
		} else if (attributes.isEmpty()) {
			int className = this.majorityClass(records);

			node = new Node("leaf", className, null, null);
		} else {
			int condition = this.bestCondition(records, attributes);

			ArrayList<Record> leftRecords = this.collect(records, condition, 0);
			ArrayList<Record> rightRecords =
					this.collect(records, condition, 1);

			if (leftRecords.isEmpty() || rightRecords.isEmpty()) {
				int className = this.majorityClass(records);

				node = new Node("leaf", className, null, null);
			} else {
				ArrayList<Integer> leftAttributes =
						this.copyAttributes(attributes);
				ArrayList<Integer> rightAttributes =
						this.copyAttributes(attributes);

				leftAttributes.remove(new Integer(condition));
				rightAttributes.remove(new Integer(condition));

				node = new Node("internal", condition, null, null);

				node.left = this.build(leftRecords, leftAttributes);
				node.right = this.build(rightRecords, rightAttributes);
			}
		}

		return node;
	}

	private int classify(int[] attributes) {
		Node current = this.root;

		while (current.nodeType.equals("internal")) {
			if (attributes[current.condition - 1] == 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		}

		return current.className;
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

	private ArrayList<Integer> copyAttributes(ArrayList<Integer> attributes) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < attributes.size(); i++) {
			result.add(attributes.get(i));
		}

		return result;
	}

	private double entropy(ArrayList<Record> records) {
		double[] frequency = new double[this.numberClasses];

		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] = 0;
		}

		for (int i = 0; i < records.size(); i++) {
			frequency[records.get(i).className - 1] += 1;
		}

		double sum = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			sum += frequency[i];
		}

		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] = frequency[i] / sum;
		}

		sum = 0;
		for (int i = 0; i < this.numberClasses; i++) {
			sum += (frequency[i] * frequency[i]);
		}

		return 1 - sum;
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
}

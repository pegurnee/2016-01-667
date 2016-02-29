package data_mining.p1.trash;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RuleClassifier {

	private class Record {
		private final int[] attributes;
		private final int className;

		private Record(int[] attributes, int className) {
			this.attributes = attributes;
			this.className = className;
		}
	}

	private class Rule {
		private final int className;
		private final int[] conditions;

		private Rule(int[] conditions, int className) {
			this.conditions = conditions;
			this.className = className;
		}
	}

	private int defaultClass;
	private int numberAttributes;
	private int numberClasses;
	private int numberRecords;
	private ArrayList<Record> records;
	private ArrayList<Rule> rules;

	public RuleClassifier() {
		this.records = null;
		this.rules = null;
		this.numberRecords = 0;
		this.numberAttributes = 0;
		this.numberClasses = 0;
		this.defaultClass = 0;
	}

	public void buildRules() {
		this.rules = new ArrayList<Rule>();
		int[] classOrder = this.orderClasses();

		for (int i = 0; i < this.numberClasses; i++) {
			ArrayList<Rule> classRules = this.findClassRules(classOrder[i]);
			for (int j = 0; j < classRules.size(); j++) {
				this.rules.add(classRules.get(j));
			}
		}

		this.defaultClass = classOrder[this.numberClasses - 1];
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
			String label = this.convert(className, this.numberAttributes + 1);
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

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			int[] attributeArray = new int[this.numberAttributes];

			for (int j = 0; j < this.numberAttributes; j++) {
				String label = inFile.next();
				attributeArray[i] = this.convert(label, j + 1);
			}

			String label = inFile.next();
			int className = this.convert(label, this.numberAttributes + 1);

			Record record = new Record(attributeArray, className);
			this.records.add(record);
		}

		inFile.close();
	}

	public void printRules(String ruleFile) throws IOException {
		PrintWriter outFile = new PrintWriter(new FileWriter(ruleFile));
		for (int i = 0; i < this.rules.size(); i++) {
			outFile.print("IF ");
			for (int j = 0; j < this.numberAttributes; j++) {
				int value = this.rules.get(i).conditions[j];
				if (value != -1) {
					outFile.print("C" + (j + 1) + "=");
					outFile.print(this.convert(value, j + 1) + " ");
				}
			}

			outFile.print("THEN ");

			int className = this.rules.get(i).className;
			outFile.println(this.convert(className, this.numberAttributes + 1));
		}

		outFile.close();
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
		System.out.println(errorRate + " percent error\n");

		inFile.close();
	}

	private boolean addCondition(Rule rule) {
		int maxIndex, maxBit;
		double maxValue, value;

		maxIndex = -1;
		maxBit = -1;
		if (this.isEmpty(rule)) {
			maxValue = 0;
		} else {
			maxValue = this.evaluate(rule);
		}

		for (int i = 0; i < this.numberAttributes; i++) {
			if (rule.conditions[i] == -1) {
				rule.conditions[i] = 0;
				value = this.evaluate(rule);
				if (value > maxValue) {
					maxIndex = i;
					maxBit = 0;
					maxValue = value;
				}

				rule.conditions[i] = 1;
				value = this.evaluate(rule);
				if (value > maxValue) {
					maxIndex = i;
					maxBit = 1;
					maxValue = value;
				}

				rule.conditions[i] = -1;
			}
		}

		if (maxIndex == -1) {
			return false;
		} else {
			rule.conditions[maxIndex] = maxBit;
			return true;
		}
	}

	private boolean classifies(Rule rule, Record record) {
		return this.covers(rule, record)
				&& (rule.className == record.className);
	}

	private int classify(int[] attributes) {
		Record record = new Record(attributes, 0);
		for (int i = 0; i < this.rules.size(); i++) {
			if (this.covers(this.rules.get(i), record)) {
				return this.rules.get(i).className;
			}
		}

		return this.defaultClass;
	}

	private String convert(int value, int column) {
		String label;

		if (column == 1) {
			if (value == 0) {
				label = "college";
			} else {
				label = "highschool";
			}
		} else if (column == 2) {
			if (value == 0) {
				label = "smoker";
			} else {
				label = "nonsmoker";
			}
		} else if (column == 3) {
			if (value == 0) {
				label = "married";
			} else {
				label = "notmarried";
			}
		} else if (column == 4) {
			if (value == 0) {
				label = "male";
			} else {
				label = "female";
			}
		} else if (column == 5) {
			if (value == 0) {
				label = "works";
			} else {
				label = "retired";
			}
		} else if (value == 1) {
			label = "lowrisk";
		} else if (value == 2) {
			label = "mediumrisk";
		} else if (value == 3) {
			label = "highrisk";
		} else {
			label = "undetermined";
		}

		return label;
	}

	private int convert(String label, int column) {
		int value;

		if (column == 1) {
			if (label.equals("college")) {
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
		} else if (label.equals("lowrisk")) {
			value = 1;
		} else if (label.equals("mediumrisk")) {
			value = 2;
		} else if (label.equals("highrisk")) {
			value = 3;
		} else {
			value = 4;
		}

		return value;
	}

	private boolean covers(Rule rule, Record record) {
		for (int i = 0; i < this.numberAttributes; i++) {
			if (rule.conditions[i] != -1) {
				if (rule.conditions[i] != record.attributes[i]) {
					return false;
				}
			}
		}

		return true;
	}

	private double evaluate(Rule rule) {
		double numberCovered = 0;
		double numberClassified = 0;
		for (int i = 0; i < this.records.size(); i++) {
			if (this.covers(rule, this.records.get(i))) {
				numberCovered++;
			}
			if (this.classifies(rule, this.records.get(i))) {
				numberClassified++;
			}
		}

		return (numberClassified + 1) / (numberCovered + this.numberClasses);
	}

	private ArrayList<Rule> findClassRules(int className) {
		ArrayList<Rule> classRules = new ArrayList<Rule>();

		while (this.recordsExist(className)) {
			Rule rule = this.findRule(className);
			classRules.add(rule);
			this.removeCoveredRecords(rule);
		}

		return classRules;
	}

	private Rule findRule(int className) {
		int[] conditions = new int[this.numberAttributes];
		for (int i = 0; i < this.numberAttributes; i++) {
			conditions[i] = -1;
		}

		Rule rule = new Rule(conditions, className);
		for (int i = 0; i < this.numberAttributes; i++) {
			if (!this.addCondition(rule)) {
				break;
			}
		}

		return rule;
	}

	private boolean isEmpty(Rule rule) {
		for (int i = 0; i < this.numberAttributes; i++) {
			if (rule.conditions[i] != -1) {
				return false;
			}
		}

		return true;
	}

	private int[] orderClasses() {
		int[] frequency = new int[this.numberClasses];
		int[] classes = new int[this.numberClasses];

		for (int i = 0; i < this.numberClasses; i++) {
			frequency[i] = 0;
			classes[i] = i + 1;
		}

		for (int i = 0; i < this.records.size(); i++) {
			frequency[this.records.get(i).className - 1] += 1;
		}

		for (int i = 0; i < this.numberClasses; i++) {
			for (int j = i; j < this.numberClasses; j++) {
				if (frequency[i] > frequency[j]) {
					int temp = frequency[i];
					frequency[i] = frequency[j];
					frequency[j] = temp;

					temp = classes[i];
					classes[i] = classes[j];
					classes[j] = temp;
				}
			}
		}

		return classes;
	}

	private boolean recordsExist(int className) {
		for (int i = 0; i < this.records.size(); i++) {
			if (this.records.get(i).className == className) {
				return true;
			}
		}

		return false;
	}

	private void removeCoveredRecords(Rule rule) {
		ArrayList<Record> result = new ArrayList<Record>();

		for (int i = 0; i < this.records.size(); i++) {
			if (!this.covers(rule, this.records.get(i))) {
				result.add(this.records.get(i));
			}
		}

		this.records = result;
	}
}

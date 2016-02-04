import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DecisionTree {
	
	private class Record 
	{
		private int[] attributes;
		private int className;
		
		private Record(int[] attributes, int className)
		{
			this.attributes = attributes;
			this.className = className;
		}
	}
	
	private class Node
	{
		private String nodeType;
		private int condition;
		private int className;
		private Node left;
		private Node right;
		
		private Node(String nodeType, int value, Node left, Node right)
		{
			this.nodeType = nodeType;
			this.left = left;
			this.right = right;
			
			if (nodeType.equals("internal"))
			{
				condition = value;
				className = -1;
			}
			else
			{
				className = value;
				condition = -1;
			}
		}
	}
	
	private Node root;
	private ArrayList<Record> records;
	private ArrayList<Integer> attributes;
	private int numberRecords;
	private int numberAttributes;
	private int numberClasses;
	
	public DecisionTree()
	{
		root = null;
		records = null;
		attributes = null;
		numberRecords = 0;
		numberAttributes = 0;
		numberClasses = 0;
	}
	
	public void buildTree()
	{
		root = build(records, attributes);
	}
	
	private Node build(ArrayList<Record> records, ArrayList<Integer> attributes)
	{
		Node node = null;
		
		if (sameClass(records))
		{
			int className = records.get(0).className;
			
			node = new Node("leaf", className, null, null);
		}
		else if (attributes.isEmpty())
		{
			int className = majorityClass(records);
			
			node = new Node("leaf", className, null, null);
		}
		else
		{
			int condition = bestCondition(records, attributes);
			
			ArrayList<Record> leftRecords = collect(records, condition, 0);
			ArrayList<Record> rightRecords = collect(records, condition, 1);
			
			if (leftRecords.isEmpty() || rightRecords.isEmpty())
			{
				int className = majorityClass(records);
				
				node = new Node("leaf", className, null, null);
			}
			else
			{
				ArrayList<Integer> leftAttributes = copyAttributes(attributes);
				ArrayList<Integer> rightAttributes = copyAttributes(attributes);
				
				leftAttributes.remove(new Integer(condition));
				rightAttributes.remove(new Integer(condition));
				
				node = new Node("internal", condition, null, null);
				
				node.left = build(leftRecords, leftAttributes);
				node.right = build(rightRecords, rightAttributes);
			}
		}
		
		return node;
	}
	
	private boolean sameClass(ArrayList<Record> records)
	{
		for (int i = 0; i < records.size(); i++)
			if (records.get(i).className != records.get(0).className)
				return false;
		
		return true;
	}
	
	private int majorityClass(ArrayList<Record> records)
	{
		int[] frequency = new int[numberClasses];
		
		for (int i = 0; i < numberClasses; i++)
			frequency[i] = 0;
		
		for (int i = 0; i < records.size(); i++)
			frequency[records.get(i).className - 1] += 1;
		
		int maxIndex = 0;
		for (int i = 0; i < numberClasses; i++)
			if (frequency[i] > frequency[maxIndex])
				maxIndex = i;
		
		return maxIndex + 1;
	}
	
	private ArrayList<Record> collect(ArrayList<Record> records, int condition, int value)
	{
		ArrayList<Record> result = new ArrayList<Record>();
		
		for (int i = 0; i < records.size(); i++)
			if (records.get(i).attributes[condition-1] == value)
				result.add(records.get(i));
		
		return result;
	}
	
	private ArrayList<Integer> copyAttributes(ArrayList<Integer> attributes)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (int i = 0; i < attributes.size(); i++)
			result.add(attributes.get(i));
		
		return result;
	}
	
	private int bestCondition(ArrayList<Record> records, ArrayList<Integer> attributes)
	{
		double minValue = evaluate(records, attributes.get(0));
		int minIndex = 0;
		
		for (int i = 0; i < attributes.size(); i++)
		{
			double value = evaluate(records, attributes.get(i));
			
			if (value < minValue)
			{
				minValue = value;
				minIndex = i;
			}
		}
		
		return attributes.get(minIndex);
	}
	
	private double evaluate(ArrayList<Record> records, int attribute)
	{
		ArrayList<Record> leftRecords = collect(records, attribute, 0);
		ArrayList<Record> rightRecords = collect(records, attribute, 1);
		
		double entropyLeft = entropy(leftRecords);
		double entropyRight = entropy(rightRecords);
		
		double average = entropyLeft*leftRecords.size()/records.size() + 
						entropyRight*rightRecords.size()/records.size();
		
		return average;
	}
	
	private double entropy(ArrayList<Record> records)
	{
		double[] frequency = new double[numberClasses];
		
		for (int i = 0; i < numberClasses; i++)
			frequency[i] = 0;
		
		for (int i = 0; i < records.size(); i++)
			frequency[records.get(i).className - 1] += 1;
		
		double sum = 0;
		for (int i = 0; i < numberClasses; i++)
			sum = sum + frequency[i];
		
		for (int i = 0; i < numberClasses; i++)
			frequency[i] = frequency[i]/sum;
		
		sum = 0;
		for (int i = 0; i < numberClasses; i++)
			sum = sum + frequency[i]*frequency[i];
		
		return 1 - sum;
	}
	
	private int classify(int[] attributes)
	{
		Node current = root;
		
		while (current.nodeType.equals("internal"))
		{
			if (attributes[current.condition - 1] == 0)
				current = current.left;
			else
				current = current.right;
		}
		
		return current.className;
	}
	
	public void loadTrainingData(String trainingFile)
	throws IOException
	{
		Scanner inFile = new Scanner(new File(trainingFile));
		
		numberRecords = inFile.nextInt();
		numberAttributes = inFile.nextInt();
		numberClasses = inFile.nextInt();
		
		records = new ArrayList<Record>();
		
		for (int i = 0; i < numberRecords; i++)
		{
			int[] attributeArray = new int[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++)
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			String label = inFile.next();
			int className = convert(label, numberAttributes+1);
			
			Record record = new Record(attributeArray, className);
			
			records.add(record);
		}
		
		attributes = new ArrayList<Integer>();
		for (int i = 0; i < numberAttributes; i++)
			attributes.add(i+1);
		
		inFile.close();
	}
	
	public void classifyData(String testFile, String classifiedFile)
	throws IOException
	{
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));
		
		int numberRecords = inFile.nextInt();
		for (int i = 0; i < numberAttributes; i++)
		{
			int[] attributeArray = new int[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++)
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			int className = classify(attributeArray);
			for (int j = 0; j < numberAttributes; j++)
			{
				String label = convert(attributeArray[j], j+1);
				outFile.print(label + " ");
			}
			
			String label = convert(className, numberAttributes+1);
			outFile.println(label);
		}
		
		inFile.close();
		outFile.close();
	}
	
	private int convert(String label, int column)
	{
		int value;

		if (column == 1)
			if (label.equals("highschool")) value = 0; else value = 1;
		else if (column == 2)
			if (label.equals("smoker")) value = 0; else value = 1;
		else if (column == 3)
			if (label.equals("married")) value = 0; else value = 1;
		else if (column == 4)
			if (label.equals("male")) value = 0; else value = 1;
		else if (column == 5)
			if (label.equals("works")) value = 0; else value = 1;
		else
			if (label.equals("highrisk")) value = 1;
			else if (label.equals("mediumrisk")) value = 2;
			else if (label.equals("lowrisk")) value = 3;
			else value = 4;
		
		return value;
	}
	
	private String convert(int value, int column)
	{
		String label;
		
		if (column == 1)
			if (value == 0) label = "highschool"; else label = "college";
		else if (column == 2)
			if (value == 0) label = "smoker"; else label = "nonsmoker";
		else if (column == 3)
			if (value == 0) label = "married"; else label = "notmarried";
		else if (column == 4)
			if (value == 0) label = "male"; else label = "female";
		else if (column == 5)
			if (value == 0) label = "works"; else label = "retired";
		else
			if (value == 1) label = "highrisk";
			else if (value == 2) label = "mediumrisk";
			else if (value == 3) label = "lowrisk";
			else label = "undetermined";
		
		return label;
	}
}

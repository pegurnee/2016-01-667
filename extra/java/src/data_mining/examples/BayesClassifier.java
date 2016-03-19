package data_mining.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class BayesClassifier 
{
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
	
	private ArrayList<Record> records;
	private int[] attributeValues;
	
	private int numberRecords;
	private int numberAttributes;
	private int numberClasses;
	
	double[] classTable;
	double[][][] table;
	
	public BayesClassifier()
	{
		records = null;
		attributeValues = null;
		
		numberRecords = 0;
		numberAttributes = 0;
		numberClasses = 0;
		
		classTable = null;
		table = null;
	}

	public void loadTrainingData(String trainingFile) throws IOException
	{
		Scanner inFile = new Scanner(new File(trainingFile));
		
		numberRecords = inFile.nextInt();
		numberAttributes = inFile.nextInt();
		numberClasses = inFile.nextInt();
		
		attributeValues = new int[numberAttributes];
		for (int i = 0; i < numberAttributes; i++) 
			attributeValues[i] = inFile.nextInt();
		
		records = new ArrayList<Record>();
		
		for (int i = 0; i < numberRecords; i++) {
			int[] attributeArray = new int[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++) 
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			String label = inFile.next();
			int className = convert(label, numberAttributes + 1);
			
			Record record = new Record(attributeArray, className);
			
			records.add(record);
		}
		
		inFile.close();
	}
	
	public void buildModel()
	{
		fillClassTable();
		
		fillProbabilityTable();
	}

	private void fillClassTable() 
	{
		classTable = new double[numberClasses];
		
		for (int i = 0; i < numberClasses; i++)
			classTable[i] = 0;
		
		for (int i = 0; i < numberRecords; i++)
			classTable[records.get(i).className-1] += 1;
		
		for (int i = 0; i < numberClasses; i++) 
			classTable[i] /= numberRecords;
	}

	private void fillProbabilityTable() 
	{
		table = new double[numberAttributes][][];
		
		for (int i = 0; i < numberAttributes; i++)
			fill(i+1);
	}

	private void fill(int attribute) 
	{
		int attributeValues = this.attributeValues[attribute-1];
		
		table[attribute-1] = new double[numberClasses][attributeValues];
		
		for (int i = 0; i < numberClasses; i++)
			for (int j = 0; j < attributeValues; j++)
				table[attribute-1][i][j] = 0;
		
		for (int k = 0; k < numberRecords; k++)
		{
			int i = records.get(k).className - 1;
			int j = records.get(k).attributes[attribute-1] - 1;
			table[attribute-1][i][j] += 1;
		}
		
		for (int i = 0; i < numberClasses; i++)
			for (int j = 0; j < attributeValues; j++) {
				double value = (table[attribute-1][i][j] + 1)/
								(classTable[i]*numberRecords + attributeValues);
				table[attribute-1][i][j] = value;
			}
	}
	
	private int classify(int[] attributes)
	{
		double maxProbability = 0.0;
		int maxClass = -1;
		
		for (int i = 0; i < numberClasses; i++) {
			double probability = findProbability(i+1, attributes);
			
			if (probability > maxProbability)
			{
				maxProbability = probability;
				maxClass = i;
			}
		}
		
		return maxClass + 1;
	}

	private double findProbability(int className, int[] attributes) 
	{
		double value;
		double product = 1;
		
		for (int i = 0; i < numberAttributes; i++)
		{
			value = table[i][className-1][attributes[i]-1];
			product = product*value;
		}
		
		return product*classTable[className-1];
	}
	
	public void classifyData(String testFile, String classifiedFile)
	throws IOException
	{
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));
		
		int numberRecords = inFile.nextInt();
		
		for (int i = 0; i < numberRecords; i++) {
			int[] attributeArray = new int[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++) 
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			int className = classify(attributeArray);
			
			String label = convert(className);
			outFile.println(label);
		}
		
		inFile.close();
		outFile.close();
	}
	
	public void validate(String validationFile) throws IOException
	{
		Scanner inFile = new Scanner(new File(validationFile));
		
		int numberRecords = inFile.nextInt();
		
		int numberErrors = 0;
		
		for (int i = 0; i < numberRecords; i++)
		{
			int[] attributeArray = new int[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++)
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			int predictedClass = classify(attributeArray);
			
			String label = inFile.next();
			int actualClass = convert(label, numberAttributes+1);
			
			if (predictedClass != actualClass)
				numberErrors += 1;
		}
		
		double errorRate = 100.0*numberErrors/numberRecords;
		System.out.println(errorRate + " percent error");
		
		inFile.close();
	}
	
	private int convert(String label, int column)
	{
		int value;
		
		if (column == 1)
			if (label.equals("college")) value = 1;
			else value = 2;
		
		else if (column == 2)
			if (label.equals("smoker")) value = 1;
			else value = 2;
		
		else if (column == 3)
			if (label.equals("married")) value = 1;
			else value = 2;
		
		else if (column == 4)
			if (label.equals("male")) value = 1;
			else value = 2;
		
		else if (column == 5)
			if (label.equals("works")) value = 1;
			else value = 2;
		
		else
			if (label.equals("highrisk")) value = 1;
			else if (label.equals("mediumrisk")) value = 2;
			else if (label.equals("lowrisk")) value = 3;
			else value = 4;
		
		return value;
	}
	
	private String convert(int value)
	{
		String label;
		
		if (value == 1) label = "highrisk";
		else if (value == 2) label = "mediumrisk";
		else if (value == 3) label = "lowrisk";
		else label = "undetermined";
		
		return label;
	}
}

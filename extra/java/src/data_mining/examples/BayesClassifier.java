package data_mining.examples;

import java.io.File;
import java.io.IOException;
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
			int className = convert(label, numberAttributes);
			
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
	
}

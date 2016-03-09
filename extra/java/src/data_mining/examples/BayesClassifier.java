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
}

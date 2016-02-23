import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class NearestNeighbor {
	
	private class Record
	{
		private double[] attributes;
		private int className;
		
		private Record(double[] attributes, int className)
		{
			this.attributes = attributes;
			this.className = className;
		}
	}
	
	private ArrayList<Record> records;
	private String[] attributeTypes;
	
	private int numberRecords;
	private int numberAttributes;
	private int numberClasses;
	
	private int numberNeighbors;
	private String distanceMeasure;
	private String majorityRule;
	
	public NearestNeighbor()
	{
		records = null;
		attributeTypes = null;
		
		numberRecords = 0;
		numberAttributes = 0;
		numberClasses = 0;
		
		numberNeighbors = 0;
		distanceMeasure = null;
		majorityRule = null;
	}
	
	public void loadTrainingData(String trainingFile)
	throws IOException
	{
		Scanner inFile = new Scanner(new File(trainingFile));
		
		numberRecords = inFile.nextInt();
		numberAttributes = inFile.nextInt();
		numberClasses = inFile.nextInt();
		
		numberNeighbors = inFile.nextInt();
		distanceMeasure = inFile.next();
		majorityRule = inFile.next();
		
		attributeTypes = new String[numberAttributes];
		for (int i = 0; i < numberAttributes; i++)
			attributeTypes[i] = inFile.next();
		
		records = new ArrayList<Record>();
		
		for (int i = 0; i < numberRecords; i++)
		{
			double[] attributeArray = new double[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++)
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			String label = inFile.next();
			int className = (int)convert(label, numberAttributes+1);
			
			Record record = new Record(attributeArray, className);
			records.add(record);
		}
		
		inFile.close();
	}
	
	public void classifyData(String testFile, String classifiedFile)
	throws IOException
	{
		Scanner inFile = new Scanner(new File(testFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));
		
		int numberRecords = inFile.nextInt();
		for (int i = 0; i < numberRecords; i++)
		{
			double[] attributeArray = new double[numberAttributes];
			
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
	
	private int classify(double[] attributes)
	{
		double[] distance = new double[numberRecords];
		int[] id = new int[numberRecords];
		
		for (int i = 0; i < numberRecords; i++)
		{
			distance[i] = distance(attributes, records.get(i).attributes);
			id[i] = i;
		}
		
		nearestNeighbor(distance, id);
		int className = majority(id, attributes);
		
		return className;
	}
	
	private void nearestNeighbor(double[] distance, int[] id)
	{
		for (int i = 0; i < numberNeighbors; i++)
			for (int j = i; j < numberRecords; j++)
				if (distance[i] > distance[j])
				{
					double tempDistance = distance[i];
					distance[i] = distance[j];
					distance[j] = tempDistance;
					
					int tempId = id[i];
					id[i] = id[j];
					id[j] = tempId;
				}
	}
	
	private int majority(int[] id, double[] attributes)
	{
		double[] frequency = new double[numberClasses];
		for (int i = 0; i < numberClasses; i++)
			frequency[i] = 0;
		
		if (majorityRule.equals("unweighted"))
		{
			for (int i = 0; i < numberNeighbors; i++)
				frequency[records.get(id[i]).className - 1] += 1;
		}
		else
		{
			for (int i = 0; i < numberNeighbors; i++)
			{
				double d = distance(records.get(id[i]).attributes, attributes);
				frequency[records.get(id[i]).className - 1] += 1/(d + 0.001);
			}
		}
		
		int maxIndex = 0;
		for (int i = 0; i < numberClasses; i++)
			if (frequency[i] > frequency[maxIndex])
				maxIndex = i;
		
		return maxIndex + 1;
	}
	
	private double distance(double[] u, double[] v)
	{
		double distance = 0;
		
		if (distanceMeasure.equals("euclidean"))
		{
			double sum = 0;
			for (int i = 0; i < u.length; i++)
				sum = sum + (u[i] - v[i])*(u[i] - v[i]);
			
			distance = Math.sqrt(sum);
		}
		else if (distanceMeasure.equals("matching"))
		{
			int matches = 0;
			for (int i = 0; i < u.length; i++)
				if ((int)u[i] == (int)v[i])
					matches = matches + 1;
			
			distance = 1 - (double)matches/u.length;
		}
		else if (distanceMeasure.equals("heterogeneous"))
		{
			double sum = 0;
			double dist = 0;
			
			for (int i = 0; i < u.length; i++) 
			{
				if (attributeTypes[i].equals("binary") || attributeTypes[i].equals("nominal"))
					if ((int)u[i] == (int)v[i])
						dist = 0;
					else
						dist = 1;
				if (attributeTypes[i].equals("ordinal") || attributeTypes[i].equals("continuous"))
					dist = u[i] - v[i];
				
				sum = sum + dist*dist;
			}
			
			distance = Math.sqrt(sum);
		}
		
		return distance;
	}
	
	public void validate(String validationFile) throws IOException
	{
		Scanner inFile = new Scanner(new File(validationFile));
		
		int numberRecords = inFile.nextInt();
		int numberErrors = 0;
		
		for (int i = 0; i < numberRecords; i++)
		{
			double[] attributeArray = new double[numberAttributes];
			
			for (int j = 0; j < numberAttributes; j++)
			{
				String label = inFile.next();
				attributeArray[j] = convert(label, j+1);
			}
			
			int predictedClass = classify(attributeArray);
			String label = inFile.next();
			int actualClass = (int)convert(label, numberAttributes+1);
			
			if (predictedClass != actualClass)
				numberErrors += 1;
		}
		
		double errorRate = 100.0*numberErrors/numberRecords;
		System.out.println(errorRate + " percent error");
		
		inFile.close();
	}
	
	private double convert(String label, int column)
	{
		double value;
		
		if (column == 1)
		{
			if (label.equals("male"))
				value = 0;
			else
				value =1;
		}
		else if (column == 2)
		{
			if (label.equals("single"))
				value = 1;
			else if (label.equals("single"))
				value = 2;
			else
				value = 3;
		}
		else if (column == 3)
		{
			if (label.equals("A"))
				value = 1.0;
			else if (label.equals("B"))
				value = 0.75;
			else if (label.equals("C"))
				value = 0.5;
			else
				value = 0.25;
		}
		else if (column == 4)
		{
			value = Double.valueOf(label);
			value = value/100;
		}
		else if (column == 5)
		{
			value = Double.valueOf(label);
			value = value/4;
		}
		else
		{
			if (label.equals("highrisk"))
				value = 1;
			else if (label.equals("mediumrisk"))
				value = 2;
			else
				value = 3;
		}
		
		return value;
	}
	
	private String convert(int value)
	{
		String label;
		if (value == 1)
			label = "highrisk";
		else if (value == 2)
			label = "mediumrisk";
		else
			label = "lowrisk";
		
		return label;
	}
}

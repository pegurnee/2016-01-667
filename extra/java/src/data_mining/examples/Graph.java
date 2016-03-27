package data_mining.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph 
{
	private class Record
	{
		private double[] attributes;
		
		private Record(double[] attributes)
		{
			this.attributes = attributes;
		}
	}

	private int numberRecords;
	private int numberAttributes;
	private double delta;
	
	private ArrayList<Record> records;
	private int[][] matrix;
	private int[] clusters;
	
	public Graph()
	{
		numberRecords = 0;
		numberAttributes = 0;
		delta = 0;
		
		records = null;
		matrix = null;
		clusters = null;
	}
	
	public void load(String inputFile) throws FileNotFoundException
	{
		Scanner inFile = new Scanner(new File(inputFile));
		
		numberRecords = inFile.nextInt();
		numberAttributes = inFile.nextInt();
		
		records = new ArrayList<Record>();
		
		for (int i = 0; i < numberRecords; i++) {
			double[] attributes = new double[numberAttributes];
			for (int j = 0; j < numberAttributes; j++)
				attributes[j] = inFile.nextDouble();
			
			Record record = new Record(attributes);
			
			records.add(record);
		}
		
		inFile.close();
	}
	
	public void setParameters(double delta)
	{
		this.delta = delta;
	}
	
	public void cluster() 
	{
		createMatrix();
		initializeClusters();
		
		int index = 0;
		int clusterName = 0;
		
		while (index < numberRecords)
		{
			if (clusters[index] == -1)
			{
				assignCluster(index, clusterName);
				clusterName = clusterName + 1;
			}
			
			index = index + 1;
		}
	}
}

package data_mining.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
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
	
	private void createMatrix()
	{
		matrix = new int[numberRecords][numberRecords];
		
		for (int i = 0; i < numberRecords; i++)
			for (int j = 0; j < numberRecords; j++)
				matrix[i][j] = neighbor(records.get(i), records.get(j));
	}
	
	private int neighbor(Record u, Record v)
	{
		double distance = 0.0;
		
		for (int i = 0; i < u.attributes.length; i++)
			distance += (u.attributes[i] - v.attributes[i]) * 
						(u.attributes[i] - v.attributes[i]);
		
		distance = Math.sqrt(distance);
		
		if (distance <= delta)
			return 1;
		else
			return 0;
	}
	
	private void initializeClusters()
	{
		clusters = new int[numberRecords];
		
		for (int i = 0; i < numberRecords; i++)
			clusters[i] = -1;
	}
	
	private void assignCluster(int index, int clusterName)
	{
		clusters[index] = clusterName;
		
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		list.addLast(index);
		
		while (!list.isEmpty())
		{
			int i = list.removeFirst();
			
			for (int j = 0; j < numberRecords; j++)
				if (matrix[i][j] == 1 && clusters[j] == -1)
				{
					clusters[j] = clusterName;
					list.addLast(j);
				}
		}
	}
	
	public void display(String outputFile) throws IOException
	{
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));
		
		for (int i = 0; i < numberRecords; i++)
		{
			for (int j = 0; j < numberAttributes; j++)
				outFile.print(records.get(i).attributes[j] + " ");
			
			outFile.println(clusters[i]+1);
		}
		
		outFile.close();
	}
}

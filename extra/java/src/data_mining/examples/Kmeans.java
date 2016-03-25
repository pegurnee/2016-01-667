package data_mining.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Kmeans
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
	private int numberClusters;
	
	private ArrayList<Record> records;
	private ArrayList<Record> centroids;
	private int[] clusters;
	private Random rand;
	
	public Kmeans()
	{
		numberRecords = 0;
		numberAttributes = 0;
		numberClusters = 0;
		
		records = null;
		centroids = null;
		clusters = null;
		rand = null;
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
	
	public void setParameters(int numberClusters, int seed)
	{
		this.numberClusters = numberClusters;
		
		this.rand = new Random(seed);
	}
	
	public void cluster()
	{
		initializeClusters();
		initializeCentroids();
		
		boolean stopCondition = false;
		
		while (!stopCondition)
		{
			int clusterChanges = assignClusters();
			
			updateCentroids();
			
			stopCondition = clusterChanges == 0;
		}
	}

	private void initializeClusters()
	{
		clusters = new int[numberRecords];
		
		for (int i = 0; i < numberRecords; i++)
			clusters[i] = -1;
	}

	private void initializeCentroids()
	{
		centroids = new ArrayList<Record>();
		
		for (int i = 0; i < numberClusters; i++)
		{
			int index = rand.nextInt(numberRecords);
			
			centroids.add(records.get(index));
		}
	}
	
	private int assignClusters()
	{
		int clusterChanges = 0;
		
		for (int i = 0; i < numberRecords; i++)
		{
			Record record = records.get(i);
			
			double minDistance = distance(record, centroids.get(0));
			int minIndex = 0;
			
			for (int j = 0; j < numberClusters; j++)
			{
				double distance = distance(record, centroids.get(j));
				
				if (distance < minDistance)
				{
					minDistance = distance;
					minIndex = j;
				}
			}
			
			if (clusters[i] != minIndex)
			{
				clusters[i] = minIndex;
				clusterChanges++;
			}
		}
		
		return clusterChanges;
	}

	private void updateCentroids()
	{
		ArrayList<Record> clusterSum = new ArrayList<Record>();
		
		for (int i = 0; i < numberClusters; i++)
		{
			double[] attributes = new double[numberAttributes];
			for (int j = 0; j < numberAttributes; j++)
				attributes[j] = 0;
			
			clusterSum.add(new Record(attributes));
		}
		
		int[] clusterSize = new int[numberClusters];
		
		for (int i = 0; i < numberClusters; i++)
			clusterSize[i] = 0;
		
		for (int i = 0; i < numberRecords; i++)
		{
			int cluster = clusters[i];
			
			Record sum = sum(clusterSum.get(cluster), records.get(i));
			clusterSum.set(cluster, sum);
			
			clusterSize[cluster] += 1;
		}
		
		for (int i = 0; i < numberClusters; i++)
		{
			Record average = scale(clusterSum.get(i), 1.0/clusterSize[i]);
			
			centroids.set(i, average);
		}
	}
	
	private double distance(Record u, Record v) {
		double sum = 0;
		
		for (int i = 0; i < u.attributes.length; i++)
			sum += (u.attributes[i] - v.attributes[i]) *
					(u.attributes[i] - v.attributes[i]);
		
		return sum;
	}
	
	private Record sum(Record u, Record v) {
		double[] result = new double[u.attributes.length];
		
		for (int i = 0; i < u.attributes.length; i++)
			result[i] = u.attributes[i] + v.attributes[i];
		
		return new Record(result);
	}
	
	private Record scale(Record u, double k) {
		double[] result = new double[u.attributes.length];
		
		for (int i = 0; i < result.length; i++)
			result[i] = k*u.attributes[i];
		
		return new Record(result);
	}
	
	public void display(String outputFile) throws IOException
	{
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));
		
		for (int i = 0; i < numberRecords; i++)
		{
			for (int j = 0; j < numberAttributes; j++)
				outFile.print(records.get(i).attributes[j] + " ");
			
			outFile.println(clusters[i] + 1);
		}
		
		outFile.close();
	}
}

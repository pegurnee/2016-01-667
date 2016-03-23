package data_mining.examples;

import java.io.File;
import java.io.FileNotFoundException;
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
}

package data_mining.examples;

import java.util.ArrayList;

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
}

package data_mining.examples;

import java.util.ArrayList;

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

}

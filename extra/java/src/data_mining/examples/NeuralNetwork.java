package data_mining.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NeuralNetwork 
{
	private class Record
	{
		private double[] input;
		private double[] output;
		
		private Record(double[] input, double[] output)
		{
			this.input = input;
			this.output = output;
		}
	}
	
	private int numberRecords;
	private int numberInputs;
	private int numberOutputs;
	
	private int numberMiddle;
	private int numberIterations;
	private int seed;
	private double rate;
	
	private ArrayList<Record> records;
	
	private double[] input;
	private double[] middle;
	private double[] output;
	
	private double[] errorMiddle;
	private double[] errorOut;
	
	private double[] thetaMiddle;
	private double[] thetaOut;
	
	private double[][] matrixMiddle;
	private double[][] matrixOut;
	
	public NeuralNetwork()
	{
		numberRecords = 0;
		numberInputs = 0;
		numberOutputs = 0;
		numberMiddle = 0;
		numberIterations = 0;
		seed = 0;
		rate = 0;
		
		records = null;
		input = null;
		middle = null;
		output = null;
		errorMiddle = null;
		errorOut = null;
		thetaMiddle = null;
		thetaOut = null;
		matrixMiddle = null;
		matrixOut = null;
	}
	
	public void loadTrainingData(String trainingData) throws IOException
	{
		Scanner inFile = new Scanner(new File(trainingData));
		
		numberRecords = inFile.nextInt();
		numberInputs = inFile.nextInt();
		numberOutputs = inFile.nextInt();
		
		records = new ArrayList<Record>();
		
		for (int i = 0; i < numberRecords; i++) {
			double[] input = new double[numberInputs];
			for (int j = 0; j < input.length; j++)
				input[j] = inFile.nextDouble();
			
			double[] output = new double[numberOutputs];
			for (int j = 0; j < numberOutputs; j++)
				output[j] = inFile.nextDouble();
			
			Record record = new Record(input, output);
			
			records.add(record);
		}
		
		inFile.close();
	}
	
	public void setParameters(int numberMiddle, int numberIterations, int seed, double rate)
	{
		this.numberMiddle = numberMiddle;
		this.numberIterations = numberIterations;
		this.seed = seed;
		this.rate = rate;
		
		Random rand = new Random(seed);
		
		input = new double[numberInputs];
		middle = new double[numberMiddle];
		output = new double[numberOutputs];
		
		errorMiddle = new double[numberMiddle];
		errorOut = new double[numberOutputs];
		
		thetaMiddle = new double[numberMiddle];
		for (int i = 0; i < numberMiddle; i++)
			thetaMiddle[i] = 2*rand.nextDouble() - 1;

		thetaOut = new double[numberOutputs];
		for (int i = 0; i < numberOutputs; i++)
			thetaOut[i] = 2*rand.nextDouble() - 1;
		
		matrixMiddle = new double[numberInputs][numberMiddle];
		for (int i = 0; i < numberInputs; i++)
			for (int j = 0; j < numberMiddle; j++)
				matrixMiddle[i][j] = 2*rand.nextDouble() - 1;
		
		matrixOut = new double[numberMiddle][numberOutputs];
		for (int i = 0; i < numberMiddle; i++)
			for (int j = 0; j < numberOutputs; j++)
				matrixOut[i][j] = 2*rand.nextDouble() - 1;
	}
	
	public void train()
	{
		for (int i = 0; i < numberIterations; i++)
			for (int j = 0; j < numberRecords; j++)
			{
				forwardCalculation(records.get(j).input);
				
				backwardCalculation(records.get(j).output);
			}
	}

	private void forwardCalculation(double[] trainingInput)
	{
		for (int i = 0; i < numberInputs; i++)
			input[i] = trainingInput[i];
		
		for (int i = 0; i < numberMiddle; i++) {
			double sum = 0;
			
			for (int j = 0; j < numberInputs; j++)
				sum += input[j]*matrixMiddle[j][i];
			
			sum += thetaMiddle[i];
			
			middle[i] = 1/(1 + Math.exp(-sum));
		}
		
		for (int i = 0; i < numberOutputs; i++)
		{
			double sum = 0;
			
			for (int j = 0; j < numberMiddle; j++)
				sum += middle[j]*matrixOut[j][i];
			
			sum += thetaOut[i];
			
			output[i] = 1/(1 + Math.exp(-sum));
		}
	}

	private void backwardCalculation(double[] trainingOutput) 
	{
		for (int i = 0; i < numberOutputs; i++)
			errorOut[i] = output[i]*(1-output[i])*(trainingOutput[i]-output[i]);
		
		for (int i = 0; i < numberMiddle; i++)
		{
			double sum = 0;
			
			for (int j = 0; j < numberOutputs; j++)
				sum += matrixOut[i][j]*errorOut[j];
			
			errorMiddle[i] = middle[i]*(1-middle[i])*sum;
		}
		
		for (int i = 0; i < numberMiddle; i++) 
			for (int j = 0; j < numberOutputs; j++)
				matrixOut[i][j] += rate*middle[i]*errorOut[j];

		for (int i = 0; i < numberInputs; i++) 
			for (int j = 0; j < numberMiddle; j++)
				matrixMiddle[i][j] += rate*input[i]*errorMiddle[j];

		for (int i = 0; i < numberOutputs; i++)
			thetaOut[i] += rate*errorOut[i];
		
		for (int i = 0; i < numberMiddle; i++)
			thetaMiddle[i] += rate*errorMiddle[i];
	}
	
	private double[] test(double[] input)
	{
		forwardCalculation(input);
		
		return output;
	}
	
	public void testData(String inputFile, String outputFile) throws IOException
	{
		Scanner inFile = new Scanner(new File(inputFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));
		
		int numberRecords = inFile.nextInt();
		
		for (int i = 0; i < numberRecords; i++)
		{
			double[] input = new double[numberInputs];
			
			for (int j = 0; j < numberInputs; j++)
				input[j] = inFile.nextDouble();
			
			double[] output = test(input);
			
			for (int j = 0; j < numberOutputs; j++)
				outFile.print(output[j] + " ");
			outFile.println();
		}
		
		inFile.close();
		outFile.close();
	}
}

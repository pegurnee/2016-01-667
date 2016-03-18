
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Neural network classifier, original code presented by Dr Suchindran Maniccam.
 * Modified and expanded by Eddie Gurnee for use in Data Mining project 2.
 *
 * @author smaniccam
 * @author eddie
 *
 */
public class NeuralNetwork {

	private class Record {
		private final double[] input;
		private final double[] output;

		private Record(double[] input, double[] output) {
			this.input = input;
			this.output = output;
		}
	}

	private double[] errorMiddle;
	private double[] errorOut;

	private double[] input;

	private double[][] matrixMiddle;
	private double[][] matrixOut;
	private double[] middle;

	private int numberInputs;
	private int numberIterations;
	private int numberMiddle;
	private int numberOutputs;

	private int numberRecords;

	private double[] output;

	private double rate;

	private ArrayList<Record> records;

	private int seed;

	private double[] thetaMiddle;
	private double[] thetaOut;

	public NeuralNetwork() {
		this.numberRecords = 0;
		this.numberInputs = 0;
		this.numberOutputs = 0;
		this.numberMiddle = 0;
		this.numberIterations = 0;
		this.seed = 0;
		this.rate = 0;

		this.records = null;
		this.input = null;
		this.middle = null;
		this.output = null;
		this.errorMiddle = null;
		this.errorOut = null;
		this.thetaMiddle = null;
		this.thetaOut = null;
		this.matrixMiddle = null;
		this.matrixOut = null;
	}

	public void loadTrainingData(String trainingData) throws IOException {
		Scanner inFile = new Scanner(new File(trainingData));

		this.numberRecords = inFile.nextInt();
		this.numberInputs = inFile.nextInt();
		this.numberOutputs = inFile.nextInt();

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			double[] input = new double[this.numberInputs];
			for (int j = 0; j < input.length; j++) {
				input[j] = inFile.nextDouble();
			}

			double[] output = new double[this.numberOutputs];
			for (int j = 0; j < this.numberOutputs; j++) {
				output[j] = inFile.nextDouble();
			}

			Record record = new Record(input, output);

			this.records.add(record);
		}

		inFile.close();
	}

	public void setParameters(int numberMiddle, int numberIterations, int seed,
			double rate) {
		this.numberMiddle = numberMiddle;
		this.numberIterations = numberIterations;
		this.seed = seed;
		this.rate = rate;

		Random rand = new Random(seed);

		this.input = new double[this.numberInputs];
		this.middle = new double[numberMiddle];
		this.output = new double[this.numberOutputs];

		this.errorMiddle = new double[numberMiddle];
		this.errorOut = new double[this.numberOutputs];

		this.thetaMiddle = new double[numberMiddle];
		for (int i = 0; i < numberMiddle; i++) {
			this.thetaMiddle[i] = (2 * rand.nextDouble()) - 1;
		}

		this.thetaOut = new double[this.numberOutputs];
		for (int i = 0; i < this.numberOutputs; i++) {
			this.thetaOut[i] = (2 * rand.nextDouble()) - 1;
		}

		this.matrixMiddle = new double[this.numberInputs][numberMiddle];
		for (int i = 0; i < this.numberInputs; i++) {
			for (int j = 0; j < numberMiddle; j++) {
				this.matrixMiddle[i][j] = (2 * rand.nextDouble()) - 1;
			}
		}

		this.matrixOut = new double[numberMiddle][this.numberOutputs];
		for (int i = 0; i < numberMiddle; i++) {
			for (int j = 0; j < this.numberOutputs; j++) {
				this.matrixOut[i][j] = (2 * rand.nextDouble()) - 1;
			}
		}
	}

	public void testData(String inputFile, String outputFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(inputFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		int numberRecords = inFile.nextInt();

		for (int i = 0; i < numberRecords; i++) {
			double[] input = new double[this.numberInputs];

			for (int j = 0; j < this.numberInputs; j++) {
				input[j] = inFile.nextDouble();
			}

			double[] output = this.test(input);

			for (int j = 0; j < this.numberOutputs; j++) {
				outFile.print(output[j] + " ");
			}
			outFile.println();
		}

		inFile.close();
		outFile.close();
	}

	public void train() {
		for (int i = 0; i < this.numberIterations; i++) {
			for (int j = 0; j < this.numberRecords; j++) {
				this.forwardCalculation(this.records.get(j).input);

				this.backwardCalculation(this.records.get(j).output);
			}
		}
	}

	public void validate(String validationFile) throws IOException {
		Scanner inFile = new Scanner(new File(validationFile));

		int numberRecords = inFile.nextInt();

		double error = 0;

		for (int i = 0; i < numberRecords; i++) {
			double[] input = new double[this.numberInputs];
			for (int j = 0; j < this.numberInputs; j++) {
				input[j] = inFile.nextDouble();
			}

			double[] actualOutput = new double[this.numberOutputs];
			for (int j = 0; j < this.numberOutputs; j++) {
				actualOutput[j] = inFile.nextDouble();
			}

			double[] predictedOutput = this.test(input);

			error += this.computeError(actualOutput, predictedOutput);
		}

		System.out.println(error / numberRecords);

		inFile.close();
	}

	private void backwardCalculation(double[] trainingOutput) {
		for (int i = 0; i < this.numberOutputs; i++) {
			this.errorOut[i] = this.output[i]* (1 - this.output[i])
								* (trainingOutput[i] - this.output[i]);
		}

		for (int i = 0; i < this.numberMiddle; i++) {
			double sum = 0;

			for (int j = 0; j < this.numberOutputs; j++) {
				sum += this.matrixOut[i][j] * this.errorOut[j];
			}

			this.errorMiddle[i] = this.middle[i] * (1 - this.middle[i]) * sum;
		}

		for (int i = 0; i < this.numberMiddle; i++) {
			for (int j = 0; j < this.numberOutputs; j++) {
				this.matrixOut[i][j] +=
						this.rate * this.middle[i] * this.errorOut[j];
			}
		}

		for (int i = 0; i < this.numberInputs; i++) {
			for (int j = 0; j < this.numberMiddle; j++) {
				this.matrixMiddle[i][j] +=
						this.rate * this.input[i] * this.errorMiddle[j];
			}
		}

		for (int i = 0; i < this.numberOutputs; i++) {
			this.thetaOut[i] += this.rate * this.errorOut[i];
		}

		for (int i = 0; i < this.numberMiddle; i++) {
			this.thetaMiddle[i] += this.rate * this.errorMiddle[i];
		}
	}

	private double computeError(double[] actualOutput,
			double[] predictedOutput) {
		double error = 0;

		for (int i = 0; i < actualOutput.length; i++) {
			error += Math.pow(actualOutput[i] - predictedOutput[i], 2);
		}

		return Math.sqrt(error / actualOutput.length);
	}

	private void forwardCalculation(double[] trainingInput) {
		for (int i = 0; i < this.numberInputs; i++) {
			this.input[i] = trainingInput[i];
		}

		for (int i = 0; i < this.numberMiddle; i++) {
			double sum = 0;

			for (int j = 0; j < this.numberInputs; j++) {
				sum += this.input[j] * this.matrixMiddle[j][i];
			}

			sum += this.thetaMiddle[i];

			this.middle[i] = 1 / (1 + Math.exp(-sum));
		}

		for (int i = 0; i < this.numberOutputs; i++) {
			double sum = 0;

			for (int j = 0; j < this.numberMiddle; j++) {
				sum += this.middle[j] * this.matrixOut[j][i];
			}

			sum += this.thetaOut[i];

			this.output[i] = 1 / (1 + Math.exp(-sum));
		}
	}

	private double[] test(double[] input) {
		this.forwardCalculation(input);

		return this.output;
	}

}

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

	public static void main(String[] args) throws IOException {
		String inFolder = "in/part3/", outFolder = "out/part3/";
		String trainingFile = inFolder + "train5",
				testingFile = inFolder + "test5",
				validationFile = inFolder + "validate5",
				classifiedFile = outFolder + "classified5";
		// NeuralNetwork classifier =
		// new NeuralNetwork(new StudentNeuralNetDataConverter());
		NeuralNetwork classifier = new NeuralNetwork(
				ConfigurationObject.getInstance().getConverter(3, 15));

		classifier.loadTrainingData(trainingFile);

		// for (Record r : classifier.records) {
		// System.out.println(
		// Arrays.toString(r.input) + " : " + Arrays.toString(r.output));
		// }

		// 53467
		// 453876
		// 234789
		// 1
		classifier.setParameters(4, 50_000, 53467, .65);

		classifier.train();

		classifier.testData(testingFile, classifiedFile);

		classifier.displayWeightsAndThetas();

		// System.out.printf("training error: %7.4f%%%n",
		// classifier.computeTrainingError());
		System.out.printf("training error:   %7.4f%%%n",
			classifier.computeValidationError(trainingFile) * 100);
		System.out.printf("validation error: %7.4f%%%n",
			(classifier.computeValidationError(validationFile) * 100));
	}

	private final NeuralNetworkDataConverterInterface converter;

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

	/**
	 * answer to section 2 question 1 part a
	 */
	public NeuralNetwork() {
		this(new DoubleDataConverter());
	}

	public NeuralNetwork(DataConverterInterface converter) {
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

		this.converter = (NeuralNetworkDataConverterInterface) converter;
	}

	public double computeTrainingError() {
		double errorValue = this.computeClassificationError();

		return (100.0 * errorValue) / this.numberRecords;
	}

	public double computeValidationError(String validationFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(validationFile));

		int numberRecords = inFile.nextInt();

		inFile.nextLine();

		double error = 0;

		for (int i = 0; i < numberRecords; i++) {
			double[] input = new double[this.numberInputs];
			for (int j = 0; j < this.numberInputs; j++) {
				input[j] = this.convert(inFile.next(), j);
			}

			double[] actualOutput = new double[this.numberOutputs];
			for (int j = 0; j < this.numberOutputs; j++) {
				actualOutput[j] =
						this.convert(inFile.next(), j + this.numberInputs);
			}

			double[] predictedOutput = this.test(input);
			// System.out.println("actual: " + Arrays.toString(actualOutput));
			// System.out.println("predic: " +
			// Arrays.toString(predictedOutput));

			error += this.computeError(actualOutput, predictedOutput);
		}

		inFile.close();

		return error / numberRecords;
	}

	public void displayWeightsAndThetas() {
		StringBuilder response = new StringBuilder();
		// this.matrixMiddle
		// this.matrixOut
		// this.thetaMiddle
		// this.thetaOut

		// make headers for input matrix
		response.append(ProjectTwoTools.getCellString(""));
		for (int i = 0; i < this.numberMiddle; i++) {
			response.append(ProjectTwoTools.getCellString("middle " + i + ":"));
		}
		response.append("\n");

		// print out input/middle weight matrix
		for (int i = 0; i < this.matrixMiddle.length; i++) {
			response.append(ProjectTwoTools.getCellString("input " + i + ": "));
			for (int j = 0; j < this.matrixMiddle[i].length; j++) {
				response.append(
					ProjectTwoTools.getCellString(this.matrixMiddle[i][j]));
			}
			response.append("\n");
		}
		response.append("\n");

		// make headers for output matrix
		response.append(ProjectTwoTools.getCellString(""));
		for (int i = 0; i < this.numberOutputs; i++) {
			response.append(ProjectTwoTools.getCellString("output " + i + ":"));
		}
		response.append("\n");

		// print out middle/output weight matrix
		for (int i = 0; i < this.matrixOut.length; i++) {
			response.append(
				ProjectTwoTools.getCellString("middle " + i + ": "));
			for (int j = 0; j < this.matrixOut[i].length; j++) {
				response.append(
					ProjectTwoTools.getCellString(this.matrixOut[i][j]));
			}
			response.append("\n");
		}
		response.append("\n");

		// theta for middle
		response.append(ProjectTwoTools.getCellString(""));
		response.append(ProjectTwoTools.getCellString("middle"));
		response.append("\n");
		for (int i = 0; i < this.thetaMiddle.length; i++) {
			response.append(ProjectTwoTools.getCellString("theta " + i + ":"));
			response.append(ProjectTwoTools.getCellString(this.thetaMiddle[i]));
			response.append("\n");
		}
		response.append("\n");

		// theta for out
		response.append(ProjectTwoTools.getCellString(""));
		response.append(ProjectTwoTools.getCellString("out"));
		response.append("\n");
		for (int i = 0; i < this.thetaOut.length; i++) {
			response.append(ProjectTwoTools.getCellString("theta " + i + ":"));
			response.append(ProjectTwoTools.getCellString(this.thetaOut[i]));
			response.append("\n");
		}
		response.append("\n");

		System.out.println(response.toString());
	}

	/**
	 * answer to section 2 question 1 part b
	 *
	 * @param trainingData
	 * @throws IOException
	 */
	public void loadTrainingData(String trainingData) throws IOException {
		Scanner inFile = new Scanner(new File(trainingData));

		this.numberRecords = inFile.nextInt();
		this.numberInputs = inFile.nextInt();
		this.numberOutputs = inFile.nextInt();

		this.records = new ArrayList<Record>();

		for (int i = 0; i < this.numberRecords; i++) {
			double[] input = new double[this.numberInputs];
			for (int j = 0; j < input.length; j++) {
				input[j] = this.convert(inFile.next(), j);
			}

			double[] output = new double[this.numberOutputs];
			for (int j = 0; j < this.numberOutputs; j++) {
				output[j] = this.convert(inFile.next(), j + this.numberInputs);
			}

			Record record = new Record(input, output);

			this.records.add(record);
		}

		inFile.close();
	}

	/**
	 * answer to section 2 question 1 part c
	 *
	 * @param numberMiddle
	 * @param numberIterations
	 * @param seed
	 * @param rate
	 */
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

	/**
	 * answer to section 2 question 1 part e
	 *
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException
	 */
	public void testData(String inputFile, String outputFile)
			throws IOException {
		Scanner inFile = new Scanner(new File(inputFile));
		PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

		int numberRecords = inFile.nextInt();

		for (int i = 0; i < numberRecords; i++) {
			double[] input = new double[this.numberInputs];

			for (int j = 0; j < this.numberInputs; j++) {
				input[j] = this.convert(inFile.next(), j);
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

	/**
	 * answer to section 2 question 1 part d
	 */
	public void train() {
		for (int i = 0; i < this.numberIterations; i++) {
			for (int j = 0; j < this.numberRecords; j++) {
				this.forwardCalculation(this.records.get(j).input);

				this.backwardCalculation(this.records.get(j).output);
			}
		}
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

	private int computeClassificationError() {
		int numberError = 0;

		for (Record r : this.records) {
			double[] actualOutput = this.test(r.input);

			if (!this.isOutputCloseEnough(r.output, actualOutput)) {
				System.out.println("error: "+ Arrays.toString(r.input)
									+ " ex: " + Arrays.toString(r.output)
									+ " ac: " + Arrays.toString(actualOutput));
				System.out.println(
					"label: "		+ this.convertOutput(r.output[0], 0) + " ac: "
									+ this.convertOutput(actualOutput[0], 0));
				numberError++;
			}
		}
		return numberError;
	}

	private double computeError(double[] actualOutput,
			double[] predictedOutput) {
		double error = 0;

		for (int i = 0; i < actualOutput.length; i++) {
			error += Math.pow(actualOutput[i] - predictedOutput[i], 2);
		}

		return Math.sqrt(error / actualOutput.length);
	}

	private double convert(String label, int column) {
		return this.converter.convertToNumericalValue(label, column);
	}

	private String convertOutput(double value, int column) {
		return this.converter.convertFromNumericalValue(value,
			column + this.numberInputs);
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

	private boolean isOutputCloseEnough(double[] output1, double[] output2) {
		for (int i = 0; i < output2.length; i++) {
			final String label1 = this.convertOutput(output1[i], i);
			final String label2 = this.convertOutput(output2[i], i);

			if (!label1.equals(label2)) {
				return false;
			}
		}
		return true;
	}

	private double[] test(double[] input) {
		this.forwardCalculation(input);

		return this.output;
	}

}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class FuzzyRanges {
	public static void main(String[] args) throws FileNotFoundException {
		String inFolder = "in/part3/", outFile = "ranges";
		int numFiles = 5;
		String[] fileTypes = { "train", "test", "validate" };
		StringBuilder output = new StringBuilder();

		Scanner inFile;

		for (int i = 0; i < numFiles; i++) {
			int numberRanges = 0, numInputs = 0, numOutputs = 0;
			ArrayList<LinkedList<Double>> values = null;

			for (int j = 0; j < fileTypes.length; j++) {
				int numberRecords;
				inFile = new Scanner(
						new File(inFolder + fileTypes[j] + (i + 1)));

				numberRecords = inFile.nextInt();

				if (j == 0) {
					numInputs = inFile.nextInt();
					numOutputs = inFile.nextInt();
					numberRanges = numInputs + numOutputs;
					values = new ArrayList<>(numberRanges);

					for (int k = 0; k < numberRanges; k++) {
						values.add(new LinkedList<>());
					}
				} else if (j == 1) {
					numberRanges = numInputs;
				} else if (j == 2) {
					numberRanges = numInputs + numOutputs;
				}

				for (int r = 0; r < numberRecords; r++) {
					for (int v = 0; v < numberRanges; v++) {
						final double incomingValue = inFile.nextDouble();
						values.get(v).add(incomingValue);
						// System.out.println(incomingValue);
					}
				}
			}

			for (int g = 0; g < numberRanges; g++) {
				double min, max;

				min = max = values.get(g).pop();

				while (!values.get(g).isEmpty()) {
					final Double top = values.get(g).pop();

					if (min > top) {
						min = top;
					} else if (max < top) {
						max = top;
					}
				}

				System.out.println(String.format(
					"For set %d, range #%d:%nmin: %12.6f max: %12.6f", i, g,
					min, max));
			}

		}
	}
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FuzzyRanges {
	public static void main(String[] args) throws FileNotFoundException {
		String inFolder = "in/part3/", outFile = "ranges";
		int numFiles = 5;
		String[] fileTypes = { "train", "test", "validate" };

		Scanner inFile;
		for (int i = 0; i < numFiles; i++) {
			int numberRanges = 0;
			double[][] values;

			for (int j = 0; j < fileTypes.length; j++) {
				int numberRecords;
				inFile = new Scanner(
						new File(inFolder + fileTypes[j] + (i + 1)));

				numberRecords = inFile.nextInt();

				if (j == 0) {
					numberRanges = inFile.nextInt() + inFile.nextInt();
				}

				for (int k = 0; k < numberRecords; k++) {

				}
			}

		}
	}
}

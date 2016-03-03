package data_mining.p1.drivers;

import java.io.IOException;
import java.util.Scanner;

import data_mining.p1.classifiers.NearestImageNeighbor;

public class NearestImageClassification {
	public static void main(String[] args) throws IOException {
		String inFolder = "in/", outFolder = "out/",
				imageFolder = inFolder + "images/";
		String trainingFile = inFolder + "imageTrain",
				classifiedFile = outFolder + "classifiedImages";
		String[] testingFiles = { "i1", "i2", "i3", "i4", "i5", "i6", "i7",
				"i8", "i9", "i10", "i11", "i12", "i13", "i14", "i15", "i16",
				"i17", "i18", "i19", "i20", "i21", "i22", "i23", "i24", "i25" };
		int numRecords;
		Scanner kb = new Scanner(System.in);

		NearestImageNeighbor moore = new NearestImageNeighbor();

		numRecords = moore.loadImageTrainingData(trainingFile);
		System.out.println("Loaded all " + numRecords + " training data");

		moore.classifyData(imageFolder, testingFiles, classifiedFile);
		System.out.println("Classified all data");

		while (true) {
			System.out.print("Enter filename to test: ");
			String filename = kb.next();

			int classname = moore.classifyImageFile(filename);
			System.out.println("That one is probably a " + classname);

			System.out.print("Again? [y/n]: ");
			if (kb.next().indexOf('n') != -1) {
				break;
			}
		}

		System.out.println("Goodbye");

	}
}

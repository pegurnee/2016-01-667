package data_mining.p2.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;

import data_mining.p2.converters.ContinuousValuesDataConverter;
import data_mining.p2.converters.DataConverterInterface;
import data_mining.p2.converters.DoubleDataConverter;
import data_mining.p2.converters.IntegerDataConverter;
import data_mining.p2.converters.JavaResumeDataConverter;
import data_mining.p2.converters.LoanNeuralNetDataConverter;
import data_mining.p2.converters.StandardAndPoorsNeuralNetDataConverter;
import data_mining.p2.converters.StudentNeuralNetDataConverter;

public class ConfigurationObject
	implements Serializable {
	private static ConfigurationObject instance = null;
	private static int numberOfPartThreeDataSets = 5;
	private static String rangesFile = "in/part3/ranges";

	private static final long serialVersionUID = -1354273965361346550L;

	public static ConfigurationObject getInstance() {
		if (instance == null) {
			instance = new ConfigurationObject(loadRanges(rangesFile));
		}
		return instance;
	}

	private static int[][][] loadRanges(String rangesFile) {
		// TODO Auto-generated method stub
		int[][][] ranges = new int[numberOfPartThreeDataSets][][];
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(rangesFile));

			for (int dataSetIndex =
					0; dataSetIndex < numberOfPartThreeDataSets; dataSetIndex++) {
				int numRanges = inFile.nextInt();

				ranges[dataSetIndex] = new int[numRanges][2];
				for (int rangeIndex = 0; rangeIndex < numRanges; rangeIndex++) {
					ranges[dataSetIndex][rangeIndex][0] = inFile.nextInt();
					ranges[dataSetIndex][rangeIndex][1] = inFile.nextInt();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

		} finally {
			inFile.close();
		}

		return ranges;
	}

	private final String IN_FOLDER = "in/";
	private final String OUT_FOLDER = "out/";
	private final int[][][] ranges;
	private final String STOCK_LOCATION = "sp500/";

	private ConfigurationObject(int[][][] ranges) {
		this.ranges = ranges;
	}

	public DataConverterInterface getConverter(int section, int question) {
		DataConverterInterface converter = null;
		switch (section) {
			case 1:
				if (question == 2) {
					converter = new IntegerDataConverter();
				} else if (question == 3) {
					converter = new JavaResumeDataConverter();
				}
				break;
			case 2:
				if (question == 2) {
					converter = new StudentNeuralNetDataConverter();
				} else if (question == 3) {
					converter = new LoanNeuralNetDataConverter();
				}
				break;
			case 3:
				if (question == 2) {
					converter = new StandardAndPoorsNeuralNetDataConverter();
				} else if (question > 10) {
					converter = new ContinuousValuesDataConverter(
							this.ranges[question - 11]);
				}
				break;
			default:
				converter = new DoubleDataConverter();
		}
		return converter;
	}

	public String getFileClassified(int section, int question) {
		String fileLocation = this.OUT_FOLDER + this.getFileLocation(section,
			question, FileType.CLASSIFIED);

		final File file = new File(fileLocation);
		final File parentDirectory = file.getParentFile();

		if (null != parentDirectory) {
			parentDirectory.mkdirs();
		}

		return fileLocation;
	}

	public String getFileTesting(int section, int question) {
		return this.IN_FOLDER
				+ this.getFileLocation(section, question, FileType.TESTING);
	}

	public String getFileTraining(int section, int question) {
		return this.IN_FOLDER
				+ this.getFileLocation(section, question, FileType.TRAINING);
	}

	public String getFileValidation(int section, int question) {
		return this.IN_FOLDER
				+ this.getFileLocation(section, question, FileType.VALIDATION);
	}

	public String getLocationInput(int section, int question) {
		String part = this.getSubfolder(section, question);
		return this.IN_FOLDER + part;
	}

	public String getLocationOutput(int section, int question) {
		String part = this.getSubfolder(section, question);
		return this.OUT_FOLDER + part;
	}

	private String getFileLocation(int section, int question, FileType type) {
		String location = this.getSubfolder(section, question);
		location += type.toString();

		if (section != 3) {
			location += (question - 1);
		} else if (question != 2) {
			location += (question - 10);
		}

		return location;
	}

	private String getSubfolder(int section, int question) {
		String part = "part" + section + "/";
		if (this.needsStockMarketValues(section, question)) {
			part = this.STOCK_LOCATION;
		}
		return part;
	}

	private boolean needsStockMarketValues(int section, int question) {
		return (section == 3) && (question == 2);
	}

}

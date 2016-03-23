import java.io.File;
import java.io.Serializable;

public class ConfigurationObject
	implements Serializable {
	private static ConfigurationObject instance = null;

	private static final long serialVersionUID = -1354273965361346550L;

	public static ConfigurationObject getInstance() {
		if (instance == null) {
			instance = new ConfigurationObject();
		}
		return instance;
	}

	private final String IN_FOLDER = "in/";
	private final String OUT_FOLDER = "out/";
	private final String STOCK_LOCATION = "sp500/";

	private ConfigurationObject() {}

	public DataConverterInterface getConverter(int section, int question) {
		DataConverterInterface converter = null;
		switch (section) {
			case 1:
				if (question == 2) {
					converter = new IntegerDataConverter();
				} else if (question == 3) {
					converter = new JavaResumeDataConverter();
				}
			case 2:
				if (question == 2) {
					converter = new StudentNeuralNetDataConverter();
				} else if (question == 3) {
					converter = new LoanNeuralNetDataConverter();
				}
			case 3:
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
		if (!this.needsStockMarketValues(section, question)) {
			if (section < 3) {
				location += (question - 1);
			}
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

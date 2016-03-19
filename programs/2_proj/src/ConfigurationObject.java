import java.io.Serializable;

public class ConfigurationObject
	implements Serializable {
	private static ConfigurationObject instance = null;

	private static final long serialVersionUID = -1354273965361346550L;

	private final String IN_FOLDER = "in/";
	private final String OUT_FOLDER = "out/";

	private final String STOCK_LOCATION = "sp500/";

	private ConfigurationObject() {}

	public String getFileInput(int section, int question, FileType type) {
		return this.getFileLocation(section, question, type);
	}

	public String getFileOutput(int section, int question, FileType type) {
		return this.getFileLocation(section, question, type);
	}

	public ConfigurationObject getInstance() {
		if (instance == null) {
			instance = new ConfigurationObject();
		}
		return instance;
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
		String location = this.getLocationInput(section, question);
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

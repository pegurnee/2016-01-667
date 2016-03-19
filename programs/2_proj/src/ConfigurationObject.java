import java.io.Serializable;

public class ConfigurationObject
	implements Serializable {
	private static ConfigurationObject instance = null;
	/**
	 *
	 */
	private static final long serialVersionUID = -1354273965361346550L;

	private final String IN_FOLDER = "in/";
	private final String OUT_FOLDER = "out/";

	private ConfigurationObject() {}

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

	private String getSubfolder(int section, int question) {
		String part = "part" + section + "/";
		if ((section == 3) && (question == 2)) {
			part = "sp500/";
		}
		return part;
	}

}

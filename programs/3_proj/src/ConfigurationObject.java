/**
 * This singleton object holds all of the data used to run and interact with
 * data mining project 3.
 *
 * @author eddie
 *
 */
public class ConfigurationObject {
	private static ConfigurationObject instance = null;

	public static ConfigurationObject getInstance() {
		if (instance == null) {
			instance = new ConfigurationObject();
		}
		return instance;
	}

	private final int[][] FILE_NAMES = { { 1, 2 }, { 3, 4 } };
	private final String IN_FOLDER = "in/";
	private final String OUT_FOLDER = "out/";

	private ConfigurationObject() {
	}

	public String getInFile(int part, int question) {
		return this.IN_FOLDER + "file" + this.FILE_NAMES[part - 1][question - 2];
	}

	public String getOutFile(int part, int question) {
		return this.OUT_FOLDER + "file" + this.FILE_NAMES[part - 1][question - 2];
	}
}

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

	private ConfigurationObject() {}
}

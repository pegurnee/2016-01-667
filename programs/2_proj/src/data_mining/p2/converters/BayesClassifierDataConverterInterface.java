package data_mining.p2.converters;

/**
 * This data converter interface is designed to be used only with the Bayes
 * Classifer. Provides for two methods to convert raw information into data to
 * be used by the classifier, and then data back into raw information again.
 *
 * @author eddie
 *
 */
public interface BayesClassifierDataConverterInterface
	extends DataConverterInterface {
	/**
	 * Converts a numerical value to its corresponding label
	 * 
	 * @param value
	 * @param column
	 * @return
	 */
	String convertFromNumericalValue(int value, int column);

	/**
	 * Converts a label into a numerical representation
	 *
	 * @param label
	 * @param column
	 * @return
	 */
	int convertToNumericalValue(String label, int column);
}

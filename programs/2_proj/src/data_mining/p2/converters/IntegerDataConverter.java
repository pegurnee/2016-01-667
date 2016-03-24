package data_mining.p2.converters;

/**
 * This data converter is used in the example dataset for section 1 problem 2.
 * 
 * @author eddie
 *
 */
public class IntegerDataConverter
	implements BayesClassifierDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {

		if ((column == 1) || (column == 2) || (column == 4)) {
			value += 1;
		}

		return Integer.toString(value);
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		int value = Integer.parseInt(label);

		if ((column == 1) || (column == 2) || (column == 4)) {
			value -= 1;
		}

		return value;
	}
}

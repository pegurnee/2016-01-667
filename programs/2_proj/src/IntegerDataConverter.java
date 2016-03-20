
public class IntegerDataConverter
	implements DataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {
		return Integer.toString(value);
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		int value = Integer.parseInt(label);

		if ((column == 1) || (column == 4)) {
			value += 1;
		}

		return value;
	}
}

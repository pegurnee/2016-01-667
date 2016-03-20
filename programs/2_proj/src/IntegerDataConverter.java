
public class IntegerDataConverter
	implements DataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {

		if ((column == 1) || (column == 2) || (column == 4)) {
			value += 1;
		}

		// switch (column) {
		// case 1:
		// case 2:
		// case 4:
		// value += 1;
		// case 0:
		// case 3:
		// default:
		// break;
		// }

		return Integer.toString(value);
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		int value = Integer.parseInt(label);

		if ((column == 1) || (column == 2) || (column == 4)) {
			value -= 1;
		}

		// switch (column) {
		// case 1:
		// case 2:
		// case 4:
		// value -= 1;
		// case 0:
		// case 3:
		// default:
		// break;
		// }

		return value;
	}
}

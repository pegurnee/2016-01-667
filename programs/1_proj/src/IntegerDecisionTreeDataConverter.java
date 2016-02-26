
public class IntegerDecisionTreeDataConverter
	implements DecisionTreeDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {
		return Integer.toString(value);
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		return Integer.parseInt(label);
	}

}

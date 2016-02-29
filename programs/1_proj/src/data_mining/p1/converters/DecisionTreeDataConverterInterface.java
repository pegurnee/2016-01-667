package data_mining.p1.converters;

public interface DecisionTreeDataConverterInterface {
	String convertFromNumericalValue(int value, int column);

	int convertToNumericalValue(String label, int column);

}

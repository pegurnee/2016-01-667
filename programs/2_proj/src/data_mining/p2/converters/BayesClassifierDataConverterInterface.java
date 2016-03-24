package data_mining.p2.converters;

public interface BayesClassifierDataConverterInterface
	extends DataConverterInterface {
	String convertFromNumericalValue(int value, int column);

	int convertToNumericalValue(String label, int column);
}

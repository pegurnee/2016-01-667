
public interface BayesClassifierDataConverterInterface {
	String convertFromNumericalValue(int value, int column);

	int convertToNumericalValue(String label, int column);
}

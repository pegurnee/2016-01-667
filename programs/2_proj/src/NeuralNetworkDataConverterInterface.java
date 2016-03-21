
public interface NeuralNetworkDataConverterInterface {
	String convertFromNumericalValue(double value, int column);

	double convertToNumericalValue(String label, int column);
}

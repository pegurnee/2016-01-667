package data_mining.p2.converters;

public interface NeuralNetworkDataConverterInterface
	extends DataConverterInterface {
	String convertFromNumericalValue(double value, int column);

	double convertToNumericalValue(String label, int column);
}

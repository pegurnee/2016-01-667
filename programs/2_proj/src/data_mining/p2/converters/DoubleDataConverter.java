package data_mining.p2.converters;

public class DoubleDataConverter
	implements NeuralNetworkDataConverterInterface {

	@Override
	public String convertFromNumericalValue(double value, int column) {
		return Double.toString(value);
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		return Double.parseDouble(label);
	}

}

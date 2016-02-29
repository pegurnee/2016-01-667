package data_mining.p1.converters;

public interface NearestNeighborDataConverterInterface {

	String convertFromNumericalValue(int value);

	double convertToNumericalValue(String label, int column);

}

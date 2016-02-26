
public interface NearestNeighborDataConverterInterface {

	String convertFromNumericalValue(int value);

	double convertToNumericalValue(String label, int column);

}

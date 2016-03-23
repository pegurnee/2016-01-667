
public class ContinuousValuesDataConverter
	implements NeuralNetworkDataConverterInterface {

	private final int[][] ranges;

	public ContinuousValuesDataConverter(int[][] ranges) {
		this.ranges = ranges;
	}

	@Override
	public String convertFromNumericalValue(double value, int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		// TODO Auto-generated method stub
		return 0;
	}

}

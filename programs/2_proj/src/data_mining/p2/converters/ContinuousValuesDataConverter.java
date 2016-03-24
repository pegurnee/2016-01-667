package data_mining.p2.converters;

import data_mining.p2.util.ProjectTwoTools;

public class ContinuousValuesDataConverter
	implements NeuralNetworkDataConverterInterface {

	private final int[][] ranges;

	public ContinuousValuesDataConverter(int[][] ranges) {
		this.ranges = ranges;
	}

	@Override
	public String convertFromNumericalValue(double value, int column) {
		// TODO Auto-generated method stub
		String label;

		label = Double.toString(ProjectTwoTools.denormalize(value,
			this.ranges[column][0], this.ranges[column][1]));

		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		// TODO Auto-generated method stub
		double value;

		value = ProjectTwoTools.normalize(Double.parseDouble(label),
			this.ranges[column][0], this.ranges[column][1]);

		return value;
	}

}

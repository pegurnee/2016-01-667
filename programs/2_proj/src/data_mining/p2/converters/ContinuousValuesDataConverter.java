package data_mining.p2.converters;

import data_mining.p2.util.ProjectTwoTools;

/**
 * This converter contains the ranges and methods needed to convert input values
 * into normalized values, and vice versa.
 *
 * @author eddie
 *
 */
public class ContinuousValuesDataConverter
	implements NeuralNetworkDataConverterInterface {

	private static final int MAX = 1;
	private static final int MIN = 0;
	private final int[][] ranges;

	/**
	 * The constructor takes an input of a multidimensional array of integers,
	 * indexed by column number, then by min/max;
	 * 
	 * @param ranges
	 */
	public ContinuousValuesDataConverter(int[][] ranges) {
		this.ranges = ranges;
	}

	@Override
	public String convertFromNumericalValue(double value, int column) {
		String label;

		label = Double.toString(ProjectTwoTools.denormalize(value,
			this.ranges[column][MIN], this.ranges[column][MAX]));

		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		double value;

		value = ProjectTwoTools.normalize(Double.parseDouble(label),
			this.ranges[column][MIN], this.ranges[column][MAX]);

		return value;
	}

}

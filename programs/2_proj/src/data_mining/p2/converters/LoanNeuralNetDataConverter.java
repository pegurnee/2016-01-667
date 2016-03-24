package data_mining.p2.converters;

import data_mining.p2.util.ProjectTwoTools;

public class LoanNeuralNetDataConverter
	implements NeuralNetworkDataConverterInterface {

	private final double[][] haystack =
			{ { 0.0, 1.0 }, { 0.0, 0.5, 1.0 }, { 0.0, 0.33, 0.66, 1.0 } };
	private final int[][] ranges = { { 500, 900 }, { 30, 90 }, { 30, 80 } };
	private final String[][] values =
			{ { "male", "female" }, { "single", "divorced", "married" },
					{ "low", "medium", "high", "undetermined" } };

	@Override
	public String convertFromNumericalValue(double value, int column) {
		String label;

		switch (column) {
			case 0:
			case 1:
			case 2:
				label = Double.toString(ProjectTwoTools.denormalize(value,
					this.ranges[column][0], this.ranges[column][1]));
				break;
			case 3:
			case 4:
			case 5:
			default:
				label = this.values[column - 3][ProjectTwoTools.closest(value,
					this.haystack[column - 3])];
		}

		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		double value;

		switch (column) {
			case 0:
			case 1:
			case 2:
				value = ProjectTwoTools.normalize(Double.parseDouble(label),
					this.ranges[column][0], this.ranges[column][1]);
				break;
			case 3:
			case 4:
			case 5:
			default:
				int hayIndex;
				switch (label) {
					case "male":
					case "single":
					case "low":
						hayIndex = 0;
						break;
					case "female":
					case "divorced":
					case "medium":
						hayIndex = 1;
						break;
					case "married":
					case "high":
						hayIndex = 2;
						break;
					case "undetermined":
					default:
						hayIndex = 3;
				}
				value = this.haystack[column - 3][hayIndex];
		}

		return value;
	}

}

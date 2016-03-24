
public class StudentNeuralNetDataConverter
	implements NeuralNetworkDataConverterInterface {
	private final double[] haystack = { 0.0, 0.5, 1.0 };
	private final int[][] ranges = { { 0, 100 }, { 0, 4 } };
	private final String[][] values =
			{ { "A", "B", "C" }, { "good", "average", "bad" } };

	@Override
	public String convertFromNumericalValue(double value, int column) {
		String label;

		switch (column) {
			case 0:
			case 1:
				label = Double.toString(ProjectTwoTools.denormalize(value,
					this.ranges[column][0], this.ranges[column][1]));
				break;
			case 2:
			case 3:
			default:
				label = this.values[column - 2][ProjectTwoTools.closest(value,
					this.haystack)];
		}

		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		double value;

		switch (column) {
			case 0:
			case 1:
				value = ProjectTwoTools.normalize(Double.parseDouble(label),
					this.ranges[column][0], this.ranges[column][1]);
				break;
			case 2:
			case 3:
			default:
				switch (label) {
					case "A":
					case "good":
						value = this.haystack[0];
						break;
					case "B":
					case "average":
						value = this.haystack[1];
						break;
					case "C":
					case "bad":
					default:
						value = this.haystack[2];
						break;
				}
		}

		return value;
	}
}

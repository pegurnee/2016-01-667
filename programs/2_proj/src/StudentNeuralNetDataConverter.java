
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
				label = Double.toString(this.denormalize(value,
					this.ranges[column][0], this.ranges[column][1]));
				break;
			case 2:
			case 3:
			default:
				label = this.values[column - 2][this.closest(value,
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
				value = this.normalize(Double.parseDouble(label),
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

	private int closest(double needle, double[] haystack) {
		double[] relatives = new double[haystack.length];

		for (int i = 0; i < relatives.length; i++) {
			relatives[i] = Math.abs(haystack[i] - needle);
		}

		int nearestIndex = 0;
		for (int i = 1; i < relatives.length; i++) {
			if (relatives[nearestIndex] < relatives[i]) {
				nearestIndex = i;
			}
		}

		return nearestIndex;
	}

	private double denormalize(double x, double min, double max) {
		return (x * (max - min)) + min;
	}

	private double normalize(double x, double min, double max) {
		return (x - min) / (max - min);
	}

}

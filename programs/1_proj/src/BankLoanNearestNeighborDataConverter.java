
public class BankLoanNearestNeighborDataConverter
	implements NearestNeighborDataConverterInterface {

	private final int[][] ranges = { { 500, 900 }, { 30, 90 }, { 30, 80 } };

	@Override
	public String convertFromNumericalValue(int value) {
		String label;
		switch (value) {
			case 1:
				label = "low";
				break;
			case 2:
				label = "medium";
				break;
			case 3:
				label = "high";
				break;
			case 4:
			default:
				label = "undetermined";
				break;
		}
		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		double value;
		switch (column) {
			case 1:
			case 2:
			case 3:
				value = this.normalize(Double.parseDouble(label),
					this.ranges[column - 1][0], this.ranges[column - 1][0]);
				break;
			case 4:
			case 5:
			case 6:
			default:
				switch (label) {
					case "male":
					case "single":
					case "low":
						value = 1;
						break;
					case "female":
					case "married":
					case "medium":
						value = 2;
						break;
					case "divorced":
					case "high":
						value = 3;
						break;
					case "undetermined":
					default:
						value = 4;
				}

		}
		return value;
	}

	private double normalize(double x, double min, double max) {
		return (x - min) / (max - min);
	}

}

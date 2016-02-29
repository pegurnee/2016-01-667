package data_mining.p1.converters;

public class StudentQualityNearestNeighborDataConverter
	implements NearestNeighborDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value) {
		String label;
		switch (value) {
			case 1:
				label = "good";
				break;
			case 2:
				label = "average";
				break;
			case 3:
			default:
				label = "bad";
				break;
		}
		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		// TODO Auto-generated method stub
		double value;
		switch (column) {
			case 1:
			case 2:
				value = Double.parseDouble(label);
				value /= 4;
				if (column == 1) {
					value /= 25;
				}
				break;
			case 3:
			case 4:
			default:
				switch (label) {
					case "A":
					case "good":
						value = 1;
						break;
					case "B":
					case "average":
						value = 2;
						break;
					case "C":
					case "bad":
					default:
						value = 3;
						break;
				}
		}
		return value;
	}

}

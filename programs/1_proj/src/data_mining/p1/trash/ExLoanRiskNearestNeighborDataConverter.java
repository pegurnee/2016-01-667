package data_mining.p1.trash;


import data_mining.p1.converters.NearestNeighborDataConverterInterface;

public class ExLoanRiskNearestNeighborDataConverter
	implements NearestNeighborDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value) {
		String label;
		if (value == 1) {
			label = "highrisk";
		} else if (value == 2) {
			label = "mediumrisk";
		} else {
			label = "lowrisk";
		}

		return label;
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		double value;

		if (column == 1) {
			if (label.equals("male")) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (column == 2) {
			if (label.equals("single")) {
				value = 1;
			} else if (label.equals("single")) {
				value = 2;
			} else {
				value = 3;
			}
		} else if (column == 3) {
			if (label.equals("A")) {
				value = 1.0;
			} else if (label.equals("B")) {
				value = 0.75;
			} else if (label.equals("C")) {
				value = 0.5;
			} else {
				value = 0.25;
			}
		} else if (column == 4) {
			value = Double.valueOf(label);
			value = value / 100;
		} else if (column == 5) {
			value = Double.valueOf(label);
			value = value / 4;
		} else {
			if (label.equals("highrisk")) {
				value = 1;
			} else if (label.equals("mediumrisk")) {
				value = 2;
			} else {
				value = 3;
			}
		}

		return value;
	}

}

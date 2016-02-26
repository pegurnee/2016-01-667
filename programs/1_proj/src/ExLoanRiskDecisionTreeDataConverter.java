
public class ExLoanRiskDecisionTreeDataConverter
	implements DecisionTreeDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {
		String label;
		if (column == 1) {
			if (value == 0) {
				label = "highschool";
			} else {
				label = "college";
			}
		} else if (column == 2) {
			if (value == 0) {
				label = "smoker";
			} else {
				label = "nonsmoker";
			}
		} else if (column == 3) {
			if (value == 0) {
				label = "married";
			} else {
				label = "notmarried";
			}
		} else if (column == 4) {
			if (value == 0) {
				label = "male";
			} else {
				label = "female";
			}
		} else if (column == 5) {
			if (value == 0) {
				label = "works";
			} else {
				label = "retired";
			}
		} else if (value == 1) {
			label = "highrisk";
		} else if (value == 2) {
			label = "mediumrisk";
		} else if (value == 3) {
			label = "lowrisk";
		} else {
			label = "undetermined";
		}
		return label;
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		// TODO Auto-generated method stub
		int value;
		if (column == 1) {
			if (label.equals("highschool")) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (column == 2) {
			if (label.equals("smoker")) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (column == 3) {
			if (label.equals("married")) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (column == 4) {
			if (label.equals("male")) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (column == 5) {
			if (label.equals("works")) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (label.equals("highrisk")) {
			value = 1;
		} else if (label.equals("mediumrisk")) {
			value = 2;
		} else if (label.equals("lowrisk")) {
			value = 3;
		} else {
			value = 4;
		}

		return value;
	}

}

package data_mining.p1.converters;

public class JavaResumeDecisionTreeDataConverter
	implements DecisionTreeDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {
		String label;
		switch (column) {
			case 1:
				label = (value == 1) ? "cs" : "other";
				break;
			case 2:
				label = (value == 1) ? "java" : "no";
				break;
			case 3:
				label = (value == 1) ? "c/c++" : "no";
				break;
			case 4:
				label = (value == 1) ? "gpa>3" : "gpa<3";
				break;
			case 5:
				label = (value == 1) ? "large" : "small";
				break;
			case 6:
				label = (value == 1) ? "years<5" : "years>5";
				break;
			case 7:
			default:
				label = (value == 1) ? "hire" : "no";
				break;
		}
		return label;
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		int value;
		if (column < 7) {
			switch (label) {
				case "cs":
				case "java":
				case "c/c++":
				case "gpa>3":
				case "large":
				case "years<5":
					value = 1;
					break;
				default:
					value = 0;
					break;
			}
		} else {
			if ("hire".equals(label)) {
				value = 1;
			} else {
				value = 2;
			}
		}
		return value;
	}

}

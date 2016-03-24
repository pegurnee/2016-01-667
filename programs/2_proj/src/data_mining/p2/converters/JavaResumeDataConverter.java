package data_mining.p2.converters;

/**
 * This data converter is used with the hiring question implemented with a bayes
 * classifer in section 1 question 3.
 * 
 * @author eddie
 *
 */
public class JavaResumeDataConverter
	implements BayesClassifierDataConverterInterface {

	@Override
	public String convertFromNumericalValue(int value, int column) {
		String label;

		switch (column) {
			case 0:
			case 2:
				label = Integer.toString(value);
				break;
			case 4:
				label = Character.toString((char) (value + 'A'));
				break;
			case 1:
				// java knowledge or no
			case 5:
				// hire or not
				if (value == 0) {
					label = "no";
				} else {
					if (column == 1) {
						label = "java";
					} else {
						label = "hire";
					}
				}
				break;
			case 3:
				// cs degree or other
			default:
				if (value == 0) {
					label = "cs";
				} else {
					label = "other";
				}
				break;
		}

		return label;
	}

	@Override
	public int convertToNumericalValue(String label, int column) {
		int value;

		switch (column) {
			case 0:
			case 2:
				value = Integer.parseInt(label);
				break;
			case 4:
				value = label.charAt(0) - 'A';
				break;
			case 1:
				// java knowledge or not
			case 3:
				// cs degree or other
			case 5:
				// hire or not
			default:
				if ("no".equals(label) || "cs".equals(label)) {
					value = 0;
				} else {
					value = 1;
				}
				break;
		}

		return value;
	}

}

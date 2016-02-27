
public class BankLoanNearestNeighborDataConverter
	implements NearestNeighborDataConverterInterface {

	/*
	 * credit score: 500-900 income: 30k-90k age: 30-80 sex: male, female
	 * marital status: single, divorced, married class: low risk, medium risk,
	 * high risk, undetermined
	 *
	 *
	 */
	@Override
	public String convertFromNumericalValue(int value) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return 0;
	}

}

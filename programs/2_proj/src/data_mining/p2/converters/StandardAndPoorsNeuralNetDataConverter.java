package data_mining.p2.converters;

import data_mining.p2.util.ProjectTwoTools;

public class StandardAndPoorsNeuralNetDataConverter
	implements NeuralNetworkDataConverterInterface {
	private final int[] sapRange = { -2, 2 };

	@Override
	public String convertFromNumericalValue(double value, int column) {
		// TODO Auto-generated method stub
		return Double.toString(ProjectTwoTools.denormalize(value,
			this.sapRange[0], this.sapRange[1]));
	}

	@Override
	public double convertToNumericalValue(String label, int column) {
		// TODO Auto-generated method stub
		return ProjectTwoTools.normalize(Double.parseDouble(label),
			this.sapRange[0], this.sapRange[1]);
	}

}

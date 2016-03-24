package data_mining.p2.util;

/**
 * This class contains a number of static methods that are used in various
 * places around the project.
 * 
 * @author eddie
 *
 */
public class ProjectTwoTools {
	public static int closest(double needle, double[] haystack) {
		double[] relatives = new double[haystack.length];

		for (int i = 0; i < relatives.length; i++) {
			relatives[i] = Math.abs(haystack[i] - needle);
		}

		int nearestIndex = 0;
		for (int i = 1; i < relatives.length; i++) {
			if (relatives[nearestIndex] > relatives[i]) {
				nearestIndex = i;
			}
		}

		return nearestIndex;
	}

	public static double denormalize(double x, double min, double max) {
		return (x * (max - min)) + min;
	}

	public static String getCellString(double cellData) {
		final String cellFormat = "%-12.6f";
		return String.format(cellFormat, cellData);
	}

	public static String getCellString(String cellData) {
		final String cellFormat = "%-12s";
		return String.format(cellFormat, cellData.toString());
	}

	public static double normalize(double x, double min, double max) {
		return (x - min) / (max - min);
	}
}

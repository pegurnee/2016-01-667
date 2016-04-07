
/**
 * This class contains a number of static methods that are used in various
 * places around the project.
 * 
 * @author eddie
 *
 */
public class ProjectThreeTools {

	public static double denormalize(double x, double min, double max) {
		return (x * (max - min)) + min;
	}

	public static double normalize(double x, double min, double max) {
		return (x - min) / (max - min);
	}
}

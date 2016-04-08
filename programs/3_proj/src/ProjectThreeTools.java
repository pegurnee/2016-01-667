
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

    public static double distance(double x, double y) {
        return Math.sqrt(Math.pow((x - y), 2));
    }

    public static double normalize(double x, double min, double max) {
        return (x - min) / (max - min);
    }
}

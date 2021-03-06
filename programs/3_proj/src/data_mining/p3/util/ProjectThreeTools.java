package data_mining.p3.util;

/**
 * This class contains a number of static methods that are used in various
 * places around the project.
 *
 * @author eddie
 *
 */
public class ProjectThreeTools {

    /**
     * Given a value and a range, denormalizes the value
     *
     * @param x
     * @param min
     * @param max
     * @return
     */
    public static double denormalize(double x, double min, double max) {
        return (x * (max - min)) + min;
    }

    /**
     * Computes the euclidean distance between two doubles
     *
     * @param x
     * @param y
     * @return
     */
    public static double distance(double x, double y) {
        return Math.sqrt(Math.pow((x - y), 2));
    }

    /**
     * Given a value and a range, normalizes the value
     * 
     * @param x
     * @param min
     * @param max
     * @return
     */
    public static double normalize(double x, double min, double max) {
        return (x - min) / (max - min);
    }
}

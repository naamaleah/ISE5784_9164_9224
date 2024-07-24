package lighting;

import primitives.*;

import java.util.List;

/**
 * interface for objects representing a light source
 * * @author Naama and Yeela
 */
public interface LightSource {

    /**
     * get color of intensity of light at a given point in a 3D space
     *
     * @param p {@link Point} to get intensity at
     * @return {@link Color} of intensity of light
     */
    public Color getIntensity(Point p);

    /**
     * get a direction vector from light source to a given point in 3D space
     *
     * @param p {@link Point} to get intensity at
     * @return {@link  Vector} from light source to the point
     */
    public Vector getL(Point p);

    /**
     * Returns distance of lightsource from a given point
     *
     * @param point from which the distance is calculated
     * @return the distance
     */
    double getDistance(Point point);

    /**
     * Creates a list of vectors from the given point to random points around the light within radius r
     *
     * @param p      the given point
     * @param r      the radius
     * @param amount the amount of vectors to create
     * @return list of vectors
     */
    public List<Vector> getLCircle(Point p, double r, int amount);

}

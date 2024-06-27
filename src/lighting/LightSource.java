package lighting;

import primitives.*;

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
}

package lighting;

import primitives.*;

/**
 * light source object
 *
 * @author Naama and Yeela
 */
public class PointLight extends Light implements LightSource {
    /**
     * position {@link Point} of light source in 3D space
     */
    private Point position;
    /**
     * attenuation coefficient
     */
    private double kC = 1;
    /**
     * attenuation coefficient depending on distance
     */
    private double kL = 0;
    /**
     * attenuation coefficient depending on distance²
     */
    private double kQ = 0;

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kC attenuation coefficient
     * @return this instance of object
     */
    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kL attenuation coefficient
     * @return this instance of object
     */
    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kQ attenuation coefficient
     * @return this instance of object
     */
    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * constructor
     *
     * @param intensity {@link Color} of intensity of light
     * @param position  position {@link Point} of the light object
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        // Calculates lights intensity at a point given it's distance from the point
        // light
        double dSquare = position.distanceSquared(p);
        return intensity.scale(1 / (kC + kL * Math.sqrt(dSquare) + kQ * dSquare));
    }

    @Override
    public Vector getL(Point p) {
        try {
            return p.subtract(position).normalize();
        }
        // point light is at given point
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}

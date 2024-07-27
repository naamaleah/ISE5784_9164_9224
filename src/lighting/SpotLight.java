package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static java.lang.Math.max;
import static java.lang.Math.pow;

/**
 * a {@link PointLight} with direction to the light beam
 *
 * @author Naama and Yeela
 */
public class SpotLight extends PointLight {
    /**
     * A factor that determines the narrowness of the light beam
     */
    private double narrowBeam = 1;
    /**
     * direction of light beam
     */
    private Vector direction;

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kC attenuation coefficient
     * @return this instance of object
     */
    public SpotLight setkC(double kC) {
        return (SpotLight) super.setkC(kC);
    }

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kL attenuation coefficient
     * @return this instance of object
     */
    public SpotLight setkL(double kL) {
        return (SpotLight) super.setkL(kL);
    }

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kQ attenuation coefficient
     * @return this instance of object
     */
    public SpotLight setkQ(double kQ) {
        return (SpotLight) super.setkQ(kQ);
    }

    /**
     * constructor
     *
     * @param intensity {@link Color} of intensity of light
     * @param position  position {@link Point} of the light object
     * @param direction direction {@link Vector} of the light beam
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Setter for narrowBeam
     *
     * @param narrowBeam parameter for narrowBeam
     * @return Return itself
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        Color color = super.getIntensity(p);
        Vector v = super.getL(p);
        double factor = pow(max(0, direction.dotProduct(v)), narrowBeam);
        return color.scale(factor);
    }
}

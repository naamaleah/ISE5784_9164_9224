package lighting;

import primitives.*;

import java.util.List;

/**
 * light source object with direction to the light (no attenuation)
 * * * @author Naama and Yeela
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * direction of the light
     */
    private Vector direction;

    /**
     * constructor
     *
     * @param intensity {@link Color} of intensity of the light
     * @param direction direction {@link Vector} of beam
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public List<Vector> getLCircle(Point p, double r, int amount) {
        return List.of(getL(p));
    }
}

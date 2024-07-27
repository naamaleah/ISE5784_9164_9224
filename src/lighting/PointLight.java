package lighting;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.sqrt;
import static primitives.Util.isZero;

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

    private static final Random RND = new Random();

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

    /**
     * Creates a list of vectors from the given point to random points around the light within radius r
     *
     * @param p      the given point
     * @param r      the radius
     * @param amount the amount of vectors to create
     * @return list of vectors
     */
    @Override
    public List<Vector> getLCircle(Point p, double r, int amount) {
        if (p.equals(position))
            return null;

        List<Vector> result = new LinkedList<>();

        Vector l = getL(p);
        result.add(l);

        if (amount < 2) {
            return result;
        }

        Vector vAcross;
        if (isZero(l.getX()) && isZero(l.getY())) {
            vAcross = new Vector(-1 * l.getZ(), 0, 0).normalize();
        } else {
            vAcross = new Vector(-1 * l.getY(), l.getX(), 0).normalize();
        }
        Vector vForward = vAcross.crossProduct(l).normalize();

        double cosAngle, sinAngle, moveX, moveY, d;

        for (int i = 0; i < amount; i++) {
            Point movedPoint = this.position;

            cosAngle = 2 * RND.nextDouble() - 1;
            sinAngle = sqrt(1 - cosAngle * cosAngle);

            d = r * (2 * RND.nextDouble() - 1);
            if (isZero(d)) {
                i--;
                continue;
            }

            moveX = d * cosAngle;
            moveY = d * sinAngle;

            if (!isZero(moveX)) {
                movedPoint = movedPoint.add(vAcross.scale(moveX));
            }
            if (!isZero(moveY)) {
                movedPoint = movedPoint.add(vForward.scale(moveY));
            }

            result.add(p.subtract(movedPoint).normalize());
        }
        return result;
    }
}

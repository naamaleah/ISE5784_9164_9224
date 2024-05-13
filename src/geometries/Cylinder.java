package geometries;

import primitives.*;

/**
 * The class will represent a Cylinder
 */
public class Cylinder extends Tube{

    private double height;

    /**
     * Constructor to initialize Cylinder based on radius,ray and height
     * @param r  for radius
     * @param axis  for ray
     * @param height for  height
     */
    public Cylinder(double r, Ray axis, double height) {
        super(r, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p) {

    }
}

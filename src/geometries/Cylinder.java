package geometries;

import primitives.*;

public class Cylinder extends Tube{

    private double height;

    public Cylinder(double r, Ray axis, double height) {
        super(r, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }
}

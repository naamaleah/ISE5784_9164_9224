package geometries;

import primitives.*;

public class Sphere extends RadialGeometry {
    private final Point center;

    public Sphere(double r,Point p) {
        super(r);
        center=p;
    }


    public Vector getNormal(Point p){
       return null;
    }
}

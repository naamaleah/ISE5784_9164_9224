package geometries;

import primitives.*;

public class Tube extends RadialGeometry{

    protected final Ray axis;

    public Tube(double r, Ray axis) {
        super(r);
        this.axis = axis;
    }


    public Vector getNormal(Point p){
        return null;
    }
}

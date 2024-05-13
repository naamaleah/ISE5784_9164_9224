package geometries;

import primitives.*;

/**
 * Class of Tube
 */
public class Tube extends RadialGeometry{

    protected final Ray axis;

    /**
     * Constructor to initialize Tube with radius and ray
     * @param r a radius
     * @param axis a ray
     */
    public Tube(double r, Ray axis) {
        super(r);
        this.axis = axis;
    }


    public Vector getNormal(Point p){
        Vector v=this.axis.getDirection();
        Point p0=this.axis.getHead();
        double t=v.dotProduct(p.subtract(p0));
        Point o=p0.add(v.scale(t));
        return ((p).subtract(o)).normalize();

    }
}

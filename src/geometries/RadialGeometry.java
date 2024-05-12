package geometries;

/**
 * RadialGeometry abstract class that implements the Geometry interface,
 */
public abstract class RadialGeometry {

    protected final double radius;

    /**
     * Constructor to initialize RadialGeometry based on radius
     * @param r a radius
     */
    public RadialGeometry(double r){
        radius=r;
    }

}

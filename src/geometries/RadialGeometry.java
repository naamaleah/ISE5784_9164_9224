package geometries;

/**
 * Class Sphere is the RadialGeometry abstract class that implements the Geometry interface
 * @author Naama and Yeela
 */

public abstract class RadialGeometry implements Geometry {

    protected final double radius;

    /**
     * Constructor to initialize RadialGeometry based on radius
     * @param r a radius
     */
    public RadialGeometry(double r){
        radius=r;
    }

}

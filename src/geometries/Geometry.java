package geometries;

import primitives.*;

/**
 * An abstract class that represents some geometric body
 *
 * @author Naama and Yeela
 */
public abstract class Geometry extends Intersectable {

    /**
     * {@link Color} of the geometry
     */
    protected Color emission = Color.BLACK;

    /**
     * {@link Material} type of the shape
     */
    private Material material = new Material();

    /**
     * getter for emission field
     *
     * @return {@link  Color} of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * setter for emission field (builder pattern style)
     *
     * @param emission {@link Color} object to set shape's color to
     * @return this instance of object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Calculates and returns the normal
     *
     * @param p A point to calculate the normal
     * @return normal
     */
    public abstract Vector getNormal(Point p);

    /**
     * getter for material field
     *
     * @return geometry's {@link Material} type
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * setter for material field (builder pattern style)
     *
     * @param material {@link Material} object to set geometry's material to
     * @return this instance of object
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}

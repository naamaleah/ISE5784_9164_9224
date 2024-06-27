package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Class RayTracerBase is an abstract class responsible for calculating colors of a scene
 * @author Naama and Yeela
 */
public abstract class RayTracerBase {
    /**
     * {@link Scene} for {@link Color} calculations to be executed on
     */
    protected Scene scene;

    /**
     * parameter constructor
     * @param scene The scene
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     *abstract method - calculate color of a pixel in  image
     * @param ray Ray to be traced
     * @return Color
     */
    public abstract Color traceRay(Ray ray);
}

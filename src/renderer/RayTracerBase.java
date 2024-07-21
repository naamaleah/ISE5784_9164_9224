package renderer;
import primitives.*;
import scene.Scene;

import java.util.List;

/**
 * Class RayTracerBase is an abstract class responsible for calculating colors of a scene
 *
 * @author Naama and Yeela
 */
public abstract class RayTracerBase {
    /**
     * {@link Scene} for {@link Color} calculations to be executed on
     */
    protected Scene scene;

    /**
     * parameter constructor
     *
     * @param scene The scene
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Trace the ray and calculates the color
     * @param ray the ray that came out of the camera
     * @return the color of the object that the ray is interacts with
     */
    public abstract Color traceRay(Ray ray);

    /**
     * Trace the ray and calculates the color of the point that interact with the geometries of the scene
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interacts with
     */
    public abstract Color traceRays(List<Ray> rays);

    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * @param centerP center pixel
     * @param Width Length
     * @param Height width
     * @param minWidth min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright Vector right
     * @param Vup vector up
     * @param prePoints pre Points
     * @return Pixel color
     */
    public abstract Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints);
}

package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 *class implements rayTracer abstract class
 * @author Naama and Yeela
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Parameter constructor
     * @param scene The scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {

        // find the closest intersection point
        Point closestPoint = findClosestIntersection(ray);
        // if no intersection point was found , return basic background color of scene
        if (closestPoint == null)
            return scene.background;

        // intersection was found, calculate color of the of pixel.
        return calcColor();
    }
    private Point findClosestIntersection(Ray ray) {
        // check if ray constructed through the pixel intersects any of geometries
        List<Point> intersections = scene.geometries.findIntersections(ray);

        // return closest point if list is not empty
        return intersections == null ? null : ray.findClosestPoint(intersections);

    }

    private Color calcColor(){
        return this.scene.ambientLight.getIntensity();
    }
}

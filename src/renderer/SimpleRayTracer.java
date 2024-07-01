package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import java.util.stream.Collectors;

import static java.awt.Color.BLACK;
import static primitives.Util.alignZero;

/**
 * class implements rayTracer abstract class
 *
 * @author Naama and Yeela
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;

    /**
     * Parameter constructor
     *
     * @param scene The scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {

        // find the closest intersection point
        GeoPoint closestPoint = findClosestIntersection(ray);
        // if no intersection point was found , return basic background color of scene
        if (closestPoint == null)
            return scene.background;

        // intersection was found, calculate color of the of pixel.
        return calcColor(closestPoint, ray);
    }

    /**
     * find the closest intersection point between ray and geometries in scene
     *
     * @param ray ray constructed from camera to scene
     * @return closest intersection Point
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        // check if ray constructed through the pixel intersects any of geometries
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);

        // return closest point if list is not empty
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);

    }

    /**
     * calculate the color of a pixel
     *
     * @param gp  the {@link GeoPoint} viewed through the pixel to calculate color of
     * @param ray ray of camera through pixel in view plane where the point is located
     * @return color of the pixel
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return scene.ambientLight.getIntensity().add(calcLocalEffects(gp, ray));
    }

    /**
     * Calculates the local effects (diffuse and specular) of a given geometric point on a ray.
     *
     * @param gp  The geometric point at which to calculate the local effects.
     * @param ray The ray being traced.
     * @return The color resulting from the local effects.
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = Util.alignZero(n.dotProduct(v));
        int nShininess = gp.geometry.getMaterial().nShininess;
        Double3 kd = gp.geometry.getMaterial().kD;
        Double3 ks = gp.geometry.getMaterial().kS;
        Color color = new Color(BLACK).add(gp.geometry.getEmission());//new Color(BLACK);
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = Util.alignZero(n.dotProduct(l));
            if ((nl * nv > 0)&& unshaded(gp,lightSource,l,n,nl)) {
                Color intensity = lightSource.getIntensity(gp.point);
                color = color.add(calcDiffusive(kd, l, n, intensity), calcSpecular(ks, l, n, v, nShininess, intensity));
            }
        }
        return color;
    }


    /**
     * Calculates the specular reflection component for a given material.
     *
     * @param ks             The specular reflection coefficient of the material.
     * @param l              The direction of the light source.
     * @param n              The surface normal at the point of reflection.
     * @param v              The direction from the point of reflection towards the viewer.
     * @param nShininess     The shininess factor of the material.
     * @param lightIntensity The intensity of the light source.
     * @return The color resulting from the specular reflection.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(2 * (l.dotProduct(n))));
        double vrMinus = Math.max(0, v.scale(-1).dotProduct(r));
        double vrn = Math.pow(vrMinus, nShininess);
        return lightIntensity.scale(ks.scale(vrn));
    }

    /**
     * Calculates the diffuse reflection component for a given material.
     *
     * @param kd        The diffuse reflection coefficient of the material.
     * @param l         The direction of the light source.
     * @param n         The surface normal at the point of reflection.
     * @param intensity The intensity of the light source.
     * @return The color resulting from the diffuse reflection.
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color intensity) {
        double ln = Math.abs(l.dotProduct(n));
        return intensity.scale(kd.scale(ln));
    }


    /**
     * For a point, light source and the ray between them, the function checks if
     * there is a geometry blocking the light. If so , the poit is considered shaded
     * and light from the light source isn't added to the calculation.
     *
     * @param gp    the point at which the light is being calculated
     * @param light the light source
     * @param l     ray from the point to the light source for which the shading is
     *              being calculated
     * @param n     normal at the point
     * @param nl    dot product of the normal and the ray to the light source
     * @return is the point unshaded relative to this certain light source?
     */
    private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1);
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray ray = new Ray(point, lightDirection);
        double distance = light.getDistance(gp.point);

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if(intersections==null) return true;
        return intersections.stream()
                .filter(g -> distance > g.point.distance(gp.point)).toList().isEmpty();
    }

}

package renderer;

import geometries.Geometry;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import java.util.stream.Collectors;

import static java.awt.Color.BLACK;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * class implements rayTracer abstract class
 *
 * @author Naama and Yeela
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

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
     * Additional calcColor function enabling recursion based on depth and light
     * intensity.
     *
     * @param gp    the point at which to calculate the light
     * @param ray   ray from camera to the point
     * @param level depth of recursion
     * @param k     weight of light at a certain point in recursion
     * @return if bottom level reached - color at the point, otherwise - the color
     * at the point, in addition to the added affects achieved by the
     * deepening of the recursion
     */
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(gp, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(gp, ray, level, k));
    }

    /**
     * Calculates the color at a certain point of intersection by using a separate
     * recursive funtion. This function sends the initial values for the recursion.
     *
     * @param p   intersection point
     * @param ray from camera to the point
     * @return color at the point
     */
    private Color calcColor(GeoPoint p, Ray ray) {
        return calcColor(p, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }

    /**
     * Calculates the color at a certain point as caused by local effects (the
     * result of various light sources and the geometry's emission light). This is
     * done using the Phong model for light propagation.
     *
     * @param gp  the point at which the light intensity is calculated
     * @param ray camera ray for which the aforementioned GeoPoint was found
     * @return the color at the point
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        Material mat = gp.geometry.getMaterial();
// returns no color in the case that camera ray is perpendicular to the normal
        // at the point's location
        if (nv == 0)
            return color;

        // For each light source in the scene, if the light is on the same side of the
        // object as the camera, it's affect on the point's color is added
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (alignZero(nl * nv) > 0) {
                // Checks shade percentage between the light source and the point and only
                // calculates the intensity from a given light as a factor of the transparency.
                Double3 ktr = transparency(gp, lightSource,l, n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(mat, nl)), iL.scale(calcSpecular(mat, n, l, nl, v)));
                }
            }
        }
        return color;
    }


    /**
     * Calculates the effect of diffusive light return at the point.
     *
     * @param mat the material of the geometry the point belongs to
     * @param nl  dot product of the normal at the point and the ray from the light
     *            source to the point
     * @return the diffusive color return at the point
     */
    private Double3 calcDiffusive(Material mat, double nl) {
        return mat.kD.scale(nl < 0 ? -nl : nl);

    }


    /**
     * For a point, light source and the ray between them, the function checks if
     * there is a geometry blocking the light. If so , the poit is considered shaded
     * and light from the light source isn't added to the calculation.
     *
     * @param gp    the point at which the light is being calculated
     * @param l     ray from the point to the light source for which the shading is
     *              being calculated
     * @param n     normal at the point
     * @param nl    dot product of the normal and the ray to the light source
     * @param light the light source
     * @return is the point unshaded relative to this certain light source?
     */
    protected boolean unshaded(GeoPoint gp, Vector l, Vector n, double nl, LightSource light) {
        Vector lightDir = l.scale(-1);
        Ray lightRay = new Ray(gp.point, lightDir, n);

        var intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        if (intersections == null)
            return true;
        for (GeoPoint p : intersections)
            if (!p.geometry.getMaterial().kT.equals(Double3.ZERO))
                return false;
        return true;
    }


    /**
     * Calculates the effect of specular light return at the point.
     *
     * @param mat the material of the geometry the point belongs to
     * @param n   normal at the point
     * @param l   the ray from the light source to the point in question
     * @param nl  dot product of the normal at the point and the ray from the light
     *            source to the point
     * @param v   the direction of the camera ray
     * @return the specular color return at the point
     */
    private Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
        double vr = v.dotProduct(l.subtract(n.scale(nl * 2)));
        return (alignZero(vr) > 0) ? Double3.ZERO : mat.kS.scale(Math.pow(-vr, mat.nShininess));
    }

    /**
     * Recursively returns the added affects for two additional rays emanating from
     * the intersection point: the reflected ray and the refracted ray.
     *
     * @param gp    the point from which to send the secondary rays
     * @param ray   the from the camera to the intersection point
     * @param level the depth of recursion
     * @param k     the weight of light at the current point in the recursion
     * @return the color obtained from the secondary rays.
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        Double3 kR = material.kR;
        Double3 kT = material.kT;

        return calcGlobalEffect(constructRefractedRay(gp.point, v, n),level, material.kT, k)
                .add(calcGlobalEffect(constructReflectedRay(gp.point, v, n),level, material.kR, k));

    }

    /**
     * given a ray , construct a new ray which is reflective to original ray
     *
     * @param p {@link Point} that original ray intersects
     * @param N normal {@link Vector} to geometry at the point
     * @param l light direction {@link Vector} (the original ray)
     * @return reflective {@link Ray}
     */
    private Ray constructReflectedRay(Point p, Vector N, Vector l) {
        // dot product of N and ray
        double nDotL = alignZero(N.dotProduct(l));
        // reflected ray = L - (2 *  l.dorProduct(n)) * n
        Vector r = l.subtract(N.scale(2 * nDotL)).normalize();
        return new Ray(p, N, r);
    }

    /**
     * given a ray, construct from it a new refracted ray
     *
     * @param p {@link Point} that original ray intersects
     * @param N normal {@link Vector} to geometry at the point
     * @param l light direction {@link Vector} (the original ray)
     * @return refracted {@link Ray}
     */
    private Ray constructRefractedRay(Point p, Vector N, Vector l) {
        return new Ray(p, N, l);
    }



    /**
     * Checks that the weight of the light intensity at this point in the recursion
     * is above the minimum (if not, black is returned) and proceeds to find the
     * nearest intersection along that ray, adding it's affects if necessary,
     * returning black or the background otherwise (if the camera ray is
     * perpendicular to the normal or no intersection point exists, accordingly).
     *
     * @param ray   camera ray
     * @param level depth of recursion
     * @param k     current weight for light intensity at this point in recursion
     * @param kx    weight to be added given the increasing level of recursion
     * @return the color to be returned along the given secondary ray.
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        // Checks if weight for light intensity is below minimum
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        // Returns background color if no intersection points are found
        if (gp == null)
            return scene.background.scale(kx);
        // If they are, color at the nearest intersection is returned
        return isZero(gp.geometry.getNormal(gp.point).dotProduct(ray.getDirection())) ? Color.BLACK
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     * calculate transparency of a point (shade)
     *
     * @param gp    {@link GeoPoint} to calculate transparency for
     * @param light {@link LightSource} lighting towards the geometry
     * @param l     normal {@link Vector} to geometry at the point
     * @param n     light direction {@link Vector} (the original ray)
     * @return {@link Double3} value of transparency at point
     */
    private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n) {
        // create a vector by scaling  light direction vector to opposite direction
        // now originating from point towards light
        Vector lightScaled = l.scale(-1);
        // construct a new ray using the scaled vector from the point towards ray
        // slightly removed from original point by epsilon (in Ray class)
        Ray shadowRay = new Ray(gp.point, n, lightScaled);
        // get distance from the light to the point
        double lightDistance = light.getDistance(shadowRay.getHead());
        // check if new ray intersect a geometry between point and the light source
        // further objects behind the light are avoided by distance parameter
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay, lightDistance);
        // point is not shaded - return transparency level of 1
        if (intersections == null)
            return Double3.ONE;

        // point is shaded - iterate through intersection points and add the shade effect from geometry
        //to transparency level at point
        Double3 ktr = Double3.ONE;
        for (var geoPoint : intersections) {
            ktr = ktr.product(geoPoint.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }
        // return the transparency
        return ktr;
    }


}

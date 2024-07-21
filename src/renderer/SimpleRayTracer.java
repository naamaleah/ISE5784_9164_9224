package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import java.util.List;
import static primitives.Util.alignZero;

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

        GeoPoint closestPoint = findClosestIntersection(ray);

        if (closestPoint == null)
            return scene.background;

        return calcColor(closestPoint, ray);
    }

    /**
     * find the closest intersection point between ray and geometries in scene
     *
     * @param ray ray constructed from camera to scene
     * @return closest intersection Point
     */
    private GeoPoint findClosestIntersection(Ray ray) {

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
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

        if (nv == 0) return color;

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
     * Calculates reflected ray and refraction ray
     *
     * @param geoPoint geometry point
     * @param ray      ray
     * @param k        k value
     * @return color
     */
    private Color calcGlobalEffects(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = geoPoint.geometry.getMaterial();
        Ray reflectedRay = constructReflectedRay(geoPoint, geoPoint.geometry.getNormal(geoPoint.point), ray.getDirection());
        Ray refractedRay = constructRefractedRay(geoPoint, geoPoint.geometry.getNormal(geoPoint.point), ray.getDirection());
        return calcGlobalEffect( level, color, material.kR, k, reflectedRay)
                .add(calcGlobalEffect( level, color, material.kT, k, refractedRay));
    }

    /**
     * function will construct a reflection ray
     *
     * @param geoPoint geometry point to check
     * @param normal   normal vector
     * @param vector   direction of ray to point
     * @return reflection ray
     */
    private Ray constructReflectedRay(GeoPoint geoPoint, Vector normal, Vector vector) {
        Vector reflectedVector = vector.subtract(normal.scale(2 * vector.dotProduct(normal)));
        return new Ray(geoPoint.point, reflectedVector, normal);
    }

    /**
     * function will construct a refraction ray
     *
     * @param geoPoint geometry point to check
     * @param normal   normal vector
     * @param vector   direction of ray to point
     * @return refraction ray
     */
    private Ray constructRefractedRay(GeoPoint geoPoint, Vector normal, Vector vector) {
        return new Ray(geoPoint.point, vector, normal);
    }



    /**
     * function calculates global effects of color on point
     *
     * @param level    level of recursion
     * @param color    color until now
     * @param kx       kx value of material
     * @param k        k value until now
     * @param ray      ray that intersects
     * @return color
     */
    private Color calcGlobalEffect( int level, Color color, Double3 kx, Double3 k, Ray ray) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint reflectedPoint = findClosestIntersection(ray);
        if (reflectedPoint != null) {
            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
        }
        return color;
    }

    /**
     * function will return double that represents transparency
     *
     * @param geoPoint    geometry point to check
     * @param lightSource light source
     * @param l           light vector
     * @param n           normal vector
     * @return transparency value
     */
    private Double3 transparency(GeoPoint geoPoint, LightSource lightSource, Vector l, Vector n) {


        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        Double3 ktr = Double3.ONE;
        if (intersections == null) return ktr;

        double distance = lightSource.getDistance(geoPoint.point);

        for (GeoPoint intersection : intersections) {

            if (distance > intersection.point.distance(geoPoint.point)) {
                ktr = ktr.product(intersection.geometry.getMaterial().kT);
            }
        }
        return ktr;
    }


}


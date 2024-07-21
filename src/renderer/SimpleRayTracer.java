package renderer;

import geometries.Geometry;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.LinkedList;
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
     * Trace the ray and calculates the color of the point that interact with the geometries of the scene
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color traceRays(List<Ray> rays) {
        Color color=new Color(BLACK);
        for(Ray ray : rays)
        {
            GeoPoint clossestGeoPoint = findClosestIntersection(ray);
            if (clossestGeoPoint == null)
                color= color.add( scene.background);
            else color= color.add(calcColor(clossestGeoPoint,ray));
        }
        return color.reduce(rays.size());
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
     * Calculates the global effect of a ray beam (list of rays) and avaraages the
     * result.
     *
     * @param level the depth of recursion
     * @param k     the weight of light at the current point in the recursion
     * @param kB    the attenuation factor of the geometry of the ray beam origin
     * @param rays  the list of rays
     * @return the averaged color of the ray beam
     */
    private Color calcRayBeamColor(int level, Double3 k, Double3 kB, List<Ray> rays) {
        int size = rays.size();
        if (size == 0) return Color.BLACK;

        if (rays.size() == 1)
            return calcGlobalEffect(rays.get(0), level, k, kB);

        Color color = Color.BLACK;
        for (Ray rT : rays)
            color = color.add(calcGlobalEffect(rT, level, k, kB));

        return color.reduce(size);
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

        return calcRayBeamColor(level, k, kR, constructReflectedRays(gp, v, n, material.kG))
                .add(calcRayBeamColor(level, k, kT, constructRefractedRays(gp, v, n, material.kB)));

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
     * @param geoPoint geometry point to color
     * @param level    level of recursion
     * @param color    color until now
     * @param kx       kx value of material
     * @param k        k value until now
     * @param ray      ray that intersects
     * @return color
     */
    private Color calcGlobalEffect(GeoPoint geoPoint, int level, Color color, Double3 kx, Double3 k, Ray ray) {
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


    /**
     * Constructs a list of rays that are refracted from the given ray. filtering
     * out those that are not in the same direction as the main ray. (spead is
     * determined by kB)
     *
     * @param gp the point from which the rays are refracted
     * @param v  the direction of the main ray
     * @param n  the normal at the point
     * @param kB the Blur factor
     * @return the list of rays
     */
    private List<Ray> constructRefractedRays(GeoPoint gp, Vector v, Vector n, double kB) {
        Ray rfRay = constructRefractedRay(gp, v, n);
        double res = rfRay.getDirection().dotProduct(n);
        return kB == 0 ? List.of(rfRay)
                : new TargetArea(rfRay, kB).constructRayBeamGrid().stream()
                .filter(r -> r.getDirection().dotProduct(n) * res > 0).collect(Collectors.toList());
    }

    /**
     * Constructs a list of rays that are reflected from the given ray. filtering
     * out those that are not in the same direction as the main ray. (spead is
     * determined by kG)
     *
     * @param gp the point from which the rays are reflected
     * @param v  the direction of the main ray
     * @param n  the normal at the point
     * @param kG the Blur factor
     * @return the list of rays
     */
    private List<Ray> constructReflectedRays(GeoPoint gp, Vector v, Vector n, double kG) {
        Ray rfRay = constructReflectedRay(gp, v, n);
        double res = rfRay.getDirection().dotProduct(n);
        return kG == 0 ? List.of(rfRay)
                : new TargetArea(rfRay, kG).constructRayBeamGrid().stream()
                .filter(r -> r.getDirection().dotProduct(n) * res > 0).collect(Collectors.toList());
    }




    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * @param centerP center pixl
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
    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc))) ;
        }

        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;
        for (int i = -1; i <= 1; i += 2){
            for (int j = -1; j <= 1; j += 2) {
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                cornersList.add(tempCorner);
                if (prePoints == null || !prePoints.contains(tempCorner)) {
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));
                    colorList.add(traceRay(tempRay));
                }
            }
        }


        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }


        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        if (isAllEquals && colorList.size() > 1)
            return tempColor;


        tempColor = primitives.Color.BLACK;
        for (Point center : nextCenterPList) {
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width/2,  Height/2,  minWidth,  minHeight ,  cameraLoc, Vright, Vup, cornersList));
        }
        return tempColor.reduce(nextCenterPList.size());


    }
}


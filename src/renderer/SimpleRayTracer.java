package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import static java.lang.Math.*;

import java.util.LinkedList;
import java.util.List;

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
     * Declares whether the ray tracer should create soft shadow rays
     */
    private boolean isSoftShadow = false;
    /**
     * Declares how many soft shadow rays per point to create, if applicable
     */
    private int numOfSSRays = 10;
    /**
     * The radius of the beam for rays of soft shadow
     */
    private double radiusBeamSS = 1;


    /**
     * Parameter constructor
     *
     * @param scene The scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }


    /**
     * Sets the isSoftShadow used
     *
     * @return RayTracerBase Object
     */
    public SimpleRayTracer useSoftShadow() {
        this.isSoftShadow = true;
        return this;
    }

    /**
     * Sets the number of soft shadow rays
     *
     * @param num number of rays
     * @return RayTracerBase Object
     */
    public SimpleRayTracer setNumOfSSRays(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Number of rays must be greater than 0");
        }

        this.numOfSSRays = num;
        return this;
    }

    /**
     * Sets the radius for the beam
     *
     * @param r the radius
     * @return RayTracerBase Object
     */
    public SimpleRayTracer setRadiusBeamSS(double r) {
        if (r <= 0) {
            throw new IllegalArgumentException("Radius of beam must be greater than 0");
        }

        this.radiusBeamSS = r;
        return this;
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        if (closestPoint == null)
            return scene.background;

        return calcColor(closestPoint, ray);
    }

    /**
     * Trace the ray and calculates the color of the point that interact with the geometries of the scene
     *
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color traceRays(List<Ray> rays) {
        Color color = new Color(BLACK);
        for (Ray ray : rays) {
            GeoPoint clossestGeoPoint = findClosestIntersection(ray);
            if (clossestGeoPoint == null)
                color = color.add(scene.background);
            else color = color.add(calcColor(clossestGeoPoint, ray));
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
        Color color = calcLocalEffect(gp, ray.getDirection(), k);
        return 1 == level ? color : color.add(calcGlobalEffects(gp, ray.getDirection(), level, k));
    }

    /**
     * Calculates the color at a certain point of intersection by using a separate
     * recursive function. This function sends the initial values for the recursion.
     *
     * @param p   intersection point
     * @param ray from camera to the point
     * @return color at the point
     */
    private Color calcColor(GeoPoint p, Ray ray) {
        return calcColor(p, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }

    /**
     * recursive function calculates the global effects of objects on a certain point
     *
     * @param intersection the point
     * @param inRay        direction of ray from the camera to point
     * @param level        level of recursion
     * @param k            the level of light
     * @return the color
     */
    private Color calcGlobalEffects(GeoPoint intersection, Vector inRay, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector n = intersection.geometry.getNormal(intersection.point);

        Material material = intersection.geometry.getMaterial();
        Double3 kr = material.kR;
        Double3 kkr = kr.product(k);

        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflectedRay(intersection.point, n, inRay);
            color = color.add(calcGlobalEffect(reflectedRay, level, kr, kkr));

        }

        Double3 kt = material.kT;
        Double3 kkt = kt.product(k);

        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefractedRay(n, inRay, intersection.point);

            color = color.add(calcGlobalEffect(refractedRay, level, kt, kkt));
        }
        return color;
    }

    /**
     * calculates global effects recursively
     *
     * @param ray   the ray from the viewer
     * @param level level of recursion
     * @param kx    k attenuation
     * @param kkx   k times attenuation
     * @return the color
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection(ray);

        if (gp == null) {
            return scene.background;
        }

        return calcColor(gp, ray, level - 1, kkx).scale(kx);
    }


    /**
     * Calculate the local effect of light sources on a point
     *
     * @param intersection the point
     * @param v            the direction of the ray from the viewer
     * @param k            the kR or kT factor at this point
     * @return the color
     */
    private Color calcLocalEffect(GeoPoint intersection, Vector v, Double3 k) {
        Vector n = intersection.geometry.getNormal(intersection.point);

        double nv = alignZero(n.dotProduct(v));
        if (isZero(nv)) {
            return Color.BLACK;
        }

        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;

        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Double3 ktr;
                if (!isSoftShadow)
                    ktr = transparency(intersection, lightSource, l, n);
                else
                    ktr = transparencySS(intersection, lightSource, n);

                if (!(ktr.product(k)).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }


    /**
     * Calculate the diffuse light effect on the point
     *
     * @param kd             diffuse attenuation factor
     * @param l              the direction of the light
     * @param n              normal from the point
     * @param lightIntensity the intensity of the light source at this point
     * @return the color
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(abs(l.dotProduct(n)));
        return lightIntensity.scale(kd.scale(ln));
    }

    /**
     * Calculate the specular light at this point
     *
     * @param ks             specular attenuation factor
     * @param l              the direction of the light
     * @param n              normal from the point
     * @param v              direction of the viewer
     * @param nShininess     shininess factor of the material at the point
     * @param lightIntensity the intensity of the light source at the point
     * @return the color of the point
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n));
        Vector r = l.subtract(n.scale(2 * ln)).normalize();
        double vr = alignZero(v.dotProduct(r));
        double vrnsh = pow(max(0, -vr), nShininess);
        return lightIntensity.scale(ks.scale(vrnsh));
    }


//    /**
//     * For a point, light source and the ray between them, the function checks if
//     * there is a geometry blocking the light. If so , the poit is considered shaded
//     * and light from the light source isn't added to the calculation.
//     *
//     * @param gp    the point at which the light is being calculated
//     * @param l     ray from the point to the light source for which the shading is
//     *              being calculated
//     * @param n     normal at the point
//     * @param nl    dot product of the normal and the ray to the light source
//     * @param light the light source
//     * @return is the point unshaded relative to this certain light source?
//     */
//    protected boolean unshaded(GeoPoint gp, Vector l, Vector n, double nl, LightSource light) {
//        Vector lightDir = l.scale(-1);
//        Ray lightRay = new Ray(gp.point, lightDir, n);
//
//        var intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
//        if (intersections == null)
//            return true;
//        for (GeoPoint p : intersections)
//            if (!p.geometry.getMaterial().kT.equals(Double3.ZERO))
//                return false;
//        return true;
//    }


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


        Vector lightDirection = l.scale(-1);
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



    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc)));
        }

        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;
        for (int i = -1; i <= 1; i += 2) {
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
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width / 2, Height / 2, minWidth, minHeight, cameraLoc, Vright, Vup, cornersList));
        }
        return tempColor.reduce(nextCenterPList.size());


    }

    /**
     * function will construct a reflection ray
     *
     * @param point  geometry point to check
     * @param normal normal vector
     * @param vector direction of ray to point
     * @return reflection ray
     */
    private Ray constructReflectedRay(Point point, Vector normal, Vector vector) {
        Vector reflectedVector = vector.subtract(normal.scale(2 * vector.dotProduct(normal)));
        return new Ray(point, reflectedVector, normal);
    }


    /**
     * Construct a refractive ray from a point
     *
     * @param n normal to the point
     * @param v direction of ray to the point
     * @param p point
     * @return new Ray
     */
    private Ray constructRefractedRay(Vector n, Vector v, Point p) {
        return new Ray(p, v, n);
    }


    /**
     * Returns transparency level with soft shadow effect
     * Constructs random rays around the light and gets the average of all the levels
     *
     * @param gp the point to check
     * @param ls the current light source
     * @param n  normal to the point
     * @return average ktr
     */
    private Double3 transparencySS(GeoPoint gp, LightSource ls, Vector n) {
        Double3 ktr = Double3.ZERO;
        List<Vector> LVectors = ls.getLCircle(gp.point, radiusBeamSS, numOfSSRays);

        for (Vector v : LVectors) {
            ktr = ktr.add(transparency(gp, ls, v, n));
        }

        ktr = ktr.reduce(LVectors.size());

        return ktr;
    }


}


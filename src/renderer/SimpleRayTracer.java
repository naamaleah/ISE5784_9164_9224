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
        return calcGlobalEffect(geoPoint, level, color, material.kR, k, reflectedRay)
                .add(calcGlobalEffect(geoPoint, level, color, material.kT, k, refractedRay));
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


//    /**
//     * Building a beam of rays for transparency and reflection
//     * @param ray The beam coming out of the camera
//     * @param direction the vector
//     * @param glossy The amount of gloss
//     * @param n normal
//     * @return Beam of rays
//     */
//    List<Ray> raysGrid(Ray ray, int direction, double glossy, Vector n){
//        int numOfRowCol = isZero(glossy)? 1: (int)Math.ceil(Math.sqrt(glossinessRaysNum));
//        if (numOfRowCol == 1) return List.of(ray);
//        Vector Vup ;
//        double Ax= Math.abs(ray.getDirection().getX()), Ay= Math.abs(ray.getDirection().getY()), Az= Math.abs(ray.getDirection().getZ());
//        if (Ax < Ay)
//            Vup= Ax < Az ?  new Vector(0, -ray.getDirection().getZ(), ray.getDirection().getY()) :
//                    new Vector(-ray.getDirection().getY(), ray.getDirection().getX(), 0);
//        else
//            Vup= Ay < Az ?  new Vector(ray.getDirection().getZ(), 0, -ray.getDirection().getX()) :
//                    new Vector(-ray.getDirection().getY(), ray.getDirection().getX(), 0);
//        Vector Vright = Vup.crossProduct(ray.getDirection()).normalize();
//        Point pc=ray.getPoint(distanceGrid);
//        double step = glossy/sizeGrid;
//        Point pij=pc.add(Vright.scale(numOfRowCol/2*-step)).add(Vup.scale(numOfRowCol/2*-step));
//        Vector tempRayVector;
//        Point Pij1;
//
//        List<Ray> rays = new ArrayList<>();
//        rays.add(ray);
//        for (int i = 1; i < numOfRowCol; i++) {
//            for (int j = 1; j < numOfRowCol; j++) {
//                Pij1=pij.add(Vright.scale(i*step)).add(Vup.scale(j*step));
//                tempRayVector =  Pij1.subtract(ray.getHead());
//                if(n.dotProduct(tempRayVector) < 0 && direction == 1) //refraction
//                    rays.add(new Ray(ray.getHead(), tempRayVector));
//                if(n.dotProduct(tempRayVector) > 0 && direction == -1) //reflection
//                    rays.add(new Ray(ray.getHead(), tempRayVector));
//            }
//        }
//
//        return rays;
//    }




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


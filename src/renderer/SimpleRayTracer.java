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
    private double radiusBeamSS = 10;

    /**
     * Declares whether to use glossiness
     */
    private boolean isGlossy = false;
    /**
     * The number of glossiness rays to create
     */
    private int numOfGlossinessRays = 100;


    /**
     * Parameter constructor
     *
     * @param scene The scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Sets the isGlossiness used
     *
     * @param flag true or false
     * @return RayTracerBasic object
     */
    public SimpleRayTracer useGlossiness(boolean flag) {
        this.isGlossy = flag;
        return this;
    }

    /**
     * Set the number og glossiness rays
     *
     * @param numOfGlossinessRays number of glossiness rays per point
     * @return this
     */
    public SimpleRayTracer setNumOfGlossinessRays(int numOfGlossinessRays) {
        if (numOfGlossinessRays <= 0) {
            throw new IllegalArgumentException("number of glossiness rays should be greater than 0");
        }

        this.numOfGlossinessRays = numOfGlossinessRays;
        return this;
    }

    /**
     * Sets the isSoftShadow used
     *
     * @param flag true or false
     * @return RayTracerBase Object
     */
    public SimpleRayTracer useSoftShadow(boolean flag) {
        this.isSoftShadow = flag;
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
        Color color = calcLocalEffect(gp, ray.getDirection(), k);
        return 1 == level ? color : color.add(calcGlobalEffects(gp, ray.getDirection(), level, k));
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
     * recursive function calculates the global effects of objects on a certain point
     *
     * @param intersection the point
     * @param inRay        direction of ray from the camera to point
     * @param level        level of recursion
     * @param k            the level of light
     * @return the color
     */
    private Color calcGlobalEffects(GeoPoint intersection, Vector inRay, int level, Double3 k) {
        Color color = Color.BLACK; //base color
        Vector n = intersection.geometry.getNormal(intersection.point); //normal

        Material material = intersection.geometry.getMaterial();

        //reflection attenuation of the material
        Double3 kr = material.kR;
        //reflection level as affected by k
        Double3 kkr = kr.product(k);

        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) { //if the reflection level is not lower than the minimum
            //construct a reflection  ray from the point
            if (!isGlossy) { //if its not set to glossy construct just one reflected ray
                Ray reflectedRay = constructReflectedRay(n, inRay, intersection.point);

                //add this color to the point by recursively calling calcGlobalEffect
                color = color.add(calcGlobalEffect(reflectedRay, level, kr, kkr));

            } else { //otherwise construct multiple glossy rays
                List<Ray> reflectedRays = constructReflectedRays(n, inRay, intersection.point, material.kG);

                //for each reflected glossy ray, calc color
                for (Ray reflectedRay : reflectedRays) {
                    color = color.add(calcGlobalEffect(reflectedRay, level, kr, kkr).reduce(reflectedRays.size()));
                }
            }
        }

        //transparency  attenuation factor of the material
        Double3 kt = material.kT;
        //transparency level
        Double3 kkt = kt.product(k);

        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {//if the transparency level is not lower than the minimum
            if (!isGlossy) { //if its not set to glossy construct just one refracted ray
                //construct a refracted ray from the point
                Ray refractedRay = constructRefractedRay(n, inRay, intersection.point);

                //add to the color to the point by recursively calling calcGlobalEffect
                color = color.add(calcGlobalEffect(refractedRay, level, kt, kkt));
            } else { //otherwise construct multiple refracted rays
                //get list of refracted rays
                List<Ray> refractedRays = constructRefractedRays(n, inRay, intersection.point, material.kG);

                //for each refeacted ray, calc the color
                for (Ray refractedRay : refractedRays) {
                    color = color.add(calcGlobalEffect(refractedRay, level, kt, kkt).reduce(refractedRays.size()));
                }
            }
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

        double nv = alignZero(n.dotProduct(v)); //nv=n*v
        if (isZero(nv)) {
            return Color.BLACK;
        }

        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;

        Color color = intersection.geometry.getEmission(); //base color

        //for each light source in the scene
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point); //the direction from the light source to the point
            double nl = alignZero(n.dotProduct(l)); //nl=n*l

            //if sign(nl) == sign(nv) (if the light hits the point add it, otherwise don't add this light)
            if (nl * nv > 0) {
                //ktr is the level of shade on the point (according to transparency of material)
                Double3 ktr;
                if (!isSoftShadow) { //if soft shadow is not activated, get the regular transparency
                    ktr = transparency(intersection, lightSource, l, n);
                } else { //otherwise get the transparency level according to soft shadow
                    ktr = transparencySS(intersection, lightSource, n);
                }
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
        double ln = alignZero(abs(l.dotProduct(n))); //ln=|l*n|
        return lightIntensity.scale(kd.scale(ln)); //Kd * |l * n| * Il
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
        double ln = alignZero(l.dotProduct(n)); //ln=l*n
        Vector r = l.subtract(n.scale(2 * ln)).normalize(); //r=l-2*(l*n)*n
        double vr = alignZero(v.dotProduct(r)); //vr=v*r
        double vrnsh = pow(max(0, -vr), nShininess); //vrnsh=max(0,-vr)^nshininess
        return lightIntensity.scale(ks.scale(vrnsh)); //Ks * (max(0, - v * r) ^ Nsh) * Il
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

    /**
     * Constructs a reflection ray from an intersection
     *
     * @param n normal to the point
     * @param v direction of the ray to the point
     * @param p the [point
     * @return new Ray
     */
    private Ray constructReflectedRay(Vector n, Vector v, Point p) {
        double vn = alignZero(v.dotProduct(n)); //v*n

        if (isZero(vn)) {
            return null;
        }

        Vector vnn = n.scale(2 * vn);// n*2*vn
        Ray r = new Ray(p, v.subtract(vnn), n); //new Ray{point,v-2*(v*n)*n}
        return r;
    }

    /**
     * Constructs randomized reflection rays at the intersection point according to kG.
     * If kG is 1 then only one ray is returned with the specular vector
     *
     * @param p  the intersection point
     * @param v  the intersection's ray direction
     * @param n  the normal at the intersection point
     * @param kG the glossiness parameter in range of [0,1], where 0 - matte, 1 - glossy
     * @return randomized reflection rays
     */
    private List<Ray> constructReflectedRays(Vector n, Vector v, Point p, double kG) {
        Ray r = constructReflectedRay(n, v, p);

        List<Ray> result = new LinkedList<>();
        result.add(r);

        // If kG is equals to 1 then return only 1 ray, the specular ray (r)
        if (isZero(kG - 1)) {
            return result;
        }

        List<Vector> randomizedVectors = createRandomVectorsOnSphere(n);

        // If kG is equals to 0 then select all the randomized vectors
        if (isZero(kG)) {
            for (Vector vec : randomizedVectors) {
                result.add(new Ray(p, vec, n));
            }
            return result;
        }

        // If kG is in range (0,1) then move the randomized vectors towards the specular vector (v)
        Vector d = r.getDirection();
        for (Vector vec : randomizedVectors) {
            vec = vec.scale(1 - kG);
            vec = vec.add(d.scale(kG));
            result.add(new Ray(p, vec, n));
        }
        return result;
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
     * Constructs randomized refraction rays at the intersection point according to kG.
     * If kG is 1 then only one ray is returned with the vector v (which is the specular vector).
     *
     * @param p  the intersection point
     * @param v  the intersection's ray direction
     * @param n  the normal at the intersection point
     * @param kG the glossiness parameter in range of [0,1], where 0 - matte, 1 - glossy
     * @return randomized refraction rays
     */
    private List<Ray> constructRefractedRays(Vector n, Vector v, Point p, double kG) {
        Ray r = constructRefractedRay(n, v, p);
        List<Ray> result = new LinkedList<>();
        result.add(r);

        // If kG is equals to 1 then return only 1 ray, the specular ray (v)
        if (isZero(kG - 1)) {
            return result;
        }

        List<Vector> randomizedVectors = createRandomVectorsOnSphere(n);

        // If kG is equals to 0 then select all the randomized vectors
        if (isZero(kG)) {
            for (Vector vec : randomizedVectors) {
                result.add(new Ray(p, vec, n));
            }
            return result;
        }

        // If kG is in range (0,1) then move the randomized vectors towards the specular vector (v)
        for (Vector vec : randomizedVectors) {
            vec = vec.scale(1 - kG);
            vec = vec.add(v.scale(kG));
            result.add(new Ray(p, vec, n));
        }
        return result;
    }

    /**
     * Creates random vectors on the unit hemisphere with a given normal on the hemisphere's bottom.<br>
     * source: https://my.eng.utah.edu/~cs6958/slides/pathtrace.pdf#page=18
     *
     * @param n normal to the hemisphere's bottom
     * @return the randomized vectors
     */
    private List<Vector> createRandomVectorsOnSphere(Vector n) {
        // pick axis with the smallest component in normal in order to prevent picking
        // an axis parallel to the normal and consequently creating zero vector
        Vector axis;
        if (Math.abs(n.getX()) < Math.abs(n.getY()) && Math.abs(n.getX()) < Math.abs(n.getZ())) {
            axis = new Vector(1, 0, 0);
        } else if (Math.abs(n.getY()) < Math.abs(n.getZ())) {
            axis = new Vector(0, 1, 0);
        } else {
            axis = new Vector(0, 0, 1);
        }

        // find two vectors orthogonal to the normal
        Vector x = n.crossProduct(axis);
        Vector z = n.crossProduct(x);

        List<Vector> randomVectors = new LinkedList<>();

        //  Vector[] randomVectors = new Vector[numOfVectors];
        for (int i = 0; i < numOfGlossinessRays; i++) {
            // pick a point on the hemisphere bottom
            double u, v, u2, v2;
            do {
                u = random() * 2 - 1;
                v = random() * 2 - 1;
                u2 = u * u;
                v2 = v * v;
            } while (u2 + v2 >= 1);

            // calculate the height of the point
            double w = Math.sqrt(1 - u2 - v2);

            // create the new vector according to the base (x, n, z) and the coordinates (u, w, v)
            randomVectors.add(x.scale(u).add(z.scale(v)).add(n.scale(w)));
        }
        return randomVectors;
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
        List<Vector> vecs = ls.getLCircle(gp.point, radiusBeamSS, numOfSSRays);

        for (Vector v : vecs) { //for each vector, add the transparency level there
            ktr = ktr.add(transparency(gp, ls, v, n));
        }

        ktr = ktr.reduce(vecs.size()); //get the average of all the transparency levels of all the vectors

        return ktr;
    }


}


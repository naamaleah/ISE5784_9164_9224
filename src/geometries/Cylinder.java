package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Class Cylinder is the  class representing a Cylinder of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Cylinder extends Tube {

    private final double height;

    /**
     * Constructor to initialize Cylinder based on radius,ray and height
     *
     * @param r      for radius
     * @param axis   for ray
     * @param height for  height
     */
    public Cylinder(double r, Ray axis, double height) {
        super(r, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p) {
        Vector direction = this.axis.getDirection();
        Point P0 = this.axis.getHead();

        //given point is on base of cylinder
        if (p.equals(P0) || isZero(p.subtract(P0).dotProduct(direction)))
            return direction.normalize().scale(-1);


        // given point is on top base of the cylinder
        if (p.equals(P0.add(direction.scale(height))) ||
                isZero(p.subtract(P0.add(direction.scale(height))).dotProduct(direction)))
            return direction.normalize();

        // given point is on the circumference of cylinder
        return super.getNormal(p);
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

    /**
     * find intersection points between ray and 3D cylinder
     * @param ray ray towards the sphere
     * @return immutable list containing 0/1/2 intersection points as {@link GeoPoint}s objects
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray ,double maxDistance) {
        // origin point of cylinder (on bottom base)
        Point basePoint=axis.getHead();
        // point across base point on top base
        Point topPoint =axis.getPoint(height);
        // direction vector of cylinder (orthogonal to base point)
        Vector vC=axis.getDirection();

        // find intersection points of ray with bottom base of cylinder
        List<GeoPoint> result= new LinkedList<>();
        // crate plane that contains base point in it
        Plane basePlane= new Plane(basePoint,vC);
        // find intersection between ray and plane
        List<GeoPoint> intersectionsBase=basePlane.findGeoIntersections(ray,maxDistance);

        // if intersections were found, check that point are actually on the base of the cylinder
        //if distance from base point to intersection point holds the equation ->  distance² < from radius²
        if(intersectionsBase!=null){
            for (GeoPoint p:intersectionsBase) {
                Point pt = p.point;
                // intersection point is the base point itself
                if(pt.equals(basePoint))
                    result.add(new GeoPoint(this,basePoint));
                    // intersection point is different to base point but is on the bottom base
                else if(pt.subtract(basePoint).dotProduct(pt.subtract(basePoint))<radius*radius)
                    result.add(new GeoPoint(this,pt));
            }
        }

        // find intersection points of ray with bottom base of cylinder
        // crate plane that contains top point in it
        Plane topPlane= new Plane(topPoint,vC);
        // find intersection between ray and plane
        List<GeoPoint> intersectionsTop=topPlane.findGeoIntersections(ray,maxDistance);
        // if intersections were found, check that point are actually on the base of the cylinder
        //if distance from top point to intersection point holds the equation ->  distance² < from radius²
        if(intersectionsTop!=null){
            for (var p:intersectionsTop) {
                Point pt = p.point;
                // intersection point is the top point itself
                if(pt.equals(topPoint))
                    result.add(new GeoPoint(this,topPoint));
                    // intersection point is different to base point but is on the bottom base
                else if(pt.subtract(topPoint).dotProduct(pt.subtract(topPoint))<radius*radius)
                    result.add(new GeoPoint(this,pt));
            }
        }

        // if rsy intersects both bases , no other intersections possible - return the result list
        if (result.size()==2)
            return result;

        // use tube parent class function to find intersections with the cylinder represented
        // as an infinite tube
        List<GeoPoint> intersectionsTube=super.findGeoIntersectionsHelper(ray ,maxDistance);

        // if intersection points were found check that they are within the finite cylinder's boundary
        // by checking if  scalar product fo direction vector with a vector from intersection point
        // to bottom base point is positive, and scalar product of direction vector with a
        // vector from intersection point to top base point is negative
        if(intersectionsTube!=null){
            for (var p:intersectionsTube){
                Point pt = p.point;
                if(vC.dotProduct(pt.subtract(basePoint))>0 && vC.dotProduct(pt.subtract(topPoint))<0)
                    result.add(new GeoPoint(this,pt));
            }
        }

        // return an immutable list
        int len = result.size();
        if(len>0)
            if (len ==1)
                return List.of(result.get(0));
            else
                return List.of(result.get(0), result.get(1));

        // no intersections
        return null;
    }
}

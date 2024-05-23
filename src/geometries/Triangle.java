package geometries;
import primitives.*;

import java.util.List;

/**
 * Class of Triangle
 */
public class Triangle extends Polygon {
    public Triangle(Point p1,Point p2,Point p3){
        super(p1,p2,p3);
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

}

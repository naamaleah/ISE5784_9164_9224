package geometries;
import primitives.*;

import java.util.List;

/**
 * Class Triangle is the  class representing a triangle of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
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

package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Polygon;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Testing Polygons
 * @author Dan
 */
public class PolygonTests {
   /**
    * Delta value for accuracy when comparing the numbers of type 'double' in
    * assertEquals
    */


    final double DELTA = 0.000001;
    final Point p001=new Point(0, 0, 1);
   final Point p100=new Point(1, 0, 0);

   /** Test method for {@link Polygon#Polygon(Point...)}. */
   @Test
   public void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(p001, p100,new Point(0, 1, 0),new Point(-1, 1, 1)),
                                  "Failed constructing a correct polygon");

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(p001, new Point(0, 1, 0),
                           new Point(1, 0, 0), new Point(-1, 1, 1)), //
                           "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(p001,p100, new Point(0, 1, 0),new Point(0, 2, 2)), //
                           "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(p001,p100, new Point(0, 1, 0),new Point(0.5, 0.25, 0.5)), //
                           "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(p001,p100, new Point(0, 1, 0),new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertix on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(p001,p100, new Point(0, 1, 0), new Point(0, 0, 1)),
                   "Constructed a polygon with vertice on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(p001,p100, new Point(0, 1, 0),new Point(0, 1, 0)),
                   "Constructed a polygon with vertice on a side");

   }

   /** Test method for {@link Polygon#getNormal(Point)}. */
   @Test
   public void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         { p001,p100, new Point(0, 1, 0), new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                      "Polygon's normal is not orthogonal to one of the edges");
   }
   /**
    * Test method for {@link Polygon#findIntersections(Ray)}.
    */
   @Test
   void testFindIntersections() {
      Polygon polygon = new Polygon(    p100,
              new Point(0, 1, 0),
              new Point(-2, 0, 0),
              new Point(0,-1,0)
      );
      List<Point> result;
      // ============ Equivalence Partitions Tests ==============
      //TC01: Ray intersects the polygon
      result = polygon.findIntersections(new Ray(new Point(-0.5, -0.5, -1), new Vector(0.5, 0.5, 3)));

      assertEquals(1, result.size(), "Wrong number of points");
      assertEquals(new Point(-0.33d, -0.33d, 0d), result.getFirst(), "Ray doesn't intersect the polygon");

      //TC02:Ray outside against vertex
      assertNull(polygon.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 0, 4))), "Ray isn't outside against vertex");

      //TC03: Ray outside against edge
      assertNull(polygon.findIntersections(new Ray(new Point(-1, -1, 0), new Vector(0, 0, 3))), "Ray isn't outside against edge");


      // ============ Boundary Values Tests =============
      //TC1: Ray On edge
      result = polygon.findIntersections(new Ray(new Point(-2, 0, 3), new Vector(1.03d, 0.51d, -3)));
      assertEquals(1, result.size(), "Wrong number of points");
      assertEquals(new Point(-0.97d, 0.51d, 0d), result.getFirst(), "Ray  isn't on edge of the polygon");

      ///TC2: Ray in vertex
      assertNull(polygon.findIntersections(new Ray(new Point(0, 1, 0), new Vector(-2d, -1d, 3))),  "Ray  isn't on vertex of the polygon");

      //TC3: Ray On edge's continuation
      assertNull(polygon.findIntersections(new Ray(new Point(-1, 2, 0), new Vector(-1d, -2d, 3))), "Ray  isn't On edge's continuation");


   }
}

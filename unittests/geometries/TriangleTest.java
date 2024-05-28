package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    /** Test method for {@link Triangle#Triangle(Point,Point,Point)}. */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave
        assertDoesNotThrow(() -> new Triangle(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0)),
                "Failed constructing a correct Triangle");


        // =============== Boundary Values Tests ==================


        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Triangle(new Point(0, 0, 1), new Point(0, 1, 0),
                        new Point(0, 0, 1)),
                "Constructed a Triangle with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Triangle(new Point(0, 0, 1),
                        new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a Triangle with vertice on a side");
         }

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: .There is a simple single test here
        Triangle triangle = new Triangle(new Point(0, 0, 1),
                new Point(1, 0, 0), new Point(0, 1, 0));

        double sqrt3 = Math.sqrt(1d / 3);

        assertEquals(new Vector(sqrt3, sqrt3, sqrt3),
                triangle.getNormal(new Point(0, 0, 1)),
                "Bad normal to plan");
    }

    @Test
    void testFindIntersections() {

        // all tests assume a point on the plane in which the triangle is on and check
        // if the function identifies whether the point is inside the triangle or not
        Triangle t = new Triangle(new Point(0,0,1), new Point(0,1,0), new Point(1,0,0));
        final Point p05051 = new Point(0.5, 0.5, 1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the triangle
        List<Point> result = t.findIntersections(new Ray(p05051, new Vector(-0.5, -1, -1)));
        assertEquals(1,
                result.size(),
                "ERROR: findIntersections() did not return the right number of points");
        assertEquals(List.of(new Point(0.3, 0.1, 0.6)), result, "Incorrect intersection points");

        // TC02: Ray outside against edge
        assertNull(t.findIntersections(new Ray(p05051, new Vector(-2, -0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // TC03: Ray outside against vertex
        assertNull(t.findIntersections(new Ray(p05051, new Vector(1, -0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // =============== Boundary Values Tests ==================
        // TC04: Ray on edge
        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, -0.1, -0.4))),
                "ERROR: findIntersections() did not return null");

        // TC05: Ray on vertex
        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, 0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // TC06: Ray on edge's continuation
        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, -1, 0.5))),
                "ERROR: findIntersections() did not return null");
    }
}
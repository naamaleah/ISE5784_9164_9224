package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for geometries.Plane class
 */
class PlaneTest {

    /** Test method for {@link Plane#Plane(Point,Point,Point)}. */
    @Test
    public void testConstructor(){
        // ============ Equivalence Partitions Tests ==============
       // TC01: Correct concave
        assertDoesNotThrow(() -> new Plane(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0)),
                "Failed constructing a correct Plane");
        // =============== Boundary Values Tests ==================

        // TC02: Points on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1),
                 new Point(0, 0, 2), new Point(1, 3, 8),
                  new Point(-1, 1, 1)), //
                "Constructed a Plane with aligned points");

        // TC02: first two points similar
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1),
                 new Point(0, 0, 2),
                 new Point(1, 3, 8), new Point(-1, 1, 1)), //
                "Constructed a Plane with aligned points");

    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: .
        Plane pl = new Plane(new Point(0, 0, 1),
         new Point(1, 0, 0),
         new Point(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3),
         pl.getNormal(new Point(0, 0, 1)),
         "getNormal(),Bad normal to plan");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Plane plane1 = new Plane(new Point(0,0,0), new Vector(0, 0, 1));
        Plane plane2 = new Plane(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));
        final var exp = List.of(new Point(0,1,0));
        final Vector vn1n1n1=new Vector(-1,-1,-1);
        final Vector vn050203=new Vector(-0.5,0.2,0.3);

        // ============ Equivalence Partitions Tests ==============

        // **** Group: Ray's line not orthogonal or parallel to the plane
        // TC01: Ray start after and does not crosse the plane
        assertNull(plane1.findIntersections(new Ray(new Point(0,0,2), new Vector(0,1,1))),
                "Ray's line after plane");
        // TC02: Ray starts before and crosses the plane (1 point)
        final var result1 = plane1.findIntersections(new Ray(new Point(0,0,-1), new Vector(0,1,1)))
                .stream().toList();

        assertEquals(1, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // **** Group: the ray is parallel to the plane
        // TC03: Ray is parallel to the plane and included in the plane
        assertNull(plane2.findIntersections(new Ray(new Point(0.5, 0.25, 0.25), vn050203)),
                "ERROR: findIntersections() did not return null when the ray is included in the plane");

        // TC04: ray is parallel to the plane and not included in the plane
        assertNull(plane2.findIntersections(new Ray(new Point(0.6, 0.25, 0.25), vn050203)),
                "ERROR: findIntersections() did not return null when the ray is parallel " +
                        "to the plane and not included in the plane");

        // **** Group: the ray is orthogonal to the plane
        // TC05: Ray is orthogonal to the plane and begins before the plane
        final var result2 = plane2.findIntersections(new Ray(new Point(0.6, 0.25, 0.25), vn1n1n1));
        assertEquals(1, result2.size(),
                "ERROR: findIntersections() returned incorrect number of points" +
                        " when the ray is orthogonal to the plane and begins before the plane");

        // TC06: Ray is orthogonal to the plane and begins in the plane
        assertNull(plane2.findIntersections(new Ray(new Point(0.5, 0.25, 0.25), vn1n1n1)),
                "ERROR: findIntersections() did not return null when the ray is orthogonal to the plane and " +
                        "begins in the plane");

        // TC07: Ray is orthogonal to the plane and begins after the plane
        assertNull(plane2.findIntersections(new Ray(new Point(0.4, 0.25, 0.25), vn1n1n1)),
                "ERROR: findIntersections() did not return null when the ray is orthogonal " +
                        "to the plane and begins after the plane");

        // **** Group: the ray is neither orthogonal nor parallel to the plane
        // TC08: Ray begins in the plane
        assertNull(plane2.findIntersections(new Ray(new Point(0.5, 0.25, 0.25), new Vector(-3, 5, 2))),
                "ERROR: findIntersections() did not return null when the ray begins in the plane");

        // Ray is neither orthogonal nor parallel to the plane and begins in the same
        // point which appears as reference point in the plane
        assertNull(plane2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(-3, 5, 2))),
                "ERROR: findIntersections() did not return null when the ray begins " +
                        "in the same point which appears as reference point in the plane");
    }
}
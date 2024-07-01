package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 */
class SphereTest {

    private final Point p001 = new Point(0, 0, 1);
    private final Vector v001 = new Vector(0, 0, 1);
    private final Point p100 = new Point(1, 0, 0);

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Sphere sph = new Sphere(p001, 1.0);
        assertEquals(v001,
                sph.getNormal(new Point(0, 0, 2)),
                "Bad normal to sphere");
    }


    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);
        final Vector v100 = new Vector(1, 0, 0);
        final Point p110 = new Point(1, 1, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(p01)))
                .toList();
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        var result = sphere.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 2, 0)))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(p01))).toList();
        assertEquals(1, result.size(), "There should be one intersection");
        assertEquals(List.of(p110), result, "Incorrect intersection point");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(3, 0.5, 0), v100)),
                "There shouldn't be any intersections");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        result = sphere.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-1, 1, 0)));
        assertEquals(1, result.size(), "There should be one intersection");
        assertEquals(List.of(p110), result, "Incorrect intersection point");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 0, 1), new Vector(1, 0, 2))),
                "There shouldn't be any intersections");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(2, 0, 0);
        result = sphere.findIntersections(new Ray(p01, v100))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(p01))).toList();
        assertEquals(2, result.size(), "There should be two intersections");
        assertEquals(List.of(p1, p2), result, "Incorrect intersection points");

        // TC14: Ray starts at sphere and goes inside (1 point)
        result = sphere.findIntersections(new Ray(new Point(1, -1, 0), new Vector(0, 1, 0)));
        assertEquals(1, result.size(), "There should be one intersection");
        assertEquals(List.of(p110), result, "Incorrect intersection point");

        // TC15: Ray starts inside (1 point)
        p1 = new Point(2, 0, 0);
        result = sphere.findIntersections(new Ray(new Point(0.5, 0, 0), v100));
        assertEquals(1, result.size(), "There should be one intersection");
        assertEquals(List.of(p1), result, "Incorrect intersection point");

        // TC16: Ray starts at the center (1 point)
        result = sphere.findIntersections(new Ray(p100, new Vector(0, 1, 0)));
        assertEquals(1, result.size(), "There should be one intersection");
        assertEquals(List.of(p110), result, "Incorrect intersection point");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 0, 1), v001)),
                "There shouldn't be any intersections");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(3, 0, 0), v100)),
                "There shouldn't be any intersections");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, 1, 0), v100)),
                "There shouldn't be any intersections");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(p110, v100)),
                "There shouldn't be any intersections");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2, 1, 0), v100)),
                "There shouldn't be any intersections");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line

        assertNull(sphere.findIntersections(new Ray(new Point(1, 2, 0), v100)),
                "There shouldn't be any intersections");
    }

    /**
     * Test method for {@link geometries.Plane#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsHelperTest1() {

        //region *** test including consideration that points are closer to ray origin than maxDistance parameter
        // ray and sphere intersect twice at (0,0,3) and (0,6,3)
        Sphere sphere = new Sphere(new Point(0, 3, 3), 3);
        Ray ray = new Ray(new Point(0, -4, 3), new Vector(0, 1, 0));
        Intersectable.GeoPoint gp1 = new Intersectable.GeoPoint(sphere, new Point(0, 0, 3));
        Intersectable.GeoPoint gp2 = new Intersectable.GeoPoint(sphere, new Point(0, 6, 3));

        // TC01 -  max distance is smaller than distance to both intersection points - no intersections
        assertNull(sphere.findGeoIntersectionsHelper(ray, 2), "points are further than maxDistance");

        //TC02 -  max distance is smaller than distance to second intersection point - one intersection point
        List<Intersectable.GeoPoint> res = sphere.findGeoIntersectionsHelper(ray, 5);
        assertEquals(List.of(gp1), res, "one point only is in boundary");

        //TC03 -  distance to both points is smaller than maxDistance - two intersection points
        List<Intersectable.GeoPoint> res2 = sphere.findGeoIntersectionsHelper(ray, 12);
        assertEquals(List.of(gp1, gp2), res2, "one point only is in boundary");

    }
}
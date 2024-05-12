package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class
 */
class PointTest {

    private final double DELTA = 0.000001;
    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

        // Subtract points
        Point  p1         = new Point(1, 2, 3);
        Point  p2         = new Point(2, 4, 6);
        Vector v1         = new Vector(1, 2, 3);
        if (!p2.subtract(p1).equals(v1))
            out.println("ERROR: (point2 - point1) does not work correctly");
        try {
            p1.subtract(p1);
            out.println("ERROR: (point - itself) does not throw an exception");
        } catch (IllegalArgumentException ignore) {} catch (Exception ignore) {
            out.println("ERROR: (point - itself) throws wrong exception");
        }
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Point  p1         = new Point(1, 2, 3);
        Vector v1         = new Vector(1, 2, 3);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(
                new Point(2, 4, 6),
                p1.add(v1),
                "ERROR: (point + vector) = other point does not work correctly");
        // =============== Boundary Values Tests ==================
        assertEquals(
                Point.ZERO,
                p1.add(new Vector(-1, -2, -3)),
                "ERROR: (point + vector) = center of coordinates does not work correctly");
        assertThrows(IllegalArgumentException.class, () -> function call, "failure text");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

        // distances
        Point  p1         = new Point(1, 2, 3);
        Point  p3         = new Point(2, 4, 5);
        assertEquals(0,p1.distanceSquared(p1),DELTA, "ERROR: point squared distance to itself is not zero");
        assertEquals(0, p1.distance(p1),DELTA, "ERROR: point distance to itself is not zero");
        assertEquals(0,p1.distanceSquared(p3) - 9,DELTA, "ERROR: squared distance between points is wrong");
        assertEquals(0,p3.distanceSquared(p1) - 9,DELTA, "ERROR: squared distance between points is wrong");
        assertEquals(0, p1.distance(p3) - 3,DELTA, "ERROR: distance between points to itself is wrong");
        assertEquals(0,p3.distance(p1) - 3,DELTA, "ERROR: distance between points to itself is wrong");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        Point  p1         = new Point(1, 2, 3);
        Point  p3         = new Point(2, 4, 5);        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        // distances
        Point  p1         = new Point(1, 2, 3);
        Point  p3         = new Point(2, 4, 5);
        assertEquals(0,p1.distanceSquared(p1),DELTA, "ERROR: point squared distance to itself is not zero");
        assertEquals(0, p1.distance(p1),DELTA, "ERROR: point distance to itself is not zero");
        assertEquals(0,p1.distanceSquared(p3) - 9,DELTA, "ERROR: squared distance between points is wrong");
        assertEquals(0,p3.distanceSquared(p1) - 9,DELTA, "ERROR: squared distance between points is wrong");
        assertEquals(0, p1.distance(p3) - 3,DELTA, "ERROR: distance between points to itself is wrong");
        assertEquals(0,p3.distance(p1) - 3,DELTA, "ERROR: distance between points to itself is wrong");

    }
}
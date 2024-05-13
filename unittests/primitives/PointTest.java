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
        // Equivalence Partitions tests ======================================================================
        // TC01 subtract two different points
        Point p1 = new Point(1, 2, 3);
        assertEquals( new Vector(-1, -1, -1), p1.subtract(new Point(2, 3, 4)), "subtract(), wrong subtract for different points");


        // Boundary Value Analysis tests ======================================================================

        // TC02 subtracting point from itself
        assertThrows(IllegalArgumentException.class,
                ()->new Point(1,2,3).subtract(new Point(1,2,3)),
                "subtract(), Point- same Point must throw exception" );

        //TC03 subtracting point from opposite
        assertEquals(new Vector(2,4,6), p1.subtract(new Point(-1,-2,-3)), "subtract(), wrong subtract for opposite points");

    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {

        // ============ Equivalence Partitions Tests ==============
        //TC01 adding point and vector
        Point  p1         = new Point(1, 2, 3);
        assertEquals(
                new Point(2, 4, 6),
                p1.add(new Vector(1, 2, 3)),
                "add(), (point + vector) = other point does not work correctly");

    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // Equivalence Partitions tests ======================================================================
        // TC01 distance squared between two different points
        Point p0 = new Point(0, 0, 1);
        assertEquals(4, p0.distanceSquared(new Point(0, 0, 3)),DELTA, "distanceSquared() of different points is incorrect");

        // Boundary Value Analysis tests ======================================================================
        // TC02 distance squared from point to itself
        assertEquals(p0.distanceSquared(p0), 0, "distanceSquared() , of point to itself is not 0");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
      // ============ Equivalence Partitions Tests ==============
        Point  p1         = new Point(1, 2, 3);
        //TC01:Test that the distance between two different points it proper
        assertEquals(3,p1.distance(new Point(2, 4, 5)) ,DELTA, "distance() between two different points is  wrong result");

        // =============== Boundary Values Tests ==================

        //TC02:Test that the distance between the same point it proper
        assertEquals(0,p1.distanceSquared(p1),DELTA, "distance() to itself is not zero");

    }
}
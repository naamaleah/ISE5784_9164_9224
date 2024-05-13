package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

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
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 0, 2), new Point(1, 3, 8), new Point(-1, 1, 1)), //
                "Constructed a Plane with aligned points");

        // TC02: first two points similar
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 0, 2), new Point(1, 3, 8), new Point(-1, 1, 1)), //
                "Constructed a Plane with aligned points");

    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: .
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point(0, 0, 1)), "getNormal(),Bad normal to plan");
    }
}
package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 */

class VectorTest {

    private final double DELTA = 0.000001;
    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */

    @Test
    void testAdd() {
        Vector v1         = new Vector(1, 2, 3);
        Vector v2         = new Vector(4, 1, 2);
        Vector v1Opposite = new Vector(-1, -2, -3);
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================
        assertThrows(
                IllegalArgumentException.class,
                () -> v1.add(v1Opposite),
                "ERROR: Vector + -itself does not throw an exception");





        Vector v2         = new Vector(1, 0, 0);
      //  assertEquals(new Vector(2,2,3),v1.add(v2),);
    }

    /**
     * Test method for {@link primitives.Vector#scale(primitives.Vector)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector vr = v123.crossProduct(v03M2);
        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals(v123.length() * v03M2.length(), vr.length(), DELTA, "crossProduct() wrong result length");
        // TC02: Test cross-product result orthogonality to its operands
        assertEquals(0, vr.dotProduct(v123), "crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(v03M2), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-product of parallel vectors
        assertThrows(IllegalArgumentException.class, () -> v123.crossProduct(vM2M4M6), //
                "crossProduct() for parallel vectors does not throw an exception");

    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        Vector v4= new Vector(1, 2, 2);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(9,v1.lengthSquared(),0.00001,"ERROR: lengthSquared() wrong value");
        // =============== Boundary Values Tests ==================



    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        Vector v4= new Vector(1, 2, 2);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(3,v1.length(),0.00001,"ERROR: length() wrong value");
        // =============== Boundary Values Tests ==================



    }

    /**
     * Test method for {@link primitives.Vector#normalize(primitives.Vector)}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

    }
}
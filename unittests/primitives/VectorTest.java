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

        //checking if adding two vectors returns the expected value
        assertEquals(new Vector(5,3,5), v1.add(v2), "ERROR: add() wrong result");

        // =============== Boundary Values Tests ==================

        //checking if adding opposite but equal vectors returns 0 and therefore throw an exception
        assertThrows(
                IllegalArgumentException.class,
                () -> v1.add(v1Opposite),
                "ERROR: Vector + -itself does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(primitives.Vector)}.
     */
    @Test
    void testScale() {
        Vector v1         = new Vector(1, 2, 3);
        Vector v1Opposite = new Vector(-1, -2, -3);
        Vector v2         = new Vector(-2, -4, -6);
        Vector v3         = new Vector(0, 3, -2);
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector v1         = new Vector(1, 2, 3);
        Vector v1Opposite = new Vector(-1, -2, -3);
        Vector v2         = new Vector(-2, -4, -6);
        Vector v3         = new Vector(0, 3, -2);
        Vector v4         = new Vector(0, 0, 1);
        // ============ Equivalence Partitions Tests ==============

        //checking if DotProduct for an angle smaller than 90 returns the correct value
        assertEquals(-28, v1.dotProduct(v2), "ERROR: dotProduct() wrong value");
        //checking if DotProduct for an angle bigger than 90 returns the correct value
        assertEquals(3, v1.dotProduct(v4), "ERROR: dotProduct() wrong value");

        // =============== Boundary Values Tests ==================
        //checking if DotProduct for orthogonal vectors is zero
        assertEquals(0, v1.dotProduct(v3), "ERROR: dotProduct() for orthogonal vectors is not zero");

        //checking if DotProduct with a vector whose length is 1 returns the expected value
        assertEquals(3, v1.dotProduct(v4), "ERROR: dotProduct() for vector whose length is 1 is not right");

        if (!isZero(v1.dotProduct(v2) + 28))
            out.println("ERROR: dotProduct() wrong value");

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
        //no need for a boundary Values Tests for that
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
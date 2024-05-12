package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class
 */

class VectorTest {

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */

    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================


        Vector v1         = new Vector(1, 2, 3);
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

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

        Vector v1         = new Vector(1, 2, 3);
        assertEquals(14,v1.lengthSquared(),0.00001,"ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============

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
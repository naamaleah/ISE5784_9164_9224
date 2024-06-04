package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 */

class VectorTest {

    private final double DELTA = 0.000001;

    /** Test method for {@link Vector#Vector(Double3)}. */
    @Test
    public void testConstructor(){
        // ============ Equivalence Partitions Tests ==============
        //constructing a correct vector
        assertDoesNotThrow(() -> new Vector(0, 0, 1),
                "Failed constructing a correct vector");
        // =============== Boundary Values Tests ==================
        //Try constructing vector zero
        assertThrows(IllegalArgumentException.class, //
                () -> new Vector(0, 0, 0), //
                "Constructed a vector zero");

    }

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
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        Vector v0=new Vector(1,-3,-2);
        Vector v1=new Vector(-1,3,2);
        Vector v2=new Vector(0.5,-1.5,-1);
        Vector v3=new Vector(5,-15,-10);

        // ============ Equivalence Partitions Tests ==============

        //multiply in number<0
        Vector v=v0.scale(-1);
        assertEquals(v,v1,"scale() wrong result");

        //multiply in 0<number<1
        v=v0.scale(0.5);
        assertEquals(v,v2,"scale() wrong result");

        //multiply in 1<number
        v=v0.scale(5);
        assertEquals(v,v3, "scale() wrong result");


        //=============== Boundary Values Tests ==================
        //multiply in zero
        assertThrows(IllegalArgumentException.class,
                ()->new Vector(1,2,3).scale(0),
                "scale in zero dont throw exception!");
        //multiply in 1
        assertEquals(v0,v0.scale(1), "scale() wrong result");

    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector v1         = new Vector(1, 2, 3);
        Vector v2         = new Vector(-2, -4, -6);
        Vector v3         = new Vector(0, 3, -2);
        Vector v4         = new Vector(0, 0, 1);
        Vector v5         = new Vector(1, 1, 1);
        Vector v6         = new Vector(1, -1, 1);
        // ============ Equivalence Partitions Tests ==============

        //checking if DotProduct for an angle smaller than 90 returns the correct value
        assertEquals(1, v5.dotProduct(v6), "ERROR: dotProduct() wrong value");
        //checking if DotProduct for an angle bigger than 90 returns the correct value
        assertEquals(-28, v1.dotProduct(v2), "ERROR: dotProduct() wrong value");

        // =============== Boundary Values Tests ==================
        //checking if DotProduct for orthogonal vectors is zero
        assertEquals(0,
                v1.dotProduct(v3),
                "ERROR: dotProduct() for orthogonal vectors is not zero");

        //checking if DotProduct with a vector whose length is 1 returns the expected value
        assertEquals(3,
                v1.dotProduct(v4),
                "ERROR: dotProduct() for vector whose length is 1 is not right");

    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(0, 3, -2);
        Vector v3 = new Vector(-2, -4, -6);
        Vector v5 = new Vector(-5, -1, 0);
        Vector v6 = new Vector(0, 0, 4);
        // ============ Equivalence Partitions Tests ==============

        Vector vr = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals( vr.length(),
                v1.length() * v2.length(),
                DELTA,
                "crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(isZero(vr.dotProduct(v2)), "crossProduct() result is not orthogonal to 2nd operand");

        assertEquals(new Vector(-13,2,3),
                v1.crossProduct(v2),
                "ERROR: dotProduct() wrong value in opposite direction");//opposite direction
        assertEquals(new Vector(8,-4,0),
                v1.crossProduct(v6),
                "ERROR: dotProduct() wrong value in acute angle");//acute angle
        assertEquals(new Vector(3,-15,9),
                v1.crossProduct(v5),
                "ERROR: dotProduct() wrong value in obtuse angle");//obtuse angle

        // =============== Boundary Values Tests ==================
        //check if the vectors is parallel
        assertThrows(IllegalArgumentException.class,
                ()->v1.crossProduct(v3),
                "crossProduct() for parallel vectors does not throw an exception");
        //same vector
        assertThrows(IllegalArgumentException.class,
                ()->v1.crossProduct(v1),
                "Didn't throw parallel exception!");

    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        Vector v1= new Vector(1, 2, 2);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(9,
                v1.lengthSquared(),
                DELTA,
                "ERROR: lengthSquared() wrong value");
        // =============== Boundary Values Tests ==================
        //no need for a boundary Values Tests for length squared


    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        Vector v1= new Vector(1, 2, 2);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(3,v1.length(),0.00001,"ERROR: length() wrong value");
        // =============== Boundary Values Tests ==================
        //no need for a boundary Values Tests for length
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalize();
        // ============ Equivalence Partitions Tests ==============
        //Testing normalized vectors characteristics
        assertEquals(1,u.length(),
                0.00001,
                "ERROR: the normalized vector is not a unit vector");
        assertThrows(IllegalArgumentException.class,
                ()->v.crossProduct(u),
                "ERROR: the normalized vector is not parallel to the original one");
        // =============== Boundary Values Tests ==================
        //no need for a boundary Values Tests for Normalization
    }
}
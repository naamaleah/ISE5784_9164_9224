package geometries;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for geometries.Sphere class
 */
class SphereTest {

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Sphere sph = new Sphere(1.0, new Point(0, 0, 1));
        assertEquals(new Vector(0, 0, 1),
                sph.getNormal(new Point(0, 0, 2)),
                "Bad normal to sphere");
    }
}
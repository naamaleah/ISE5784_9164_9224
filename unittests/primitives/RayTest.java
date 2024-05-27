package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void testGetPoint() {
        Ray ray=new Ray(new Point(1,1,1),new Vector(1,0,0));

        // ============ Equivalence Partitions Tests ==============
        //TC1:Negative distance
        assertEquals(new Point(0,1,1), ray.getPoint(-1),
                "getPoint() of negative is incorrect");
        //TC2:Positive distance
        assertEquals(new Point(2,1,1), ray.getPoint(1),
                "getPoint() of positive is incorrect");

        //============== Boundary Value Analysis tests ==============
        //TC2:Zero distance
        assertEquals(new Point(1,1,1), ray.getPoint(0),
                "getPoint() of positive is incorrect");
    }
}
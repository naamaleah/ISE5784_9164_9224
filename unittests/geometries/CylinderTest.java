package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTest {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Ray ray = new Ray(new Point(0,1,0), new Vector(0,1,0));
        Cylinder  cylinder = new Cylinder(2, ray ,4);
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        assertEquals(cylinder.getNormal(new Point(2,2,0)) ,new Vector(1,0,0),
                "Normal abnormality");
        // TC02: Normal test on base
        assertEquals(cylinder.getNormal(new Point(1,1,0)) ,
                new Vector(0,-1,0),
                "Normal abnormality");
        // TC03: Normal test on top base
        assertEquals(cylinder.getNormal(new Point(1,5,0)) ,new Vector(0,1,0),
                "Normal abnormality");
        // =============== Boundary Values Tests ==================
        // TC01: A dot on the center of the lower base
        assertEquals(cylinder.getNormal(new Point(0,1,0)),new Vector(0,-1,0),
                "Normal abnormality");
        // TC02: A dot at the center of the upper base
        assertEquals(cylinder.getNormal(new Point(0,5,0)),new Vector(0,1,0),
                "Normal abnormality");    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTests() {

        Cylinder cylinder = new Cylinder( 1,new Ray(new Point(2,0,0), new Vector(0,0,1)),2);

        final Vector v100=new Vector(1,0,0);
        final Vector v001=new Vector(0,0,1);
        final Point p300=new Point(3,0,0);

        //TC03 ray starts outside and parallel to the cylinder's ray and crosses the cylinder

        var result = cylinder.findIntersections(new Ray(new Point(2.5, 0, -1), v001));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2.5, 0, 0),
                new Point(2.5, 0, 2)),
                result,
                "Bad intersection point");

        //TC04 ray starts from outside and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point(-2, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 0.5),
                new Point(3, 0, 0.5)),
                result,
                "Bad intersection points");

        //TC05 ray starts from inside and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point(1.5, 0, 0.5), v100));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");

        //TC06 ray starts from outside the cylinder and doesn't cross the cylinder

        result = cylinder.findIntersections(new Ray(new Point(5, 0, 0), v100));
        assertNull(result, "Wrong number of points");

        //TC07 ray starts from outside and crosses base and surface

        result = cylinder.findIntersections(new Ray(new Point(1, 0, -1), new Vector(1, 0, 1)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 0),
                new Point(3, 0, 1)),
                result,
                "Bad intersection points");

        //TC08 ray starts from outside and crosses surface and base

        result = cylinder.findIntersections(new Ray(new Point(4, 0, 2), new Vector(-1, 0, -1)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 0),
                new Point(3, 0, 1)),
                result,
                "Bad intersection points");


        // =============== Boundary Values Tests ==================


        //TC09 ray is on the surface of the cylinder (not bases)

        result = cylinder.findIntersections(new Ray(p300, v001));
        assertNull(result, "Wrong number of points");

        //TC10 ray is on the base of the cylinder and crosses 2 times

        result = cylinder.findIntersections(new Ray(new Point(-1, 0, 0), v100));
        assertNull(result, "Wrong number of points");

        //TC11 ray is in center of the cylinder

        result = cylinder.findIntersections(new Ray(new Point(2, 0, 0), v001));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 2)), result, "Bad intersection points");

        //TC12 ray is perpendicular to cylinder's ray and starts from outside the tube

        result = cylinder.findIntersections(new Ray(new Point(-2, 0, 0.5), v100));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 0.5),
                new Point(3, 0, 0.5)),
                result,
                "Bad intersection points");

        //TC13 ray is perpendicular to cylinder's ray and starts from inside cylinder (not center)

        result = cylinder.findIntersections(new Ray(new Point(1.5, 0, 0.5), v100));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");

        //TC14 ray is perpendicular to cylinder's ray and starts from the center of cylinder

        result = cylinder.findIntersections(new Ray(new Point(2, 0, 0.5), v100));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");

        //TC15 ray is perpendicular to cylinder's ray and starts from the surface of cylinder to inside

        result = cylinder.findIntersections(new Ray(new Point(1, 0, 0.5), v100));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");

        //TC16 ray is perpendicular to cylinder's ray and starts from the surface of cylinder to outside

        result = cylinder.findIntersections(new Ray(p300, v100));
        assertNull(result, "Wrong number of points");


        //TC17 ray starts from the surface to outside

        result = cylinder.findIntersections(new Ray(p300, new Vector(1,1,1)));
        assertNull(result, "Wrong number of points");


        //TC18 ray starts from the surface to inside

        result = cylinder.findIntersections(new Ray(new Point(3,0,0.5), new Vector(-1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1,0,0.5)), result, "Bad intersection point");


        //TC19 ray starts from the center

        result = cylinder.findIntersections(new Ray(new Point(2,0,0), new Vector(1,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3,0,1)), result, "Bad intersection point");


        //TC20 prolongation of ray crosses cylinder

        result = cylinder.findIntersections(new Ray(p300, v100));
        assertNull(result, "Wrong number of points");


        //TC21 ray is on the surface starts before cylinder

        result = cylinder.findIntersections(new Ray(new Point(3,0,-1), v001));
        assertNull(result, "Wrong number of points");



        //TC22 ray is on the surface starts at bottom's base

        result = cylinder.findIntersections(new Ray(p300, v001));
        assertNull(result, "Wrong number of points");


        //TC23 ray is on the surface starts on the surface

        result = cylinder.findIntersections(new Ray(new Point(3,0,1), v001));
        assertNull(result, "Wrong number of points");

        //TC24 ray is on the surface starts at top's base

        result = cylinder.findIntersections(new Ray(new Point(3,0,2), v001));
        assertNull(result, "Wrong number of points");
    }


}
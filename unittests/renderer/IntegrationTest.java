package renderer;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {


    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//      .setRayTracer(new SimpleRayTracer(new Scene("Test")))
//      .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVpDistance(1).setVpSize(3,3);
    private final Camera camera000=cameraBuilder.build();
    private final Camera camera0005=cameraBuilder.setLocation(new Point(0,0,0.5)).build();

    String errorMessage  = " incorrect number of intersections";

    private int generateRays(Geometry g,Camera camera)
    {
        int Nx=3;
        int Ny=3;
        int count=0;

        for (int i = 0; i < Nx ; i++)
            for (int j = 0; j < Ny; j++){
                var intersections = g.findIntersections(camera.constructRay(Nx, Ny, i, j));
                count += (intersections != null ? intersections.size() : 0);
            }
        return count;
    }

    @Test
    void sphereTest() {

        // TC01: 2 intersection points
        assertEquals(2,
                generateRays(new Sphere(new Point(0,0,-3),1),camera000), errorMessage);
        // TC02: 18 intersection points
        assertEquals(18,
                generateRays(new Sphere(new Point(0,0,-2.5),2.5),camera0005), errorMessage);
        // TC03: 10 intersection points
        assertEquals(10,
                generateRays(new Sphere(new Point(0,0,-2),2),camera0005), errorMessage);
        // TC04: 9 intersection points
        assertEquals(9,
                generateRays(new Sphere(new Point(0,0,1.5),6),camera0005), errorMessage);
        // TC05: 0 intersection points
        assertEquals(0,
                generateRays(new Sphere(new Point(0,0,1),0.5),camera000), errorMessage);

    }

    @Test
    void triangleTest() {
        // TC01: 1 intersection point
        assertEquals(1,
                generateRays(new Triangle(new Point(0,1,-2),new Point(1,-1,-2),
                        new Point(-1,-1,-2)),camera000), errorMessage);
        // TC02: 2 intersection points
        assertEquals(2,
                generateRays(new Triangle(new Point(0,20,-2),new Point(1,-1,-2),
                        new Point(-1,-1,-2)),camera000), errorMessage);
    }

    @Test
    void planeTest() {
        // TC01: 9 intersection points, parallel plane
        assertEquals(9,
                generateRays(new Plane(new Point(0,0,-3),new Vector(0,0,1)),camera000), errorMessage);
        // TC02: 9 intersection points, not parallel plane
        assertEquals(9,
                generateRays(new Plane(new Point(0,0,-1),new Vector(1.5,3,5)),
                        camera000), errorMessage);
        // TC03: 6 intersection points, plane crossing the view plane covering two out of three rows
        assertEquals(6,
                generateRays(new Plane(new Point(0,2,-3),new Vector(0,-1,-1)),
                        camera000), errorMessage);
    }
}

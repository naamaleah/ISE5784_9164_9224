package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/** Tests for landscape with house, mountains, balloons, and office buildings */
public class LandscapeWithHouseUpInspirationTest {
    /** Scene for the tests */
    private final Scene scene = new Scene("Landscape with House, Balloons, and Office Buildings");
    /** Camera builder for the tests */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setRayTracer(new SimpleRayTracer(scene));
    // Office Buildings with Reflective Glass
    Material reflectiveGlass = new Material().setkD(0.4).setkS(0.4).setkR(0.2).setnShininess(100);
    Color grayBlue = new Color(47, 79, 79); // More gray-blue color

    @Test
    public void landscapeWithHouseBalloonsAndOfficeBuildings() {
        scene.geometries.add(
                // Mountains
                new Sphere(new Point(-200, -50, -400), 150d).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(100, -50, -400), 200d).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Plane(new Point(0, -50, -300), new Vector(0, 1, 0)).setEmission(new Color(107, 142, 35))
                        .setMaterial(new Material().setkD(0.5).setkS(0.2).setnShininess(30)),
                // Clouds
                new Sphere(new Point(-100, 100, -150), 30d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                new Sphere(new Point(-70, 110, -160), 25d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                new Sphere(new Point(-130, 120, -140), 20d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                // House
                // Front wall
                new Polygon(new Point(-50, -30, -150), new Point(50, -30, -150), new Point(50, 30, -150), new Point(-50, 30, -150))
                        .setEmission(new Color(139, 69, 19)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Back wall
                new Polygon(new Point(-50, -30, -250), new Point(50, -30, -250), new Point(50, 30, -250), new Point(-50, 30, -250))
                        .setEmission(new Color(139, 69, 19)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Left wall
                new Polygon(new Point(-50, -30, -150), new Point(-50, -30, -250), new Point(-50, 30, -250), new Point(-50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // Lighter brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Right wall
                new Polygon(new Point(50, -30, -150), new Point(50, -30, -250), new Point(50, 30, -250), new Point(50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // Lighter brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof front
                new Polygon(new Point(-50, 30, -150), new Point(50, 30, -150), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof back
                new Polygon(new Point(-50, 30, -250), new Point(50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Chimney
                new Polygon(new Point(-30, 30, -180), new Point(-20, 30, -180), new Point(-20, 110, -180), new Point(-30, 110, -180)) // Front face
                        .setEmission(new Color(101, 67, 33)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-30, 30, -190), new Point(-20, 30, -190), new Point(-20, 110, -190), new Point(-30, 110, -190)) // Back face
                        .setEmission(new Color(101, 67, 33)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-30, 30, -180), new Point(-30, 30, -190), new Point(-30, 110, -190), new Point(-30, 110, -180)) // Left face
                        .setEmission(new Color(101, 67, 33)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-20, 30, -180), new Point(-20, 30, -190), new Point(-20, 110, -190), new Point(-20, 110, -180)) // Right face
                        .setEmission(new Color(101, 67, 33)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Balloons
                // Balloon 1
                new Sphere(new Point(-30, 200, -180), 20d).setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-30, 200, -180), new Point(-31, 200, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 2
                new Sphere(new Point(0, 220, -180), 25d).setEmission(new Color(0, 255, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(0, 220, -180), new Point(-1, 220, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 3
                new Sphere(new Point(30, 210, -180), 22d).setEmission(new Color(0, 0, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(30, 210, -180), new Point(29, 210, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Additional Balloons
                new Sphere(new Point(-20, 240, -180), 16d).setEmission(new Color(255, 165, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-20, 240, -180), new Point(-21, 240, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(10, 250, -180), 19d).setEmission(new Color(75, 0, 130))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(10, 250, -180), new Point(9, 250, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(40, 230, -180), 15d).setEmission(new Color(238, 130, 238))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(40, 230, -180), new Point(39, 230, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),


                // Building 1
                new Polygon(new Point(-300, -50, -400), new Point(-200, -50, -400), new Point(-200, 200, -400), new Point(-300, 200, -400))
                        .setEmission(grayBlue)
                        .setMaterial(reflectiveGlass),
                // Building 2
                new Polygon(new Point(-180, -50, -420), new Point(-80, -50, -420), new Point(-80, 300, -420), new Point(-180, 300, -420))
                        .setEmission(grayBlue)
                        .setMaterial(reflectiveGlass),
                // Building 3
                new Polygon(new Point(-60, -50, -450), new Point(40, -50, -450), new Point(40, 180, -450), new Point(-60, 180, -450))
                        .setEmission(grayBlue)
                        .setMaterial(reflectiveGlass),
                // Additional Buildings
                new Polygon(new Point(70, -50, -460), new Point(170, -50, -460), new Point(170, 350, -460), new Point(70, 350, -460))
                        .setEmission(grayBlue)
                        .setMaterial(reflectiveGlass),
                new Polygon(new Point(190, -50, -480), new Point(290, -50, -480), new Point(290, 400, -480), new Point(190, 400, -480))
                        .setEmission(grayBlue)
                        .setMaterial(reflectiveGlass),
                new Polygon(new Point(310, -50, -500), new Point(410, -50, -500), new Point(410, 280, -500), new Point(310, 280, -500))
                        .setEmission(grayBlue)
                        .setMaterial(reflectiveGlass)
        );


        scene.setBackground(new Color(135, 206, 250)); // Sky blue background
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new DirectionalLight(new Color(400, 400, 400), new Vector(1, -1, -1)));

        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .setVpSize(500, 500)
                .setImageWriter(new ImageWriter("landscapeWithHouseAndBalloons", 1000, 1000))
                .build()
                .renderImage()
                .writeToImage();
    }
}

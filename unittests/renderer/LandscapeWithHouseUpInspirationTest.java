package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Tests for landscape with house, mountains, balloons, and office buildings
 */
public class LandscapeWithHouseUpInspirationTest {
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Landscape with House, Balloons, and Office Buildings");
    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setRayTracer(new SimpleRayTracer(scene));
    // Office Buildings with Darker Reflective Glass in 3D using Polygons
    Material lessReflectiveGlass = new Material().setkD(0.4).setkS(0.4).setkR(0.2).setnShininess(100);
    Color darkGrayBlue = new Color(47, 79, 79); // Darker gray-blue color

    @Test
    public void landscapeWithHouseBalloonsAndOfficeBuildings() {
        scene.geometries.add(
                // Mountains
                new Sphere(new Point(-160, -50, -400), 150d).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setkD(0.3).setkS(0.1).setnShininess(100)),
                new Sphere(new Point(140, -50, -400), 200d).setEmission(new Color(15, 150, 34))
                        .setMaterial(new Material().setkD(0.2).setkS(0.1).setnShininess(100)),
                new Plane(new Point(0, -50, -300), new Vector(0, 1, 0)).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setkD(0.5).setkS(0.2).setnShininess(30)),
                // Clouds
                new Sphere(new Point(-140, 200, -150), 30d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                new Sphere(new Point(-110, 210, -160), 25d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                new Sphere(new Point(-170, 220, -140), 20d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),

                // Additional Clouds
                new Sphere(new Point(140, 200, -150), 30d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                new Sphere(new Point(110, 210, -160), 25d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                new Sphere(new Point(170, 220, -140), 20d).setEmission(new Color(255, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
                // House
                // Front wall
                new Polygon(new Point(-50, -30, -150), new Point(50, -30, -150), new Point(50, 30, -150), new Point(-50, 30, -150))
                        .setEmission(new Color(204, 204, 0)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),

                // Back wall
                new Polygon(new Point(-50, -30, -250), new Point(50, -30, -250), new Point(50, 30, -250), new Point(-50, 30, -250))
                        .setEmission(new Color(204, 204, 0)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Left wall
                new Polygon(new Point(-50, -30, -150), new Point(-50, -30, -250), new Point(-50, 30, -250), new Point(-50, 30, -150))
                        .setEmission(new Color(204, 204, 0)) // Lighter brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Right wall
                new Polygon(new Point(50, -30, -150), new Point(50, -30, -250), new Point(50, 30, -250), new Point(50, 30, -150))
                        .setEmission(new Color(204, 204, 0)) // Lighter brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
// Roof front
                new Polygon(new Point(-50, 30, -150), new Point(50, 30, -150), new Point(0, 80, -200))
                        .setEmission(new Color(139, 0, 0)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
// Roof back
                new Polygon(new Point(-50, 30, -250), new Point(50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(139, 0, 0)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
// Roof sides
                new Polygon(new Point(-50, 30, -150), new Point(-50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(139, 0, 0)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(50, 30, -150), new Point(50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(139, 0, 0)) // Dark red color
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
// Balloons
                new Sphere(new Point(-30, 200, -180), 20d).setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-30, 200, -180), new Point(-31, 200, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(0, 230, -180), 25d).setEmission(new Color(0, 255, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(0, 230, -180), new Point(-1, 230, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(35, 215, -180), 22d).setEmission(new Color(0, 0, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(35, 215, -180), new Point(34, 215, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(-40, 250, -180), 16d).setEmission(new Color(255, 165, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-40, 250, -180), new Point(-41, 250, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(15, 260, -180), 19d).setEmission(new Color(75, 0, 130))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(15, 260, -180), new Point(14, 260, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(55, 235, -180), 15d).setEmission(new Color(238, 130, 238))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(55, 235, -180), new Point(54, 235, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(20, 280, -180), 17d).setEmission(new Color(255, 105, 180))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(20, 280, -180), new Point(19, 280, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(-20, 290, -180), 18d).setEmission(new Color(0, 255, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-20, 290, -180), new Point(-21, 290, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(50, 270, -180), 20d).setEmission(new Color(0, 128, 128))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(50, 270, -180), new Point(49, 270, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(-10, 250, -180), 18d).setEmission(new Color(255, 215, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-10, 250, -180), new Point(-11, 250, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(25, 240, -180), 21d).setEmission(new Color(255, 20, 147))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(25, 240, -180), new Point(24, 240, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(10, 300, -180), 19d).setEmission(new Color(70, 130, 180))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(10, 300, -180), new Point(9, 300, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),

                new Sphere(new Point(-35, 230, -180), 17d).setEmission(new Color(154, 205, 50))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -180), new Point(1, 80, -180), new Point(-35, 230, -180), new Point(-36, 230, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Building 1
                new Polygon(new Point(-300, -50, -400), new Point(-200, -50, -400), new Point(-200, 200, -400), new Point(-300, 200, -400)) // Front
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-300, -50, -500), new Point(-200, -50, -500), new Point(-200, 200, -500), new Point(-300, 200, -500)) // Back
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-300, -50, -400), new Point(-300, -50, -500), new Point(-300, 200, -500), new Point(-300, 200, -400)) // Left
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-200, -50, -400), new Point(-200, -50, -500), new Point(-200, 200, -500), new Point(-200, 200, -400)) // Right
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-300, 200, -400), new Point(-200, 200, -400), new Point(-200, 200, -500), new Point(-300, 200, -500)) // Top
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-300, -50, -400), new Point(-200, -50, -400), new Point(-200, -50, -500), new Point(-300, -50, -500)) // Bottom
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),

                // Building 2
                new Polygon(new Point(-180, -50, -420), new Point(-80, -50, -420), new Point(-80, 300, -420), new Point(-180, 300, -420)) // Front
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-180, -50, -520), new Point(-80, -50, -520), new Point(-80, 300, -520), new Point(-180, 300, -520)) // Back
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-180, -50, -420), new Point(-180, -50, -520), new Point(-180, 300, -520), new Point(-180, 300, -420)) // Left
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-80, -50, -420), new Point(-80, -50, -520), new Point(-80, 300, -520), new Point(-80, 300, -420)) // Right
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-180, 300, -420), new Point(-80, 300, -420), new Point(-80, 300, -520), new Point(-180, 300, -520)) // Top
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-180, -50, -420), new Point(-80, -50, -420), new Point(-80, -50, -520), new Point(-180, -50, -520)) // Bottom
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),

                // Building 3
                new Polygon(new Point(-60, -50, -450), new Point(40, -50, -450), new Point(40, 230, -450), new Point(-60, 230, -450)) // Front
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-60, -50, -550), new Point(40, -50, -550), new Point(40, 230, -550), new Point(-60, 230, -550)) // Back
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-60, -50, -450), new Point(-60, -50, -550), new Point(-60, 230, -550), new Point(-60, 230, -450)) // Left
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(40, -50, -450), new Point(40, -50, -550), new Point(40, 230, -550), new Point(40, 230, -450)) // Right
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-60, 230, -450), new Point(40, 230, -450), new Point(40, 230, -550), new Point(-60, 230, -550)) // Top
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(-60, -50, -450), new Point(40, -50, -450), new Point(40, -50, -550), new Point(-60, -50, -550)) // Bottom
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),

                // Additional Buildings
                new Polygon(new Point(70, -50, -460), new Point(170, -50, -460), new Point(170, 400, -460), new Point(70, 400, -460)) // Front
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(70, -50, -560), new Point(170, -50, -560), new Point(170, 400, -560), new Point(70, 400, -560)) // Back
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(70, -50, -460), new Point(70, -50, -560), new Point(70, 400, -560), new Point(70, 400, -460)) // Left
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(170, -50, -460), new Point(170, -50, -560), new Point(170, 400, -560), new Point(170, 400, -460)) // Right
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(70, 400, -460), new Point(170, 400, -460), new Point(170, 400, -560), new Point(70, 400, -560)) // Top
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(70, -50, -460), new Point(170, -50, -460), new Point(170, -50, -560), new Point(70, -50, -560)) // Bottom
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),

                new Polygon(new Point(190, -50, -480), new Point(290, -50, -480), new Point(290, 450, -480), new Point(190, 450, -480)) // Front
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(190, -50, -580), new Point(290, -50, -580), new Point(290, 450, -580), new Point(190, 450, -580)) // Back
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(190, -50, -480), new Point(190, -50, -580), new Point(190, 450, -580), new Point(190, 450, -480)) // Left
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(290, -50, -480), new Point(290, -50, -580), new Point(290, 450, -580), new Point(290, 450, -480)) // Right
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(190, 450, -480), new Point(290, 450, -480), new Point(290, 450, -580), new Point(190, 450, -580)) // Top
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(190, -50, -480), new Point(290, -50, -480), new Point(290, -50, -580), new Point(190, -50, -580)) // Bottom
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),

                new Polygon(new Point(310, -50, -500), new Point(410, -50, -500), new Point(410, 330, -500), new Point(310, 330, -500)) // Front
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(310, -50, -600), new Point(410, -50, -600), new Point(410, 330, -600), new Point(310, 330, -600)) // Back
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(310, -50, -500), new Point(310, -50, -600), new Point(310, 330, -600), new Point(310, 330, -500)) // Left
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(410, -50, -500), new Point(410, -50, -600), new Point(410, 330, -600), new Point(410, 330, -500)) // Right
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(310, 330, -500), new Point(410, 330, -500), new Point(410, 330, -600), new Point(310, 330, -600)) // Top
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                new Polygon(new Point(310, -50, -500), new Point(410, -50, -500), new Point(410, -50, -600), new Point(310, -50, -600)) // Bottom
                        .setEmission(darkGrayBlue)
                        .setMaterial(lessReflectiveGlass),
                // Street Lamp
                // Pole
                new Polygon(new Point(-180, -50, -250), new Point(-178, -50, -250), new Point(-178, 65, -250), new Point(-180, 65, -250))
                        .setEmission(new Color(105, 105, 105)) // Dark gray color
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
//                // Lamp Head
//                new Sphere(new Point(-179, 85, -250), 20d).setEmission(new Color(250, 250, 0))
//                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.5).setnShininess(100)),
//
                new Triangle(new Point(-179 - 7.5, 55, -249 - 7.5), new Point(-179 + 7.5, 55, -249 - 7.5), new Point(-179, 65, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-179 + 7.5, 55, -249 - 7.5), new Point(-179 + 7.5, 55, -249 + 7.5), new Point(-179, 65, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-179 + 7.5, 55, -249 + 7.5), new Point(-179 - 7.5, 55, -249 + 7.5), new Point(-179, 65, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-179 - 7.5, 55, -249 + 7.5), new Point(-179 - 7.5, 55, -249 - 7.5), new Point(-179, 65, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),


                new Triangle(new Point(-179 - 7.5, 47, -249 - 7.5), new Point(-179 + 7.5, 47, -249 - 7.5), new Point(-179, 57, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-179 + 7.5, 47, -249 - 7.5), new Point(-179 + 7.5, 47, -249 + 7.5), new Point(-179, 57, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-179 + 7.5, 47, -249 + 7.5), new Point(-179 - 7.5, 47, -249 + 7.5), new Point(-179, 57, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-179 - 7.5, 47, -249 + 7.5), new Point(-179 - 7.5, 47, -249 - 7.5), new Point(-179, 57, -249))
                        .setEmission(new Color(105, 105, 105))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100))

        );

        // Swing set  with two seats and diagonal supports

        //  support
        scene.geometries.add(
                new Polygon(
                        new Point(-250, -50, -100),  // Bottom-left
                        new Point(-250, 50, -100),   // Top-middle
                        new Point(-245, 50, -100),    // Top-right
                        new Point(-245, -50, -100)   // Bottom-right
                ).setEmission(new Color(139, 69, 19))  // Brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30))
        );

// Top horizontal beam
        scene.geometries.add(
                new Polygon(
                        new Point(-300, 50, -100),   // Left-end front
                        new Point(-200, 50, -100),    // Right-end front
                        new Point(-200, 55, -100),    // Right-end top
                        new Point(-300, 55, -100)    // Left-end top
                ).setEmission(new Color(139, 69, 19))  // Brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)));

// Swing seats
        scene.geometries.add(
                new Polygon(
                        new Point(-295, 0, -100),    // Front-left
                        new Point(-275, 0, -100),    // Front-right
                        new Point(-275, 5, -100),    // Back-right
                        new Point(-295, 5, -100)     // Back-left
                ).setEmission(new Color(0, 0, 0))  // Black color (for seat)
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(
                        new Point(-225, 0, -100),     // Front-left
                        new Point(-205, 0, -100),     // Front-right
                        new Point(-205, 5, -100),     // Back-right
                        new Point(-225, 5, -100)      // Back-left
                ).setEmission(new Color(0, 0, 0))  // Black color (for seat)
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30))
        );

// Swing ropes
        scene.geometries.add(
                // Ropes for the first seat
                new Polygon(
                        new Point(-295, 0, -100),    // Bottom-left
                        new Point(-295, 50, -100),   // Top-left
                        new Point(-293, 50, -100),   // Top-right
                        new Point(-293, 0, -100)     // Bottom-right
                ).setEmission(new Color(210, 180, 140))  // Light brown color (for ropes)
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(
                        new Point(-277, 0, -100),    // Bottom-left
                        new Point(-277, 50, -100),   // Top-left
                        new Point(-275, 50, -100),   // Top-right
                        new Point(-275, 0, -100)     // Bottom-right
                ).setEmission(new Color(210, 180, 140))  // Light brown color (for ropes)
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Ropes for the second seat
                new Polygon(
                        new Point(-225, 0, -100),     // Bottom-left
                        new Point(-225, 50, -100),    // Top-left
                        new Point(-223, 50, -100),    // Top-right
                        new Point(-223, 0, -100)      // Bottom-right
                ).setEmission(new Color(210, 180, 140))  // Light brown color (for ropes)
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(
                        new Point(-207, 0, -100),     // Bottom-left
                        new Point(-207, 50, -100),    // Top-left
                        new Point(-205, 50, -100),    // Top-right
                        new Point(-205, 0, -100)      // Bottom-right
                ).setEmission(new Color(210, 180, 140))  // Light brown color (for ropes)
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30))
                );


        scene.setBackground(new Color(135, 206, 250)); // Sky blue background
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0));
        scene.lights.add(new DirectionalLight(new Color(400, 400, 400), new Vector(1, -1, -1)));
        scene.lights.add(new PointLight(new Color(255, 255, 0), new Point(-179, 62, -247)));
        scene.lights.add(new SpotLight(new Color(0, 0, 255), new Point(-179, 54, -247),new Vector(0,-1,0)).setNarrowBeam(15));


        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .setVpSize(500, 500)
                .setImageWriter(new ImageWriter("landscapeWithHouseAndBalloonsBetter1", 1000, 1000))
                .build()
                .renderImage()
                .writeToImage();
    }
}

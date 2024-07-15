/**
 *
 */
package renderer;

import static java.awt.Color.*;

import geometries.Plane;
import geometries.Polygon;
import lighting.DirectionalLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setRayTracer(new SimpleRayTracer(scene));

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setnShininess(100).setkT(0.3)),
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setkL(0.0004).setkQ(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        scene.geometries.add(
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100))
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)
                                .setkT(new Double3(0.5, 0, 0))),
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20))
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(670, 670, 3000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setkR(1)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setkR(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
                .setkL(0.00001).setkQ(0.000005));

        cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000)
                .setVpSize(2500, 2500)
                .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkT(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setkL(4E-5).setkQ(2E-7));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }


    /**
     * Tests for rendering a scene with 4 geometries and multiple lights
     * including reflections and refractions.
     */
    @Test
    public void customScene() {
        /** Scene for the tests */
        final Scene scene = new Scene("Custom Test Scene");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setnShininess(100).setkT(0.3)),
                new Sphere(new Point(100, 50, -100), 30d).setEmission(new Color(RED))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Triangle(new Point(-100, -100, -100), new Point(0, 100, -100), new Point(100, -100, -100))
                        .setEmission(new Color(GREEN))
                        .setMaterial(new Material().setkD(0.6).setkS(0.3).setnShininess(80)),
                new Plane(new Point(0, 0, -150), new Vector(0, 0, 1))
                        .setEmission((new Color(YELLOW)).add(new Color(200, 100, 50)))
                        .setMaterial(new Material().setkR(0.5))
        );

        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.2));

        scene.lights.add(new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                .setkL(0.0004).setkQ(0.0000006));
        scene.lights.add(new PointLight(new Color(500, 300, 300), new Point(50, 50, 50))
                .setkL(0.0002).setkQ(0.000002));

        cameraBuilder.setLocation(new Point(0, 0, 500)).setVpDistance(500)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("customScene", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }


    /**
     * Tests for rendering a scene of a gym with 10 geometries and multiple lights
     * including reflections and refractions.
     */
    @Test
    public void gymScene() {
        /** Scene for the tests */
        final Scene scene = new Scene("Gym Scene");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(-1, 0, 0), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
        scene.geometries.add(
                // קיר קדמי
                new Polygon(new Point(-200, -100, -300), new Point(200, -100, -300),
                        new Point(200, 100, -300), new Point(-200, 100, -300))
                        .setEmission(new Color(169, 169, 169)) // צבע אפור
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // קיר אחורי
                new Polygon(new Point(-200, -100, 300), new Point(200, -100, 300),
                        new Point(200, 100, 300), new Point(-200, 100, 300))
                        .setEmission(new Color(169, 169, 169)) // צבע אפור
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // קיר שמאלי
                new Polygon(new Point(-200, -100, -300), new Point(-200, -100, 300),
                        new Point(-200, 100, 300), new Point(-200, 100, -300))
                        .setEmission(new Color(169, 169, 169)) // צבע אפור
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // קיר ימני
                new Polygon(new Point(200, -100, -300), new Point(200, -100, 300),
                        new Point(200, 100, 300), new Point(200, 100, -300))
                        .setEmission(new Color(169, 169, 169)) // צבע אפור
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // רצפה
                new Polygon(new Point(-200, -100, -300), new Point(200, -100, -300),
                        new Point(200, -100, 300), new Point(-200, -100, 300))
                        .setEmission(new Color(105, 105, 105)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // גג
                new Polygon(new Point(-200, 100, -300), new Point(200, 100, -300),
                        new Point(200, 100, 300), new Point(-200, 100, 300))
                        .setEmission(new Color(192, 192, 192)) // צבע אפור בהיר
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // מראה גדולה על הקיר הימני
                new Polygon(new Point(190, -90, -200), new Point(190, -90, 200),
                        new Point(190, 90, 200), new Point(190, 90, -200))
                        .setEmission(new Color(BLACK)) // צבע שחור כדי לא להוסיף צבע למראה
                        .setMaterial(new Material().setkR(1)), // מקדם החזרה גבוה כדי לדמות מראה
                // כדור התעמלות ראשון
                new Sphere(new Point(-100, -70, -100), 30d).setEmission(new Color(255, 0, 0)) // צבע אדום
                        .setMaterial(new Material().setkD(0.6).setkS(0.4).setnShininess(50)), // פרמטרים של גומי
                // כדור התעמלות שני
                new Sphere(new Point(0, -70, 0), 30d).setEmission(new Color(0, 255, 0)) // צבע ירוק
                        .setMaterial(new Material().setkD(0.6).setkS(0.4).setnShininess(50)), // פרמטרים של גומי
                // כדור התעמלות שלישי
                new Sphere(new Point(100, -70, 100), 30d).setEmission(new Color(0, 0, 255)) // צבע כחול
                        .setMaterial(new Material().setkD(0.6).setkS(0.4).setnShininess(50)) // פרמטרים של גומי
        );

        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(0, 100, 0), new Vector(0, -1, 0))
                .setkL(4E-5).setkQ(2E-7));
        scene.lights.add(new PointLight(new Color(500, 300, 300), new Point(0, 100, 0))
                .setkL(0.0002).setkQ(0.000002));

        cameraBuilder.setLocation(new Point(200, 0, 0)).setVpDistance(300)
                .setVpSize(400, 400)
                .setImageWriter(new ImageWriter("gymScene", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }


    /**
     * Produce a picture of a custom landscape scene with mountains and clouds
     */
    @Test
    public void landscapeScene() {
        /** Scene for the tests */
        final Scene scene = new Scene("Landscape Scene");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
        scene.geometries.add(
                // קרקע
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(34, 139, 34)) // צבע ירוק כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // הר ראשון
                new Sphere(new Point(-100, -50, -200), 100d).setEmission(new Color(139, 69, 19)) // צבע חום
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // הר שני
                new Sphere(new Point(0, -50, -300), 150d).setEmission(new Color(160, 82, 45)) // צבע חום בהיר יותר
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // הר שלישי
                new Sphere(new Point(150, -50, -400), 200d).setEmission(new Color(139, 69, 19)) // צבע חום
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // ענן ראשון
                new Sphere(new Point(-150, 100, -250), 40d).setEmission(new Color(255, 255, 255)) // צבע לבן
                        .setMaterial(new Material().setkD(0.9).setkS(0.1).setnShininess(10).setkT(0.6)),
                new Sphere(new Point(-110, 120, -230), 30d).setEmission(new Color(255, 255, 255)) // צבע לבן
                        .setMaterial(new Material().setkD(0.9).setkS(0.1).setnShininess(10).setkT(0.6)),
                new Sphere(new Point(-170, 120, -270), 30d).setEmission(new Color(255, 255, 255)) // צבע לבן
                        .setMaterial(new Material().setkD(0.9).setkS(0.1).setnShininess(10).setkT(0.6)),
                // ענן שני
                new Sphere(new Point(100, 150, -350), 50d).setEmission(new Color(255, 255, 255)) // צבע לבן
                        .setMaterial(new Material().setkD(0.9).setkS(0.1).setnShininess(10).setkT(0.6)),
                new Sphere(new Point(140, 170, -370), 40d).setEmission(new Color(255, 255, 255)) // צבע לבן
                        .setMaterial(new Material().setkD(0.9).setkS(0.1).setnShininess(10).setkT(0.6)),
                new Sphere(new Point(160, 150, -330), 30d).setEmission(new Color(255, 255, 255)) // צבע לבן
                        .setMaterial(new Material().setkD(0.9).setkS(0.1).setnShininess(10).setkT(0.6))
        );

        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

        scene.lights.add(new DirectionalLight(new Color(255, 223, 0), new Vector(-1, -1, -1))); // אור צהוב, כמו שמש

        cameraBuilder.setLocation(new Point(0, 0, 500)).setVpDistance(500)
                .setVpSize(400, 400)
                .setImageWriter(new ImageWriter("landscapeScene", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }


    @Test
    public void landscapeWithHouse() {
        /** Scene for the tests */
        final Scene scene = new Scene("Landscape with House");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
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
                        .setEmission(new Color(139, 69, 19)) // צבע חום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Back wall
                new Polygon(new Point(-50, -30, -250), new Point(50, -30, -250), new Point(50, 30, -250), new Point(-50, 30, -250))
                        .setEmission(new Color(139, 69, 19)) // צבע חום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Left wall
                new Polygon(new Point(-50, -30, -150), new Point(-50, -30, -250), new Point(-50, 30, -250), new Point(-50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // צבע חום בהיר יותר
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Right wall
                new Polygon(new Point(50, -30, -150), new Point(50, -30, -250), new Point(50, 30, -250), new Point(50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // צבע חום בהיר יותר
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof front
                new Polygon(new Point(-50, 30, -150), new Point(50, 30, -150), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // צבע אדום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof back
                new Polygon(new Point(-50, 30, -250), new Point(50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // צבע אדום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Chimney
                new Polygon(new Point(-10, 80, -180), new Point(10, 80, -180), new Point(10, 110, -180), new Point(-10, 110, -180)) // Front face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-10, 80, -190), new Point(10, 80, -190), new Point(10, 110, -190), new Point(-10, 110, -190)) // Back face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-10, 80, -180), new Point(-10, 80, -190), new Point(-10, 110, -190), new Point(-10, 110, -180)) // Left face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(10, 80, -180), new Point(10, 80, -190), new Point(10, 110, -190), new Point(10, 110, -180)) // Right face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30))
        );

        scene.setBackground(new Color(135, 206, 250)); // צבע רקע תכלת
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new DirectionalLight(new Color(400, 400, 400), new Vector(1, -1, -1)));

        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .setVpSize(500, 500)
                .setImageWriter(new ImageWriter("landscapeWithHouse", 1000, 1000))
                .build()
                .renderImage()
                .writeToImage();
    }


    @Test
    public void landscapeWithHouse2() {
        /** Scene for the tests */
        final Scene scene = new Scene("Landscape with House");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
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
                        .setEmission(new Color(139, 69, 19)) // צבע חום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Back wall
                new Polygon(new Point(-50, -30, -250), new Point(50, -30, -250), new Point(50, 30, -250), new Point(-50, 30, -250))
                        .setEmission(new Color(139, 69, 19)) // צבע חום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Left wall
                new Polygon(new Point(-50, -30, -150), new Point(-50, -30, -250), new Point(-50, 30, -250), new Point(-50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // צבע חום בהיר יותר
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Right wall
                new Polygon(new Point(50, -30, -150), new Point(50, -30, -250), new Point(50, 30, -250), new Point(50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // צבע חום בהיר יותר
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof front
                new Polygon(new Point(-50, 30, -150), new Point(50, 30, -150), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // צבע אדום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof back
                new Polygon(new Point(-50, 30, -250), new Point(50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // צבע אדום כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Chimney
                new Polygon(new Point(-10, 80, -180), new Point(10, 80, -180), new Point(10, 110, -180), new Point(-10, 110, -180)) // Front face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-10, 80, -190), new Point(10, 80, -190), new Point(10, 110, -190), new Point(-10, 110, -190)) // Back face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-10, 80, -180), new Point(-10, 80, -190), new Point(-10, 110, -190), new Point(-10, 110, -180)) // Left face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(10, 80, -180), new Point(10, 80, -190), new Point(10, 110, -190), new Point(10, 110, -180)) // Right face
                        .setEmission(new Color(169, 169, 169)) // צבע אפור כהה
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Balloons
                new Sphere(new Point(-30, 150, -180), 10d).setEmission(new Color(255, 0, 0)) // Balloon 1
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(0, 160, -200), 10d).setEmission(new Color(0, 255, 0)) // Balloon 2
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(30, 150, -180), 10d).setEmission(new Color(0, 0, 255)) // Balloon 3
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Polygon(new Point(-30, 30, -180), new Point(-29, 30, -180), new Point(-29, 150, -180), new Point(-30, 150, -180)) // String 1
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Polygon(new Point(0, 30, -200), new Point(1, 30, -200), new Point(1, 160, -200), new Point(0, 160, -200)) // String 2
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Polygon(new Point(30, 30, -180), new Point(31, 30, -180), new Point(31, 150, -180), new Point(30, 150, -180)) // String 3
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100))
        );

        scene.setBackground(new Color(135, 206, 250)); // צבע רקע תכלת
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


    @Test
    public void landscapeWithHouseAndBalloons() {
        /** Scene for the tests */
        final Scene scene = new Scene("Landscape with House and Balloons");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
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
                new Polygon(new Point(-10, 80, -180), new Point(10, 80, -180), new Point(10, 110, -180), new Point(-10, 110, -180)) // Front face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-10, 80, -190), new Point(10, 80, -190), new Point(10, 110, -190), new Point(-10, 110, -190)) // Back face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(-10, 80, -180), new Point(-10, 80, -190), new Point(-10, 110, -190), new Point(-10, 110, -180)) // Left face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(10, 80, -180), new Point(10, 80, -190), new Point(10, 110, -190), new Point(10, 110, -180)) // Right face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Balloons
                // Balloon 1
                new Sphere(new Point(-30, 150, -180), 10d).setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(-30, 30, -180), new Point(-29, 30, -180), new Point(-29, 150, -180), new Point(-30, 150, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 2
                new Sphere(new Point(0, 160, -200), 12d).setEmission(new Color(0, 255, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 30, -200), new Point(1, 30, -200), new Point(1, 160, -200), new Point(0, 160, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 3
                new Sphere(new Point(30, 150, -180), 8d).setEmission(new Color(0, 0, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.5).setnShininess(100)),
                new Polygon(new Point(30, 30, -180), new Point(31, 30, -180), new Point(31, 150, -180), new Point(30, 150, -180))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Additional Balloons
                new Sphere(new Point(-20, 170, -190), 9d).setEmission(new Color(255, 165, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(-20, 30, -190), new Point(-19, 30, -190), new Point(-19, 170, -190), new Point(-20, 170, -190))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(10, 180, -190), 11d).setEmission(new Color(75, 0, 130))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(10, 30, -190), new Point(11, 30, -190), new Point(11, 180, -190), new Point(10, 180, -190))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                new Sphere(new Point(40, 160, -195), 10d).setEmission(new Color(238, 130, 238))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(40, 30, -195), new Point(41, 30, -195), new Point(41, 160, -195), new Point(40, 160, -195))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100))
        );

        scene.setBackground(new Color(135, 206, 250)); // Sky blue background
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new DirectionalLight(new Color(400, 400, 400), new Vector(1, -1, -1)));

        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .setVpSize(500, 500)
                .setImageWriter(new ImageWriter("landscapeWithHouseAndBalloons2", 1000, 1000))
                .build()
                .renderImage()
                .writeToImage();
    }


    @Test
    public void landscapeWithHouseAndBalloons3() {
        /** Scene for the tests */
        final Scene scene = new Scene("Landscape with House and Balloons");
        /** Camera builder for the tests */
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setRayTracer(new SimpleRayTracer(scene));
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
                // House rotated 30 degrees
                // Front wall
                new Polygon(new Point(-50, -30, -150), new Point(50, -30, -250), new Point(50, 30, -250), new Point(-50, 30, -150))
                        .setEmission(new Color(139, 69, 19)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Back wall
                new Polygon(new Point(-50, -30, -250), new Point(50, -30, -350), new Point(50, 30, -350), new Point(-50, 30, -250))
                        .setEmission(new Color(139, 69, 19)) // Dark brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Left wall
                new Polygon(new Point(-50, -30, -150), new Point(-50, -30, -250), new Point(-50, 30, -250), new Point(-50, 30, -150))
                        .setEmission(new Color(160, 82, 45)) // Lighter brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Right wall
                new Polygon(new Point(50, -30, -250), new Point(50, -30, -350), new Point(50, 30, -350), new Point(50, 30, -250))
                        .setEmission(new Color(160, 82, 45)) // Lighter brown color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof front
                new Polygon(new Point(-50, 30, -150), new Point(50, 30, -250), new Point(0, 80, -200))
                        .setEmission(new Color(178, 34, 34)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Roof back
                new Polygon(new Point(-50, 30, -250), new Point(50, 30, -350), new Point(0, 80, -300))
                        .setEmission(new Color(178, 34, 34)) // Dark red color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Chimney
                new Polygon(new Point(15, 80, -210), new Point(25, 80, -210), new Point(25, 110, -210), new Point(15, 110, -210)) // Front face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(15, 80, -220), new Point(25, 80, -220), new Point(25, 110, -220), new Point(15, 110, -220)) // Back face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(15, 80, -210), new Point(15, 80, -220), new Point(15, 110, -220), new Point(15, 110, -210)) // Left face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                new Polygon(new Point(25, 80, -210), new Point(25, 80, -220), new Point(25, 110, -220), new Point(25, 110, -210)) // Right face
                        .setEmission(new Color(169, 169, 169)) // Dark grey color
                        .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(30)),
                // Balloons
                // Balloon 1
                new Sphere(new Point(-30, 150, -180), 10d).setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.1).setnShininess(100)),
                new Polygon(new Point(0, 80, -200), new Point(-30, 150, -180), new Point(-29, 150, -180), new Point(0, 80, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 2
                new Sphere(new Point(0, 160, -200), 12d).setEmission(new Color(0, 255, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -200), new Point(0, 160, -200), new Point(1, 160, -200), new Point(0, 80, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 3
                new Sphere(new Point(30, 150, -180), 8d).setEmission(new Color(0, 0, 255))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -200), new Point(30, 150, -180), new Point(31, 150, -180), new Point(0, 80, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 4
                new Sphere(new Point(-20, 170, -190), 9d).setEmission(new Color(255, 165, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.2).setnShininess(100)),
                new Polygon(new Point(0, 80, -200), new Point(-20, 170, -190), new Point(-19, 170, -190), new Point(0, 80, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 5
                new Sphere(new Point(10, 180, -190), 11d).setEmission(new Color(75, 0, 130))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.3).setnShininess(100)),
                new Polygon(new Point(0, 80, -200), new Point(10, 180, -190), new Point(11, 180, -190), new Point(0, 80, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
                // Balloon 6
                new Sphere(new Point(40, 160, -195), 10d).setEmission(new Color(238, 130, 238))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setkT(0.4).setnShininess(100)),
                new Polygon(new Point(0, 80, -200), new Point(40, 160, -195), new Point(41, 160, -195), new Point(0, 80, -200))
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100))
        );

        scene.setBackground(new Color(135, 206, 250)); // Sky blue background
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new DirectionalLight(new Color(400, 400, 400), new Vector(1, -1, -1)));

        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .setVpSize(500, 500)
                .setImageWriter(new ImageWriter("landscapeWithHouseAndBalloons2", 1000, 1000))
                .build()
                .renderImage()
                .writeToImage();
    }


}

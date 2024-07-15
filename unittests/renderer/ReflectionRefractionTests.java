/**
 * 
 */
package renderer;

import static java.awt.Color.*;

import geometries.Plane;
import geometries.Polygon;
import lighting.DirectionalLight;
import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/** Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author dzilb */
public class ReflectionRefractionTests {
   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()
      .setDirection(new Vector(0,0,-1), new Vector(0,1,0))
      .setRayTracer(new SimpleRayTracer(scene));

   /** Produce a picture of a sphere lighted by a spot light */
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

   /** Produce a picture of a sphere lighted by a spot light */
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

   /** Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow */
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
                    .setImageWriter(new ImageWriter("landscapeWithHouseAndBalloons", 1000, 1000))
                    .build()
                    .renderImage()
                    .writeToImage();
        }

}

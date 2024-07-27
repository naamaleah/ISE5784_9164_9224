package renderer;

import primitives.*;
import primitives.Vector;


import java.util.*;
import java.util.stream.IntStream;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Camera is the  class representing a Camera of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Camera implements Cloneable {

    /**
     * vector pointing towards view plane (-Z axis)
     */
    private Vector vTo;
    /**
     * vector pointing up ( Y axis)
     */
    private Vector vUp;
    /**
     * camera's position point in 3D space
     */
    private Point p0;
    /**
     * vector pointing right ( X axis)
     */
    private Vector vRight;
    /**
     * width of view plane size
     */
    private double width = 0.0;
    /**
     * height of view plane size
     */
    private double height = 0.0;
    /**
     * distance between view plane from camera
     */
    private double distance = 0.0;
    /**
     * image writing to file functionality object
     */
    private ImageWriter imageWriter;
    /**
     * calculate color of pixel functionality object
     */
    private RayTracerBase rayTracer;
    /**
     * number of rays through a pixel
     */
    private int antiAliasing = 1;
    /**
     * optimize with adaptive
     */
    private boolean adaptive = false;
    /**
     * optimize with threads
     */
    private boolean threads = false;
    /**
     * Pixel Manager object to support multi-threading
     */
    private PixelManager pixelManager;

    /**
     * Progress percentage printing interval
     */
    private long printInterval = 1;


    /**
     * Empty constructor
     */
    private Camera() {
    }

    /**
     * A static method , which will return a new object of the Builder class
     *
     * @return new object of the Builder class
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * render image "captured" through view plane
     */
    public Camera renderImage() {
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        pixelManager = new PixelManager(nY, nX, printInterval);
        if (threads) {
            if (!adaptive) {
                IntStream.range(0, nX).parallel().forEach(x -> {
                    IntStream.range(0, nY).parallel().forEach(y -> {
                        PixelManager.Pixel pixel = pixelManager.nextPixel();
                        this.castRays(nX, nY, pixel.col(), pixel.row());
                        pixelManager.pixelDone();
                    });
                });
            } else {
                IntStream.range(0, nX).parallel().forEach(x -> {
                    IntStream.range(0, nY).parallel().forEach(y -> {
                        PixelManager.Pixel pixel = pixelManager.nextPixel();
                        imageWriter.writePixel(pixel.col(), pixel.row(), AdaptiveSuperSampling(imageWriter.getNx(), imageWriter.getNy(), pixel.col(), pixel.row(), antiAliasing));
                        pixelManager.pixelDone();
                    });
                });
            }
        } else {
            if (!adaptive) {
                for (int i = 0; i < nX; i++)
                    for (int j = 0; j < nY; j++) {
                        castRays(nX, nY, i, j);
                    }
            } else {
                for (int i = 0; i < nX; i++)
                    for (int j = 0; j < nY; j++) {
                        imageWriter.writePixel(i, j, AdaptiveSuperSampling(imageWriter.getNx(), imageWriter.getNy(), i, j, antiAliasing));
                    }
            }

        }
        return this;
    }

    /**
     * cast a ray from camera through pixel (i,j) in view plane and get color of pixel
     *
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     */
    private void castRays(int Nx, int Ny, int j, int i) {
        List<Ray> rays = constructRays(Nx, Ny, j, i, antiAliasing);
        Color color = rayTracer.traceRays(rays);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Checks the color of the pixel with the help of individual rays and averages between them and only
     * if necessary continues to send beams of rays in recursion
     *
     * @param nX        Pixel length
     * @param nY        Pixel width
     * @param j         The position of the pixel relative to the y-axis
     * @param i         The position of the pixel relative to the x-axis
     * @param numOfRays The amount of rays sent
     * @return Pixel color
     */
    private Color AdaptiveSuperSampling(int nX, int nY, int j, int i, int numOfRays) {
        int numOfRaysInRowCol = (int) Math.floor(Math.sqrt(numOfRays));
        if (numOfRaysInRowCol == 1) return rayTracer.traceRay(constructRay(nX, nY, j, i));

        Point pIJ = getCenterOfPixel(nX, nY, j, i);

        double rY = alignZero(height / nY);
        double rX = alignZero(width / nX);


        double PRy = rY / numOfRaysInRowCol;
        double PRx = rX / numOfRaysInRowCol;
        return rayTracer.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy, p0, vRight, vUp, null);
    }

    /**
     * construct ray through a pixel in the view plane
     * nX and nY create the resolution
     *
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index row in the view plane
     * @param i  index column in the view plane
     * @return ray that goes through the pixel (j, i)  Ray(p0, Vi,j)
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pIJ = getCenterOfPixel(nX, nY, j, i);
        Vector vIJ = pIJ.subtract(p0);
        return new Ray(p0, vIJ);
    }

    /**
     * print grid lines on  image
     *
     * @param interval interval between each pair of grid lines
     * @param color    color of grid lines
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("image writer is not initialized", ImageWriter.class.getName(), "");
        for (int i = 0; i < imageWriter.getNy(); i++) {
            for (int j = 0; j < imageWriter.getNx(); j++) {
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, color);
            }
        }
        return this;
    }

    /**
     * create a jpeg file, with scene "captured" by camera
     */
    public Camera writeToImage() {
        if (imageWriter == null)
            throw new MissingResourceException("image writer is not initialized", ImageWriter.class.getName(), "");
        imageWriter.writeToImage();
        return this;
    }

    /**
     * get the center point of the pixel in the view plane
     *
     * @param nX number of rows in view plane
     * @param nY number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @return the center point of the pixel
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i) {
        Point Pc = p0.add(vTo.scale(distance));

        double Ry = height / nY;
        double Rx = width / nX;

        double Yi = -(i - (nY - 1) / 2d) * Ry;
        double Xj = (j - (nX - 1) / 2d) * Rx;

        Point Pij = Pc;

        if (!isZero(Xj)) {
            Pij = Pij.add(vRight.scale(Xj));
        }

        if (!isZero(Yi)) {
            Pij = Pij.add(vUp.scale(Yi));
        }

        return Pij;
    }


    /**
     * Creates a beam of rays into a square grid
     *
     * @param nX        Pixel length
     * @param nY        Pixel width
     * @param j         Position the pixel on the y-axis inside the grid
     * @param i         Position the pixel on the x-axis inside the grid
     * @param numOfRays The root of the number of beams sent per pixel
     * @return List of beams of rays
     */
    public List<Ray> constructRays(int nX, int nY, int j, int i, int numOfRays) {
        if (numOfRays == 0) {
            throw new IllegalArgumentException("num Of Rays can not be 0");
        }
        if (numOfRays == 1) {
            return List.of(new Ray(p0, getCenterOfPixel(nX, nY, j, i).subtract(p0)));
        } else {
            List<Ray> rays = new LinkedList<>();
            Point pIJ = getCenterOfPixel(nX, nY, j, i);

            double rY = alignZero(height / nY);

            double rX = alignZero(width / nX);

            double pY = alignZero(rY / numOfRays);
            double pX = alignZero(rX / numOfRays);
            Point PijTemP = pIJ;
            for (int p = 1; p < numOfRays; p++) {
                for (int m = 1; m < numOfRays; m++) {
                    PijTemP = pIJ.add(vRight.scale(pX * m)).add(vUp.scale(pY * p));
                    rays.add(new Ray(p0, PijTemP.subtract(p0).normalize()));
                }
            }
            return rays;
        }
    }

    /**
     * Rotates the camera around the axes with the given angles
     *
     * @param x angles to rotate around the x axis
     * @param y angles to rotate around the y axis
     * @param z angles to rotate around the z axis
     * @return the current camera
     */
    public Camera rotate(double x, double y, double z) {
        vTo = vTo.rotateX(x).rotateY(y).rotateZ(z);
        vUp = vUp.rotateX(x).rotateY(y).rotateZ(z);
        vRight = vTo.crossProduct(vUp);

        return this;
    }

    /**
     * Adds the given amount to the camera's position
     *
     * @return the current camera
     */
    public Camera move(Vector amount) {
        p0 = p0.add(amount);
        return this;
    }

    /**
     * Adds x, y, z to the camera's position
     *
     * @return the current camera
     */
    public Camera move(double x, double y, double z) {
        return move(new Vector(x, y, z));
    }



    /**
     * A nested class to implement a constructor template for the camera
     */
    public static class Builder {
        private final Camera camera = new Camera();

        /**
         * Sets the location point for the camera.
         *
         * @param p0 the location Point to set
         * @return the Builder instance with the updated Height and Width
         */
        public Builder setLocation(Point p0) {
            this.camera.p0 = p0;
            return this;
        }

        /**
         * Sets the Direction for the camera.
         *
         * @param vTo the vector pointing toward the screen to set
         * @param vUp the vector pointing upward to set
         * @return the Builder instance with the updated vTo and vUp
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo.dotProduct(vUp) != 0)
                throw new IllegalArgumentException("Not orthogonal Vectors");
            this.camera.vTo = vTo.normalize();
            this.camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the camera position and direction based on a target point.
         * @param target the point the camera is aimed at
         * @param vUp the up direction for the camera
         * @return the Builder instance with the updated location and direction
         */
        public Builder setDirection(Point target, Vector vUp ) {
            this.camera.vTo = target.subtract(this.camera.p0).normalize();
            this.camera.vUp = vUp.subtract(this.camera.vTo.scale(vUp.dotProduct(this.camera.vTo))).normalize();
            this.camera.vRight = this.camera.vUp.crossProduct(vUp).normalize();
            return this;
        }

        /**
         * Sets the View plane size for the camera.
         *
         * @param width  the Width to set
         * @param height the Height to set
         * @return the Builder instance with the updated Height and Width
         */
        public Builder setVpSize(double width, double height) {
            if (width < 0 || height < 0)
                throw new IllegalArgumentException("Height and length can not be negative numbers");
            this.camera.width = width;
            this.camera.height = height;
            return this;
        }

        /**
         * Sets the Distance for the camera.
         *
         * @param distance the Distance to set
         * @return the Builder instance with the updated Distance
         */
        public Builder setVpDistance(double distance) {
            if (distance < 0) throw new IllegalArgumentException("Distance can not be negative numbers");
            this.camera.distance = distance;
            return this;
        }

        /**
         * Sets the ImageWriter for the camera.
         *
         * @param imageWriter the ImageWriter to set
         * @return the Builder instance with the updated ImageWriter
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            this.camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the RayTracer for the camera.
         *
         * @param rayTracer the RayTracer to set
         * @return the Builder instance with the updated RayTracer
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            this.camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * set the anti Aliasing
         *
         * @return the Camera object
         */
        public Builder setantiAliasing(int antiAliasing) {
            this.camera.antiAliasing = antiAliasing;
            return this;
        }

        /**
         * set the adaptive
         *
         * @return the Camera object
         */
        public Builder setadaptive(boolean adaptive) {
            this.camera.adaptive = adaptive;
            return this;
        }

        /**
         * set the threads
         *
         * @return the Camera object
         */
        public Builder setThreads() {
            this.camera.threads = true;
            return this;
        }


        /**
         * Builds and returns the Camera instance.
         * <p>
         * Checks for missing essential fields and throws a MissingResourceException if any are missing.
         * If all fields are set, calculates the vRight vector and returns a cloned Camera instance.
         *
         * @return the built Camera instance
         * @throws MissingResourceException if any required field is missing
         */
        public Camera build() {
            String message = "Missing info for render";
            String className = "Camera";

            if (camera.height == 0.0)
                throw new MissingResourceException(message, className, "Missing height");
            if (camera.width == 0.0)
                throw new MissingResourceException(message, className, "Missing width");
            if (camera.distance == 0.0)
                throw new MissingResourceException(message, className, "Missing distance");
            if (camera.vTo == null)
                throw new MissingResourceException(message, className, "Missing vTo");
            if (camera.vUp == null)
                throw new MissingResourceException(message, className, "Missing vUp");
            if (camera.imageWriter == null)
                throw new MissingResourceException(message, className, "Missing imageWriter");
            if (camera.rayTracer == null)
                throw new MissingResourceException(message, className, "Missing rayTracer");
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

    }
}

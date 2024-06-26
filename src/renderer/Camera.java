package renderer;
import primitives.*;
import primitives.Vector;


import java.util.*;

import static primitives.Util.isZero;

/**
 * Class Camera is the  class representing a Camera of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */
public class Camera implements Cloneable {

    private Vector vTo;
    private Vector vUp;
    private Point p0;
    private Vector vRight;
    double width=0.0;
    double height=0.0;
    double distance=0.0;
    ImageWriter imageWriter;
    RayTracerBase rayTracer;


    /**
     * Empty constructor
     */
    private Camera() {
    }

    /**
     * A static method , which will return a new object of the Builder class
     * @return new object of the Builder class
     */
    public static Builder getBuilder(){
        return new Builder();
    }

    /**
     * render image "captured" through view plane
     */
    public Camera renderImage(){
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        for (int i = 0; i < nX ; i++)
            for (int j = 0; j < nY; j++){
                castRay(nX,nY,i,j);
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
    private void castRay(int Nx, int Ny, int j, int i) {
        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);
        // return the color using ray tracer
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * print grid lines on  image
     * @param interval interval between each pair of grid lines
     * @param color    color of grid lines
     */
    public Camera printGrid(int interval, Color color){
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
    public Camera writeToImage(){
        if (imageWriter == null)
            throw new MissingResourceException("image writer is not initialized", ImageWriter.class.getName(), "");
        imageWriter.writeToImage();
        return this;
    }

    /**
     * construct ray from a {@link Camera} towards center of a pixel in a view plane
     * @param nX number of rows in view plane
     * @param nY number of columns in view plane
     * @param j column index of pixel
     * @param i row index of pixel
     * @return {@link Ray} from camera to center of pixel (i,j)
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        //view plane center:
        Point Pc= p0.add(vTo.scale(distance));

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        double Ry=height/nY;
        double Rx=width/nX;

        // calculate necessary "size" needed to move from
        // center of view plane to reach the middle point of pixel (i,j)
        double Yi=-(i-(nY-1)/2d)*Ry;
        double Xj=(j-(nX-1)/2d)*Rx;

        // set result point to middle of view plane
        Point Pij=Pc;

        // if result of xJ is > 0
        // move result point left/right on  X axis
        // to reach middle point of pixel (i,j) (on X axis direction)
        if (!isZero(Xj)) {
            Pij = Pij.add(vRight.scale(Xj));
        }

        // if result of yI is > 0
        // move result point up/down on Y axis
        // to reach middle point of pixel (i,j)
        if (!isZero(Yi)) {
            Pij = Pij.add(vUp.scale(Yi));
        }

        //return ray from camera to middle point of pixel(i,j) in view plane
        return new Ray(p0 ,Pij.subtract(p0));
    }

    /**
     * A nested class to implement a constructor template for the camera
     */
    public static class Builder{
        private final Camera camera=new Camera();
        public Builder setLocation(Point p){
            this.camera.p0=p;
            return this;
        }
        public Builder setDirection(Vector vTo,Vector vUp){
            if(vTo.dotProduct(vUp) != 0)
                throw new IllegalArgumentException("Not orthogonal Vectors");
            this.camera.vTo=vTo.normalize();
            this.camera.vUp=vUp.normalize();
            return this;
        }
        public Builder setVpSize(double width,double height){
            if(width<0 ||height<0) throw new IllegalArgumentException("Height and length can not be negative numbers");
            this.camera.width=width;
            this.camera.height=height;
            return this;
        }
        public Builder setVpDistance(double distance){
            if(distance<0) throw new IllegalArgumentException("Distance can not be negative numbers");
            this.camera.distance=distance;
            return this;
        }
        public Builder setImageWriter(ImageWriter imageWriter){
            this.camera.imageWriter=imageWriter;
            return this;
        }

        /**
         * Ray-Tracer setter
         * @param rayTracer Ray-Tracer
         * @return Returns itself
         */
        public Builder setRayTracer(RayTracerBase rayTracer){
            this.camera.rayTracer=rayTracer;
            return this;
        }
        public Camera build(){
            String message="Missing info for render";
            String className="Camera";

            //Checking for missing arguments
            if(camera.height==0.0 )
                throw new MissingResourceException(message,className, "Missing height");
            if(camera.width==0.0 )
                throw new MissingResourceException(message,className, "Missing width");
            if(camera.distance==0.0)
                throw new MissingResourceException(message,className, "Missing distance");
            if(camera.vTo==null )
                throw new MissingResourceException(message,className, "Missing vTo");
            if(camera.vUp==null )
                throw new MissingResourceException(message,className, "Missing vUp");
            if(camera.imageWriter==null )
                throw new MissingResourceException(message,className, "Missing imageWriter");
            if(camera.rayTracer==null )
                throw new MissingResourceException(message,className, "Missing rayTracer");
            camera.vRight=camera.vTo.crossProduct(camera.vUp).normalize();

            try
            {
                return (Camera) camera.clone();
            }
            catch (CloneNotSupportedException e){
                return null;
            }
        }

    }
}

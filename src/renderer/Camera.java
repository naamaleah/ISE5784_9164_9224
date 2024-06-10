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
        double Yi=-(i-(nY-1)/2)*Ry;
        double Xj=(j-(nX-1)/2)*Rx;

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

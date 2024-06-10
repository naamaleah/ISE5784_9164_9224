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
     *
     */
    public static Builder getBuilder(){
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i){
        Point Pc= p0.add(vTo.scale(distance));
        double Ry=height/nY;
        double Rx=width/nX;
        double Yi=-(i-(nY-1)/2)*Ry;
        double Xj=(j-(nX-1)/2)*Rx;
        Point Pij=Pc;
        if (!isZero(Xj)) {
            Pij = Pij.add(vRight.scale(Xj));
        }

        if (!isZero(Yi)) {
            Pij = Pij.add(vUp.scale(Yi));
        }

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

package renderer;
import primitives.*;

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
//    public Camera(Point p0,Vector vTo,Vector vUp) {
//        this.vTo=vTo;
//        this.vUp=vUp;
//        this.p0=p0;
//        this.vRight= vTo.crossProduct(vUp).normalize();
//    }

    /**
     * Empty constructor
     */
    private Camera() {
    }

    /**
     *
     */
    public static Builder getBuilder(){
        return Builder;
    }

    public Ray constructRay(int nX, int nY, int j, int i){
        return null;
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
            this.camera.vTo=vTo;
            this.camera.vUp=vUp;
            return this;
        }
        public Builder setVpSize(double width,double height){
            if(width<0 ||height<0) throw new IllegalArgumentException("Height and length can not be negative numbers");
            this.camera.width=width;
            this.camera.height=height;
            return this;
        }
        public Builder setVpDistance(double distance){
            this.camera.distance=distance;
            return this;
        }
        public Camera build(){
            if(camera.height==0.0 || camera.width==0.0 || camera.distance)
            return (Camera) camera.clone();
        }

    }
}

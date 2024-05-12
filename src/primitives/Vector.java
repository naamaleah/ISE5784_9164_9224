package primitives;

public class Vector extends Point {

    public Vector(double x, double y,double z) {
        super(x,y,z);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }

    public Vector(Double3 d) {
        super(d);
        if(d.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }

    @Override
    public boolean equals (Object obj){
        if(this== obj) return true;
        return obj instanceof Vector other &&  super.equals(other);
    }

    @Override
    public String toString(){ return "->" + super.toString(); }

    public Vector add(Vector v){
        return new Vector(this.xyz.add(v.xyz));
    }

    public Vector scale(double x){
        return new Vector(this.xyz.scale(x));
    }

    public double dotProduct(Vector v){
        return xyz.d1*v.xyz.d1+xyz.d2*v.xyz.d2+xyz.d3*v.xyz.d3;
    }

    public Vector crossProduct (Vector v){
        return new Vector(this.xyz.d2 * v.xyz.d3 - this.xyz.d3  * v.xyz.d2,this.xyz.d3  * v.xyz.d1 - this.xyz.d1 * v.xyz.d3 ,this.xyz.d1 * v.xyz.d2 - this.xyz.d2 * v.xyz.d1);
    }

    public double lengthSquared(){
        return dotProduct(this);
    }

    public double length(){
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize(){
        return new Vector(this.xyz.reduce(length()));
    }
}

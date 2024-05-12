package primitives;

public class Point {
    protected final Double3 xyz;

    public static Point ZERO=new Point(0d,0d,0d);

    public Point(Double3 d3) {
       xyz=d3;
    }

    public Point(Double d1,Double d2,Double d3) {
        xyz=new Double3(d1,d2,d3);
    }

    public Vector subtract(Point other){
        return new Vector(this.xyz.subtract(other.xyz));
    }

    public Point Add(Vector vec){
        return new Point(this.xyz.add(vec.xyz));
    }

    public double distanceSquared(Point p){
        return (xyz.d1-p.xyz.d1)*(xyz.d1-p.xyz.d1)+(xyz.d2-p.xyz.d2)*(xyz.d2-p.xyz.d2)+(xyz.d3-p.xyz.d3)*(xyz.d3-p.xyz.d3);
    }

    public double distance(Point p){
        return Math.sqrt(this.distanceSquared(p));
    }




    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        return obj instanceof Point other && xyz.equals(other.xyz);
    }

    @Override
    public String toString() {return ""+xyz;}
}

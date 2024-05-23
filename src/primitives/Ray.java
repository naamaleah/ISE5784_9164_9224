package primitives;

/**
 * This class represents ray
 */
/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */
public class Ray {
  /** Source point */
  private final Point head;
  /** Direction vector*/
  private final Vector direction;

  @Override
    public String toString() {
      return "head:"+head+"direction:"+direction;
  }
  @Override
  public boolean equals(Object obj) {
        if(this == obj)return true;
        return obj instanceof Ray other && head.equals(other.head)&&direction.equals(other.direction);
  }

  /**A parameter constructor accepts a point and a vector
   *
   * @param p first parameter Point
   * @param v second parameter Vector
   */
  public Ray(Point p,Vector v){
    this.head=p;
    this.direction=v.normalize();
  }

  /**
   * getter for head
   * @return head
   */
  public Point getHead() {
    return head;
  }

  /**
   * getter for direction
   * @return direction
   */
  public Vector getDirection() {
    return direction;
  }
}

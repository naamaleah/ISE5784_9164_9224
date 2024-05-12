package primitives;

/**
 * This class represents ray
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
   * @param p
   * @param v
   */
  public Ray(Point p,Vector v){
    this.head=p;
    this.direction=v.normalize();
  }

}

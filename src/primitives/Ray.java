package primitives;

public class Ray {
  private Point head;
  private Vectore direction;

  @Override
    public String toString() {
      return "head:"+head+"direction:"+direction;
  }
  @Override
  public boolean equals(Object obj) {
        if(this == obj)return true;
        return obj instanceof Ray other && head.equals(other.head)&&direction.equals(other.direction);
  }
}

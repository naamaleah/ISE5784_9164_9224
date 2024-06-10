package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {

   public  String name;
   public Color background;
    public AmbientLight ambientLight=AmbientLight.NONE;
    public Geometries geometries=new Geometries ();

}

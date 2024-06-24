package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {

    private final Color intensity;
    public static AmbientLight NONE= new AmbientLight(Color.BLACK,0);
    public AmbientLight(Color Ia, Double3 Ka) {
        intensity=Ia.scale(Ka);

    }

    public AmbientLight(Color Ia, double Ka) {
        intensity=Ia.scale(Ka);
    }

   public Color getIntensity()
   {
       return intensity;
   }
}

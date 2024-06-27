package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient Light as color for all object in the scene
 *
 * @author Naama and Yeela
 */
public class AmbientLight {

    private final Color intensity;
    /**
     * default color for all object in the scene
     */
    public static AmbientLight NONE = new AmbientLight(Color.BLACK, 0);

    /**
     * param constructor
     *
     * @param Ia light intensity
     * @param Ka attenuation coefficient of color
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        intensity = Ia.scale(Ka);

    }

    /**
     * param constructor
     *
     * @param Ia light intensity
     * @param Ka attenuation coefficient of color
     */
    public AmbientLight(Color Ia, double Ka) {
        intensity = Ia.scale(Ka);
    }

    /**
     * getter
     *
     * @return color intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}

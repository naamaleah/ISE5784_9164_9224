package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient Light as color for all object in the scene
 *
 * @author Naama and Yeela
 */
public class AmbientLight extends Light {

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
        super(Ia.scale(Ka));
    }

    /**
     * param constructor
     *
     * @param Ia light intensity
     * @param Ka attenuation coefficient of color
     */
    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(Ka));
    }

}

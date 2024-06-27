package lighting;

import primitives.Color;

/**
 * Light object - abstract class
 *
 * @author Naama and Yeela
 */
abstract class Light {
    /**
     * intensity of the light
     */
    protected Color intensity;

    /**
     * constructor
     *
     * @param intensity {@link Color} of intensity of light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * getter for intensity
     *
     * @return intensity of light
     */
    public Color getIntensity() {
        return intensity;
    }
}

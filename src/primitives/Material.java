package primitives;

/**
 * material type of geometric geometry in a 3D space
 *
 * @author Naama and Yeela
 */
public class Material {
    /**
     * coefficient for transparency level of material
     */
    public Double3 kT = Double3.ZERO;

    /**
     * coefficient for reflectiveness level of material
     */
    public Double3 kR = Double3.ZERO;
    /**
     * attenuation coefficient for diffusion of light on the material
     */
    public Double3 kD = Double3.ZERO;
    /**
     * attenuation coefficient for specular level of the material
     */
    public Double3 kS = Double3.ZERO;
    /**
     * Glossiness factor
     * */
    public double kG = 0;
    /**
     * Blurriness factor
     * */
    public double kB = 0;

    /**
     * shininess level of geometry
     */
    public int nShininess = 0;

    /**
     * setter for kD field (Builder pattern style)
     *
     * @param kD coefficient value of diffusion of light on material
     * @return this instance of object
     */
    public Material setkD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * setter for kD field (Builder pattern style)
     *
     * @param kD coefficient value of diffusion of light on material
     * @return this instance of object
     */
    public Material setkD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * setter for kS field (Builder pattern style)
     *
     * @param kS coefficient value of specular level of material
     * @return this instance of object
     */
    public Material setkS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * setter for kS field (Builder pattern style)
     *
     * @param kS coefficient value of specular level of material
     * @return this instance of object
     */
    public Material setkS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * setter for nShininess field (Builder pattern style)
     *
     * @param nShininess shininess level of geometry
     * @return this instance of object
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * setter
     *
     * @param kT for setter
     * @return this
     */
    public Material setkT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * setter
     *
     * @param kT for setter
     * @return this
     */
    public Material setkT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * setter
     *
     * @param kR for setter
     * @return this
     */
    public Material setkR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * setter
     *
     * @param kR for setter
     * @return this
     */
    public Material setkR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets Glossy attenuation factor.
     *
     * @param kG the Glossy attenuation factor.
     * @return the material
     */
    public Material setkG(double kG) {
        this.kG = kG;
        return this;
    }

    /**
     * Sets Blur attenuation factor.
     *
     * @param kB the Blur attenuation factor.
     * @return the material
     */
    public Material setkB(double kB) {
        this.kB = kB;
        return this;
    }


}

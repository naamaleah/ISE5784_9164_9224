package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * The class represents a graphical scene
 *
 * @author Naama and Yeela
 */
public class Scene {

    /**
     * name of scene
     */
    public String name;
    /**
     * background color of scene
     */
    public Color background = Color.BLACK;
    /**
     * basic ambient lighting of scene
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * collection of graphical geometric shapes in the scene
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructs a Scene with the given name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the name of the scene.
     *
     * @param name the name to set
     * @return the updated Scene instance
     */
    public Scene setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color to set
     * @return the updated Scene instance
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light to set
     * @return the updated Scene instance
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the geometries to set
     * @return the updated Scene instance
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}

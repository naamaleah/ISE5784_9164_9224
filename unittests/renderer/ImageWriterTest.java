package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {



    @Test
    void testWriteToImage() {
        ImageWriter imageWriter= new ImageWriter("yellow",800,500);
        for (int i = 0; i < imageWriter.getNx(); i++)
            for (int j = 0; j < imageWriter.getNy(); j++) {
                imageWriter.writePixel(i,j,new Color(java.awt.Color.YELLOW));

            }
        imageWriter.writeToImage();


    }

    @Test
    void testWritePixel() {
    }
}
package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static java.awt.Color.*;


class ImageWriterTest {


    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter("yellowRedGrid", 800, 500);
        // Iterate over each pixel row
        for (int i = 0; i < imageWriter.getNx(); i++)
            // Iterate over each pixel column
            for (int j = 0; j < imageWriter.getNy(); j++) {
                // Check if the current pixel is on a grid line
                if (i % 50 == 0 || j % 50 == 0)
                    imageWriter.writePixel(i, j, new Color(RED));
                else
                    imageWriter.writePixel(i, j, new Color(YELLOW));

            }
        imageWriter.writeToImage();


    }

    @Test
    void testWritePixel() {
    }
}
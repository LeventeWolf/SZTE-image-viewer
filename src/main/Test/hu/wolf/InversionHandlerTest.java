package hu.wolf;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class InversionHandlerTest {
    //Saving worsen the original image resolution
    private final static double ERROR_LIMIT = 0.1;

    @Test
    public void testImageInversion() throws IOException {
        File file = new File("src/main/Test/testImgs/invertedCatto.jpg");

        BufferedImage picture = ImageIO.read(file);
        Image expected = SwingFXUtils.toFXImage(picture, null);


        File file2 = new File("src/main/Test/testImgs/catto.jpg");
        BufferedImage picture2 = ImageIO.read(file2);
        Image result = InversionHandler.invertImage(SwingFXUtils.toFXImage(picture2, null));

        assertTrue(isEveryPixelTheSame(expected, result));
    }

    /**
     * This method checks that if all the pixels
     * in the two images are the same within the error limit
     * @param expectedImage the expected inverted image
     * @param resultImage the inverted image we got from our invertImage() method
     * @return true if every pixel is the same else false
     */
    private boolean isEveryPixelTheSame(Image expectedImage, Image resultImage) {
        if (expectedImage == null || resultImage == null) {
            return false;
        }

        int imageWidth = (int) expectedImage.getWidth();
        int imageHeight = (int) expectedImage.getHeight();

        PixelReader resultReader = resultImage.getPixelReader();
        PixelReader expectedReader = expectedImage.getPixelReader();

        Color expectedColor;
        Color resultColor;

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                expectedColor = expectedReader.getColor(x, y);
                resultColor = resultReader.getColor(x, y);

                double expectedR = expectedColor.getRed();
                double expectedG = expectedColor.getGreen();
                double expectedB = expectedColor.getBlue();

                double resultR = resultColor.getRed();
                double resultG = resultColor.getGreen();
                double resultB = resultColor.getBlue();

                if (Math.abs(expectedR - resultR) > ERROR_LIMIT || Math.abs(expectedG - resultG) > ERROR_LIMIT || Math.abs(expectedB - resultB) > ERROR_LIMIT) {
                    System.err.printf("At pixel x: %d y: %d\n The two colors dont match\n", x, y);

                    System.err.println("expected:");
                    System.err.printf("R: %f G: %f B: %f\n", expectedR, expectedB, expectedG);

                    System.err.println("resulted:");
                    System.err.printf("R: %f G: %f B: %f\n", resultR, resultB, resultG);
                    return false;
                }
            }
        }

        return true;
    }
}
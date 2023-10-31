/**
 * Basic Tests for CS1501 Project 5
 *
 * @author Dr. Farnan
 * @author Brian T. Nixon
 * @author Marcelo d'Almeida
 */
package cs1501_p5;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

class AllTest {
    final int DEFAULT_TIMEOUT = 10;
    DistanceMetric_Inter dm;
    ColorMapGenerator_Inter generator;
    ColorQuantizer cq;

    Pixel[][] genStripedArr() {
        return new Pixel[][]{
                {new Pixel(5, 5, 5), new Pixel(5, 5, 5), new Pixel(5, 5, 5)},
                {new Pixel(50, 50, 50), new Pixel(50, 50, 50), new Pixel(50, 50, 50)},
                {new Pixel(100, 100, 100), new Pixel(100, 100, 100), new Pixel(100, 100, 100)},
                {new Pixel(150, 150, 150), new Pixel(150, 150, 150), new Pixel(150, 150, 150)},
                {new Pixel(200, 200, 200), new Pixel(200, 200, 200), new Pixel(200, 200, 200)},
                {new Pixel(250, 250, 250), new Pixel(250, 250, 250), new Pixel(250, 250, 250)}
        };
    }

    private int pixelToInt(Pixel pix) {
        return ((pix.getRed() << 16) & 0xff0000) | ((pix.getGreen() << 8) & 0xff00) | ((pix.getBlue() & 0xff));
    }

    @Test
    @DisplayName("Basic Squared Euclidean")
    void basic_euclidean_test() {
        Pixel p1 = new Pixel(10, 15, 20);
        Pixel p2 = new Pixel(20, 25, 30);
        dm = new SquaredEuclideanMetric();

        assertEquals(300, dm.colorDistance(p1, p2), "Incorrect distance from squared Euclidean");
        assertEquals(300, dm.colorDistance(p2, p1), "Flipping the order of pixels should not impact the result of squared Euclidean");
    }

    @Test
    @DisplayName("Basic Cosine Distance")
    void basic_cosine_test() {
        Pixel p1 = new Pixel(1, 255, 1);
        Pixel p2 = new Pixel(255, 1, 255);
        dm = new CosineDistanceMetric();

        // use BigDecimal for rounding returned result
        double result = dm.colorDistance(p1, p2);
        BigDecimal bd = new BigDecimal(Double.toString(result));
        bd = bd.setScale(5, RoundingMode.HALF_UP);
        assertEquals(0.99168, bd.doubleValue(), "Incorrect distance from cosine distance");

        result = dm.colorDistance(p2, p1);
        bd = new BigDecimal(Double.toString(result));
        bd = bd.setScale(5, RoundingMode.HALF_UP);
        assertEquals(0.99168, bd.doubleValue(), "Flipping the order of pixels should not impact the result of cosine distance");
    }

    @Test
    @DisplayName("Basic Bucketing generateColorPalette")
    void basic_bucketing_palette_test() {
        Pixel[][] stripedArr = genStripedArr();
        generator = new BucketingMapGenerator();

        // check 1 color
        Pixel[] result = generator.generateColorPalette(stripedArr, 1);
        Pixel expected = new Pixel(128, 0, 0);

        assertEquals(1, result.length, "Incorrect number of colors returned from Basic Bucketing generateColorPalette");
        assertEquals(expected, result[0], "Incorrect color returned for palette of a single color");

        // Check with 4 colors that evenly divide 2^24
        result = generator.generateColorPalette(stripedArr, 4);
        Pixel[] expectedCT = new Pixel[]{new Pixel(32, 0, 0), new Pixel(96, 0, 0), new Pixel(160, 0, 0), new Pixel(224, 0, 0)};

        assertEquals(4, result.length, "Incorrect number of colors returned from Basic Bucketing generateColorPalette");
        for (int i = 0; i < expectedCT.length; i++) {
            assertEquals(expectedCT[i], result[i], "Incorrect color returned for palette of bucketing");
        }

        // Check with 7 colors that do not evenly divide 2^24
        result = generator.generateColorPalette(stripedArr, 7);
        expectedCT = new Pixel[]{
            new Pixel(18, 73, 36),
            new Pixel(54, 219, 109),
            new Pixel(91, 109, 182),
            new Pixel(128, 0, 0),
            new Pixel(164, 146, 73),
            new Pixel(201, 36, 146),
            new Pixel(237, 182, 219)
        };

        assertEquals(7, result.length, "Incorrect number of colors returned from Basic Bucketing generateColorPalette");
        for (int i = 0; i < expectedCT.length; i++) {
            assertEquals(expectedCT[i], result[i], "Incorrect color returned for palette of Bucketing");
        }
    }

    @Test
    @DisplayName("Basic quantizeTo2DArray")
    void basic_quantize_2d_array() {
        Pixel[][] stripedArr = genStripedArr();
        dm = new SquaredEuclideanMetric();
        generator = new ClusteringMapGenerator(dm);
        cq = new ColorQuantizer(stripedArr, generator);

        // Check for 1 color
        Pixel[][] result = cq.quantizeTo2DArray(1);
        Pixel single_expected = new Pixel(125, 125, 125);

        assertEquals(stripedArr.length, result.length, "Incorrect number of rows in quantized pixels");
        assertEquals(stripedArr[0].length, result[0].length, "Incorrect number of columns in quantized pixels");
        for (int row = 0; row < stripedArr.length; row++) {
            for (int col = 0; col < stripedArr[0].length; col++) {
                assertEquals(single_expected, result[row][col], "Incorrectly quantized pixel");
            }
        }

        // Check for 4 colors
        result = cq.quantizeTo2DArray(4);
        Pixel[] expectedMappings = new Pixel[]{
			new Pixel(27, 27, 27),
			new Pixel(125, 125, 125),
			new Pixel(200, 200, 200),
			new Pixel(250, 250, 250)
		};

		int expected = 0;
        for (int row = 0; row < stripedArr.length; row++) {
            for (int col = 0; col < stripedArr[0].length; col++) {
				switch (row) {
					case 0:
					case 1:
						expected = 0;
						break;
					case 2:
					case 3:
						expected = 1;
						break;
					case 4:
						expected = 2;
						break;
					default:
						expected = 3;
				}
                assertEquals(expectedMappings[expected], result[row][col], "A pixel was mapped to the incorrect reduced color in Clustering");
            }
        }
    }
}


/**
 * A driver for CS1501 Project 5
 * @author	Dr. Farnan
 */

package cs1501_p5;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Color;

public class App {
    public static void main(String[] args) {

        try
        {
            // Load bitmap image
            BufferedImage image = ImageIO.read(new File("build/resources/test/test.bmp"));

            // Create pixel matrix
            Pixel[][] pixelMatrix = convertBitmapToPixelMatrix(image);
            savePixelMatrixToFile("build/resources/test/pixel_matrix.txt", pixelMatrix);
            //Quantize it
            ColorQuantizer quantizer = new ColorQuantizer(pixelMatrix, new ClusteringMapGenerator(new SquaredEuclideanMetric()));
            pixelMatrix = Quantize(4, quantizer);
            // Save pixel matrix to file
            savePixelMatrixToFile("build/resources/main/pixel_matrix.txt", pixelMatrix);
            savePixelMatrixToBitmap("build/resources/main/pixel_bitmap.bmp", pixelMatrix);
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public static Pixel[][] Quantize(int numColors, ColorQuantizer quantizer)
    {
        return quantizer.quantizeTo2DArray(numColors);
    }

    public static void savePixelMatrixToBitmap(String filePath, Pixel[][] pixelMatrix) {
        int width = pixelMatrix.length;
        int height = pixelMatrix[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Pixel pixel;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                pixel = pixelMatrix[x][y];
                Color color = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                image.setRGB(x, y, color.getRGB());
            }
        }
        try
        {
            File file = new File(filePath);
            ImageIO.write(image, "bmp", file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Pixel[][] convertBitmapToPixelMatrix(BufferedImage image) {
        Pixel[][] pixelMatrix = new Pixel[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                pixelMatrix[x][y] = new Pixel(red, green, blue);
            }
        }

        return pixelMatrix;
    }

    public static void savePixelMatrixToFile(String filePath, Pixel[][] matrix) {

        try {
            // Open file for writing
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // Write matrix to file
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(matrix[i][j] + String.valueOf('\t'));
                }
                writer.newLine();
            }

            // Close file
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

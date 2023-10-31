package cs1501_p5;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.Color;

public class ColorQuantizer implements ColorQuantizer_Inter
{
    private ColorMapGenerator_Inter colorMapGenerator;
    private Pixel[][] pixelArr;

    public ColorQuantizer(Pixel[][] pixelArr, ColorMapGenerator_Inter colorMapGenerator)
    {
        this.pixelArr = pixelArr;
        this.colorMapGenerator = colorMapGenerator;
    }

    public ColorQuantizer(String filename, ColorMapGenerator_Inter colorMapGenerator)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("build/resources/main/image.bmp"));
            Pixel[][] pixelMatrix = convertBitmapToPixelMatrix(image);
            this.pixelArr = pixelMatrix;
            this.colorMapGenerator = colorMapGenerator;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Pixel[][] quantizeTo2DArray(int numColors)
    {
        Map<Pixel, Pixel> colorMap = this.colorMapGenerator.generateColorMap(pixelArr, this.colorMapGenerator.generateColorPalette(pixelArr, numColors));
        Pixel[][] quantizeTo2DArray = new Pixel[pixelArr.length][pixelArr[0].length];
        for(int i = 0; i < pixelArr.length; i++) for(int u = 0; u < pixelArr[i].length; u++)
        {
            Pixel originPixel = pixelArr[i][u];
            Pixel afterPixel = colorMap.get(originPixel);
            quantizeTo2DArray[i][u] = afterPixel;
        }
        return quantizeTo2DArray;
    }

    public void quantizeToBMP(String fileName, int numColors)
    {
        Pixel[][] pixels = quantizeTo2DArray(numColors);
        savePixelMatrixToBitmap(fileName, pixels);
    }

    public static Pixel[][] convertBitmapToPixelMatrix(BufferedImage image)
    {
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

    public static void savePixelMatrixToBitmap(String filePath, Pixel[][] pixelMatrix)
    {
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
}
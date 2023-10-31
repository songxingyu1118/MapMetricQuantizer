package cs1501_p5;

import java.util.HashMap;

public class BucketingMapGenerator implements ColorMapGenerator_Inter
{
    private final int MAX_VALUE = 16777216;

    private int PixelToNum(Pixel p)
    {
        int R = (p.getRed() & 0xFF) << 16;
        int G = (p.getGreen() & 0xFF) << 8;
        int B = (p.getBlue() & 0xFF);
        return (R + G + B);
    }

    private Pixel NumToPixel(int n)
    {
        int R = (n >> 16) & 0xFF;
        int G = (n >> 8) & 0xFF;
        int B = n & 0xFF;
        return new Pixel(R, G, B);
    }

    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors)
    {
        Pixel[] ColorPalette = new Pixel[numColors];
        int gap = MAX_VALUE / numColors;
        int first = gap / 2;
        if(ColorPalette.length >= 1) ColorPalette[0] = NumToPixel(first);
        for(int i = 1; i < ColorPalette.length; i++)
        {
            first += gap;
            if(Math.abs(first - 8388608) <= 1) first = 8388608;
            ColorPalette[i] = NumToPixel(first);
        }
        return ColorPalette;
    }

    public HashMap<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette)
    {
        HashMap<Pixel, Pixel> generateColorMap = new HashMap<Pixel, Pixel>();
        int[] NumberPixels = new int[initialColorPalette.length];
        for(int i = 0; i < NumberPixels.length; i++) NumberPixels[i] = PixelToNum(initialColorPalette[i]);
        for(int i = 0; i < pixelArr.length; i++) for(int u = 0; u < pixelArr[i].length; u++)
        {
            Pixel currPixel = pixelArr[i][u];
            if(!generateColorMap.containsKey(currPixel))
            {
                int pixelNumber = PixelToNum(currPixel);
                int distance = Integer.MAX_VALUE;
                int matchTo = 0;
                for(int p = 0; p < NumberPixels.length; p++)
                {
                    int currDistance = Math.abs(pixelNumber - NumberPixels[p]);
                    if(currDistance < distance)
                    {
                        distance = currDistance;
                        matchTo = p;
                    }
                }
                generateColorMap.put(currPixel, initialColorPalette[matchTo]);
            }
        }
        return generateColorMap;
    }
}
package cs1501_p5;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class ClusteringMapGenerator implements ColorMapGenerator_Inter
{
    private DistanceMetric_Inter metric;

    public ClusteringMapGenerator(DistanceMetric_Inter metric) {this.metric = metric;}

    private int PixelToNum(Pixel p)
    {
        if(p == null) return 0;
        int R = (p.getRed() & 0xFF) << 16;
        int G = (p.getGreen() & 0xFF) << 8;
        int B = (p.getBlue() & 0xFF);
        return (R + G + B);
    }

    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors)
    {
        ArrayList<Pixel> Colors = new ArrayList<Pixel>();
        HashMap<Pixel, Double> colorToDistance = new HashMap<Pixel, Double>();
        for(int i = 0; i < pixelArr.length; i++) for(int u = 0; u < pixelArr[i].length; u++)
        {
            Pixel c = pixelArr[i][u];
            if(!Colors.contains(c)) Colors.add(c);
        }
        Pixel[] generateColorPalette = new Pixel[numColors];
        generateColorPalette[0] = pixelArr[0][0];
        for(int i = 1; i < generateColorPalette.length; i++)
        {
            for(Pixel color : Colors)
            {
                double nearestDistance = Double.MAX_VALUE;
                for(int u = 0; u < i; u++)
                {
                    double colorDistance = metric.colorDistance(color, generateColorPalette[u]);
                    if(colorDistance < nearestDistance) nearestDistance = colorDistance;
                }
                colorToDistance.put(color, nearestDistance);
            }
            double farthestDistance = 0;
            Pixel farthestPixel = null;
            for(Map.Entry<Pixel, Double> entry : colorToDistance.entrySet())
            {
                Pixel color = entry.getKey();
                double distance = entry.getValue();
                if(distance > farthestDistance)
                {
                    farthestDistance = distance;
                    farthestPixel = color;
                }
                else if((distance == farthestDistance) && (PixelToNum(color) > PixelToNum(farthestPixel)))
                {
                    farthestDistance = distance;
                    farthestPixel = color;
                }
            }
            generateColorPalette[i] = farthestPixel;
            colorToDistance.clear();
        }
        return generateColorPalette;
    }

    public HashMap<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette)
    {
        ArrayList<Pixel> Colors = new ArrayList<Pixel>();
        for(int i = 0; i < pixelArr.length; i++) for(int u = 0; u < pixelArr[i].length; u++) Colors.add(pixelArr[i][u]);
        ArrayList<Pixel>[] clusters = new ArrayList[initialColorPalette.length];
        Pixel[] finalColorPalette = new Pixel[initialColorPalette.length];
        for(int i = 0; i < initialColorPalette.length; i++)
        {
            finalColorPalette[i] = initialColorPalette[i];
            clusters[i] = new ArrayList<Pixel>();
        }
        for(Pixel color : Colors)
        {
            double distance = Double.MAX_VALUE;
            int selectToCluster = 0;
            for(int i = 0; i < finalColorPalette.length; i++)
            {
                double colorDistance = metric.colorDistance(color, finalColorPalette[i]);
                if(colorDistance < distance)
                {
                    distance = colorDistance;
                    selectToCluster = i;
                }
            }
            clusters[selectToCluster].add(color);
        }
        boolean stop = false;
        while(!stop)
        {
            for(int i = 0; i < finalColorPalette.length; i++)
            {
                ArrayList<Pixel> cluster = clusters[i];
                int count = 0;
                int Rmean = 0;
                int Gmean = 0;
                int Bmean = 0;
                for(Pixel example : cluster)
                {
                    count++;
                    Rmean += example.getRed();
                    Gmean += example.getGreen();
                    Bmean += example.getBlue();
                }
                if(count > 0)
                {
                    Rmean /= count;
                    Gmean /= count;
                    Bmean /= count;
                    finalColorPalette[i] = new Pixel(Rmean, Gmean, Bmean);
                }
            }
            boolean shouldStop = true;
            for(int i = 0; i < finalColorPalette.length; i++)
            {
                ArrayList<Pixel> cluster = clusters[i];
                ArrayList<Pixel> moveToNewCluster = new ArrayList<Pixel>();
                for(Pixel example : cluster)
                {
                    double distance = Double.MAX_VALUE;
                    int moveToCluster = 0;
                    for(int u = 0; u < finalColorPalette.length; u++)
                    {
                        double colorDistance = metric.colorDistance(example, finalColorPalette[u]);
                        if(colorDistance < distance)
                        {
                            distance = colorDistance;
                            moveToCluster = u;
                        }
                    }
                    if(moveToCluster != i)
                    {
                        clusters[moveToCluster].add(example);
                        moveToNewCluster.add(example);
                    }
                }
                for(Pixel getOut : moveToNewCluster) cluster.remove(getOut);
                if(moveToNewCluster.size() >= 1) shouldStop = false;
            }
            if(shouldStop) stop = true;
        }
        HashMap<Pixel, Pixel> generateColorMap = new HashMap<Pixel, Pixel>();
        for(int i = 0; i < finalColorPalette.length; i++)
        {
            ArrayList<Pixel> cluster = clusters[i];
            for(Pixel example : cluster) generateColorMap.put(example, finalColorPalette[i]);
        }
        return generateColorMap;
    }
}
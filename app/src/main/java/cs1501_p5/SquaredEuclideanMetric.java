package cs1501_p5;

import java.math.*;

public class SquaredEuclideanMetric implements DistanceMetric_Inter
{
    public double colorDistance(Pixel p1, Pixel p2)
    {
        double colorDistance = 0.0;
        for(int i = 0; i < 3; i++)
        {
            switch(i)
            {
                case 0:
                colorDistance += Math.pow((p1.getRed() - p2.getRed()), 2);
                break;
                case 1:
                colorDistance += Math.pow((p1.getGreen() - p2.getGreen()), 2);
                break;
                case 2:
                colorDistance += Math.pow((p1.getBlue() - p2.getBlue()), 2);
                break;
            }
        }
        return colorDistance;
    }
}
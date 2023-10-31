package cs1501_p5;

import java.math.*;

public class CosineDistanceMetric implements DistanceMetric_Inter
{
    public double colorDistance(Pixel p1, Pixel p2)
    {
        double colorDistance = 1.0;
        int R1 = p1.getRed();
        int G1 = p1.getGreen();
        int B1 = p1.getBlue();
        int R2 = p2.getRed();
        int G2 = p2.getGreen();
        int B2 = p2.getBlue();
        long NUM1 = ((R1*R2)+(G1*G2)+(B1*B2));
        double NUM2 = Math.sqrt(Math.pow(R1, 2) + Math.pow(G1, 2) + Math.pow(B1, 2));
        double NUM3 = Math.sqrt(Math.pow(R2, 2) + Math.pow(G2, 2) + Math.pow(B2, 2));
        colorDistance -= (NUM1 / (NUM2 * NUM3));
        return colorDistance;
    }
}
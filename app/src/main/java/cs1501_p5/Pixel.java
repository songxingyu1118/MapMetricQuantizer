package cs1501_p5;

public class Pixel {
    private int red;
    private int green;
    private int blue;

    public Pixel(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "(" + this.red + "," + this.green + "," + this.blue + ")";
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Pixel))  return false;
        Pixel otherPix = (Pixel) other;
        if (this.getRed() != otherPix.getRed()) return false;
        if (this.getGreen() != otherPix.getGreen()) return false;
        if (this.getBlue() != otherPix.getBlue())   return false;
        // else
        return true;
    }

    @Override
    public int hashCode() {
        return (100 * this.red) + (10 * this.green) + (this.blue);
    }
}

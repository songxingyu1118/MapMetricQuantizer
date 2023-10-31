/**
 * Color Map Generator interface for CS1501 Project 5
 * @author  Dr. Farnan
 * @author  Brian T. Nixon
 * @author  Marcelo d'Almeida
 */
package cs1501_p5;

import java.util.Map;

interface ColorMapGenerator_Inter {
	/**
	 * Produces an initial palette. This initial palette will be
	 * the centers of the buckets and the starting centroids of clusters.
	 * 
     * @param pixelArr the 2D Pixel array that represents the bitmap image
     * @param numColors the number of desired colors in the palette
     * @return A Pixel array containing numColors elements
	 */
	public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors);

    /**
     * Computes the reduced color map. For bucketing, this will map each color
	 * to the center of its bucket, for clustering, this maps examples to final
	 * centroids.
     *
     * @param pixelArr the pixels array that represents the bitmap image
	 * @param initialColorPalette an array of Pixels generated by generateColorPalette
     * @return A Map that maps each distinct color in pixelArr to a final color 
     */
    public Map<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette);
}

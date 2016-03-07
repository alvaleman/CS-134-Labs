import squint.*;

/*
 * A class to compute a histogram of an image.
 * 
 * Alvaro Aleman
 * 11/5/2014
 */
public class Histogram {

    // range of brightness values
    private final int MAX = 255;
    private final int MIN = 0;
    
    //array filled with frequency of each brightness
    private int[]number = new int [256];

    // Create a Histogram for the specified color layer of an image
    public Histogram(SImage image, int layer) {
        //counts occurrences of brightness values 
        int width = image.getWidth();
        int height = image.getHeight();
        int[][]input = image.getPixelArray(layer);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                number[input[x][y]]++;
            }

        }
        
    }

    // returns frequency value for a given shade 
    public int frequency(int shade) {
        if ( MIN <= shade && shade <= MAX ) {
            // returns the count of pixels whose brightness equaled "shade".             
            return number[shade];
        } else {
            return -1;
        }
    }

}
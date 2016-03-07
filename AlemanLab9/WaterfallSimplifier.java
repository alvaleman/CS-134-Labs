
import squint.*;
import javax.swing.*;

/*
 * Class WaterfallSimplifier - Compresses the histogram of brightness levels used to 
 *                             represent an image by replacing every brightness value 
 *                             with the difference between its original value and the 
 *                             original value of the pixel directly above it
 *
 * Alvaro Aleman
 * 
 * 11/11/14
 */
public class WaterfallSimplifier extends ImageSimplifier{

    //returns a desccription of the type of simplification this class performs
    public String toString(){
        return "Waterfall";
    }

    //return an array of the same size as the parameteter array in which the 
    //values are the difference between the original value and the original value
    //of the pixel directly above it 
    public int[][] encodePixels( int [][] shades) {
        
        //set dimensions of result equal to original image array
        int[][] result = new int[shades.length][shades[0].length];

        for (int x = 0; x < shades.length; x++) {
            //top row remains unchanged
            result[x][0] = shades[x][0];

            for (int y = 1; y < shades[0].length; y++) {
                //subtracts original value with pixel directly above it
                result[x][y] = shades[x][y] - shades[x][(y - 1)];

            }

        }

        return result;
    }

    //Reverses Waterfall process on shades 
    public int[][] decodePixels( int [][] shades){
    
        for (int x = 0; x < shades.length; x++){
      

            for (int y = 1; y < shades[0].length; y++) {
                //adds value of pixel directly above current pixel to pixel value
                shades[x][y] += shades[x][(y - 1)];

            }

        }
        
        return shades;
    }
}

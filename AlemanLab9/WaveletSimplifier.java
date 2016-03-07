/* WaveletSimplifier --- A WaveletSimplifier compresses the histogram of brightness levels
 *                       used to represent an image by replacing the left half of the image
 *                       with the average values of horizontally adjacent pixels and the left
 *                       half with the differences between the same pairs of pixels.
 *                       
 * Alvaro Aleman
 * 11/11/2014
 */
public class WaveletSimplifier extends ImageSimplifier {

    // Returns a description of the type of simplification this class performs
    public String toString() {
        return "Wavelet";
    }

    // Return an array of the same size as the parameter array in which the
    // values stored on the left half of the array are the averages of horizontally
    // adjacent pixels from the parameter array and the values stored on the right
    // half are half the differences between the same pairs of values.
    public int [][] encodePixels( int [][] shades ) {  
        //set dimensions of result equal to original image array
        int [][] result = new int[shades.length][shades[0].length];
        //Variable used to determine halfway point on width
        int halfWidth = (shades.length + 1)/2;

        for ( int y = 0; y < shades[0].length ; y++ ) {
            
            for ( int x = 0; x < shades.length/2; x++ ) {
                //changes the pixels on the left half to be averages of horizontal adjacent pixels
                result[x][y] = (shades[2*x][y] + shades[2*x+1][y] )/2;
                
                //changes the pixels on the right half to be the half of the difference of adjacent 
                //horizontal pixels
                result[x + halfWidth][y] = (shades[2*x][y] - shades[2*x+1][y])/2;
            }
            
        }

        // If the image width was odd, place the rightmost column of the parameter array
        // in the center of the array returned.
        if ( shades.length % 2 == 1 ) {
            
            for ( int y = 0; y < shades[0].length; y++ ) {
                result[halfWidth - 1][y] = shades[shades.length-1][y];
            }
            
        }

        return result;
    }

    //Reverses Wavelet provess on shades 
    public int [][] decodePixels( int [][] shades ) {  
        //set dimensions of result equal to original image array
        int halfWidth = (shades.length + 1) / 2;
        //Variable used to determine halfway point on width
        int[][] result = new int[shades.length][shades[0].length];
       
        //loop that reverses wavelet process
        for (int y = 0; y < shades[0].length; y++) {

            for (int x = 0; x < shades.length / 2; x++)

            {
                //restores even numbered columns
                result[(2*x)][y] = (shades[x][y] + shades[(x + halfWidth)][y]);
                //restores odd numbered columns
                result[(2*x+1)][y] = (shades[x][y] - shades[(x + halfWidth)][y]);

            }

        }
        
        //moves middle column back to the end if image array is odd
        if (shades.length % 2 == 1) {

            for (int y = 0; y < shades[0].length; y++) {

                result[(shades.length - 1)][y] = shades[(halfWidth - 1)][y];

            }

        }

        return result;

    }

}
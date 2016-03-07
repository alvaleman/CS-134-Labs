/* RangeSimplifier --- A RangeSimplier transforms an image by reducing the number of
 *                     brightness levels used to represent the pixels of the image.
 *                     Performing both the encode and decode operaitons implemented
 *                     by this class is equivalent to performing the "quantize"
 *                     operation implmeneted in lab 7.
 */

public class RangeSimplifier extends ImageSimplifier {

    // The number of brightness levels to be used
    private int levels;
    
    // Just saves the number of brightness levels to use
    public RangeSimplifier( int theLevels ) {
        levels = theLevels;
    }

    // Returns a description of the type of simplification this class performs
    public String toString( ) {
        return "Range Reducer";
    }

    // Linearly scales all of the brightness  values used in a pixel array
    // so that a pixel that was 255 in the original is set to "levels".
    public int [][] encodePixels( int [][] shades ) {
        for ( int x = 0; x < shades.length; x++ ) {
            for ( int y = 0; y < shades[0].length; y++ ) {
                shades[x][y] = shades[x][y]*levels/256;
            }
        }
        return shades;
    }

    // Linearly scales all of the brightness  values used in a pixel array
    // so that a pixel that was "levels" in the original is set to 255.
    public int [][] decodePixels( int [][] shades ) {
        for ( int x = 0; x < shades.length; x++ ) {
            for ( int y = 0; y < shades[0].length; y++ ) {
                shades[x][y] = shades[x][y]*256/levels;
            }
        }
        return shades;
    }


}
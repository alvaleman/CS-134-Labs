/* RecWaveletSimplifier --- A RecWaveletSimplifier transforms and image by repeatedly
 *                          applying the wavelet simiplification process to narrower
 *                          rectangles representing horizontally compressed versions of
 *                          the original image.
 *                          
 *                          Alvaro Aleman
 *                          11/12/2014
 */
public class RecWaveletSimplifier extends ImageSimplifier {

    // A non-recursive wavelet simplifier
    private WaveletSimplifier wavelet = new WaveletSimplifier();

    // Returns a description of the type of simplification this class performs
    public String toString() {
        return "Recursive Wavelet";
    }

    //  Simplify a pixel array using the wavelet transformation
    public int [][] encodePixels( int [][] shades ) {
        if ( shades.length > 1 ) {
            // Apply the non-recursive wavelet transformation
            int [][] answer = wavelet.encodePixels( shades );
            // Apply the recursive wavelet transformation to the left half of result
            return paste( this.encodePixels( leftHalf( answer )), answer );
        } else {
            // Leave the brightess values unchanged if the image is only 1 pixel wide
            return shades;
        }
    }

    //decodes recursively
    public int [][] decodePixels( int [][] shades ) {
        
        if (shades.length > 1) {

            return this.wavelet.decodePixels(paste(decodePixels(leftHalf(shades)), shades));

        }
        
        return shades;

    }

    // This method extracts the left half of an array of brightness values
    private int [][] leftHalf( int [][] shades ) {
        int width = (shades.length + 1)/2;
        int height = shades[0].length;

        int [][] result = new int[width][height];

        for ( int x = 0; x < width; x++ ) {
            for ( int y = 0; y < height; y++ ) {
                result[x][y] = shades[x][y];
            }
        }
        return result;
    }

    // This method pastes a copy of the values in the src array into the upper
    // left corner of the dest array.
    private int [][] paste( int [][] src, int [][] dest ) {

        for ( int x = 0; x < dest.length && x < src.length; x++ ) {
            for ( int y = 0; y < dest[0].length && y < src[0].length; y++ ) {
                dest[x][y] = src[x][y];
            }
        }

        return dest;
    }

    
    
}
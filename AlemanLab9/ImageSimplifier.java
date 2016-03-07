import squint.*;

/*   ImageSimplifier --- This class is designed to serve as a superclass for other
 *                       classes that apply interesting image transformations
 *                       designed to simplify the structure of the distribution
 *                       of numeric values used to represent the original image
 *                       in ways that will improve the effectiveness of applying
 *                       Huffman coding to the image's representation while
 *                       preserving the possibility of restoring the original
 *                       image either exactly or at least to a close approximation.
 *                       
 *                       This particular class implement the trivial transformation.
 *                       The transformed image is identical to the original.
 *                       The assumption is that classes that extend this class will
 *                       define more interesting transformations.
 */
public class ImageSimplifier {

    // Nothing needs to be done to construct this trivial type of ImageSimplifier.
    public ImageSimplifier() {
        
    }
    
    // The first two methods defined, encode and decode, are the only truly "public"
    // methods of the class.  That is, they are the only methods invoked from
    // outside the class.
    //
    // Most classes that extend ImageSimplifier will inherit the versions of these
    // methods provided below rather than define new versions that override these
    // definitions.

    // The encode method takes an original image and transforms it into an alternate
    // representation designed to improve compressions.
    public SImage encode( SImage original ) {
        return new SImage( encodeLayer( original, SImage.RED ),
                           encodeLayer( original, SImage.GREEN ),
                           encodeLayer( original, SImage.BLUE )
                           );
    }
    
    // It is assumed that classes that extend ImageSimplier will define their own
    // versions of toString that returns a brief description of the simplification
    // method the class implements. Something like "Waterfall" or "Wavelet".
    // The string returned by this method will be displayed in the menu created
    // by the SimplifierDriver to let users select a simplification method to apply.
    
    // Returns a description of the type of simplification this class performs
    public String toString( ) {
        return "Trivial Simplifier";
    }

    
    // The decode method takes a transformed image and restores as close an
    // approximation as possible to the original image.
    public SImage decode( SImage original ) {
        return new SImage( decodeLayer( original, SImage.RED ),
                           decodeLayer( original, SImage.GREEN ),
                           decodeLayer( original, SImage.BLUE )
                           );
    }
    
    // The next two methods perform the desired transformation on the array of pixel
    // values describing a single color layer of an image.
    //
    // Most classes that extend ImageSimplier will define their own versions of
    // encodePixels and decodePixels that override the trivial versions defined below.
    
    
    // The encodePixels method takes a pixel array describing a single color layer from
    // an original image and transforms it into an alternate representation designed to
    // improve compressions.
    public int [][] encodePixels( int [][] shades ) {
        return shades;
    }
    
    // The decodePixels method takes a a pixel array describing a single color layer
    // from a transformed image and restores as close an approximation as possible to
    // the corresponding pixel array from the original image.
    public int [][] decodePixels( int [][] shades ) {
        return shades;
    }
    
    // The last two methods perform the desired transformation on the array of pixel
    // values describing a single color layer of an image by extracting the
    // specified layer and passing it to either encodePixels or decodePixels.
    //
    // These methods are truly private. They should not be used by other classes or
    // redefined in classes that extend ImageSimplifier.
    
    
    // The encodeLayer method transforms the specified color layer from an original
    // image into an alternate representation designed to improve compressions.
    private int [][] encodeLayer( SImage orig, int layer ) {
        return encodePixels( orig.getPixelArray( layer ) );
    }
    
    // The encodeLayer method restores as close an approximation as possible to the
    // pixel array of the specified layer of the original image.
    private int [][] decodeLayer( SImage orig, int layer ) {
        return decodePixels( orig.getPixelArray( layer ) );
    }
    
}
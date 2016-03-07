import squint.SImage;

// Create a window displaying an image representing the pixel-by-pixel differences
// between two images of identical dimensions
public class DisplayDifference extends ImageViewer {

    // Install the difference image in the ImageViewer
    public DisplayDifference( SImage im1, SImage im2 ) {
        setImage( difference( im1, im2 ) );
    }
    
    // Construct an image representing the difference between two other images
    private SImage difference( SImage im1, SImage im2 ) {
            return new SImage( layerDifference( im1, im2, SImage.RED ),
                               layerDifference( im1, im2, SImage.GREEN ),
                               layerDifference( im1, im2, SImage.BLUE )
                               );
    }
    
    // Contruct a pixel array representing the differences between the pixel
    // arrays for the specified level of the two images provided
    public int [][] layerDifference( SImage im1, SImage im2, int layer ) {
        int [][] pix1 = im1.getPixelArray( layer );
        int [][] pix2 = im2.getPixelArray( layer );
        int [][] result = 
           new int[ Math.min( pix1.length, pix2.length )][ Math.min( pix1[0].length, pix2[0].length )];
     
        for ( int x = 0; x < result.length; x++ ) {
            for ( int y = 0; y < result[0].length; y++ ) {
                pix1[x][y] = pix1[x][y] - pix2[x][y];
            }
        }
        return pix1;
    }
}
import java.awt.*;
import squint.*;
import javax.swing.*;
import java.io.File;

/*
 * SimplifierDriver - An extension of the ImageViewer class designed to provide
 *                    an interface that can measure the effectiveness of image
 *                    simplification techniques on the compression achieved using
 *                    Huffman coding.
 *                    
 *                    Alvaro Aleman 
 *                    11/12/2014
 */
public class SimplifierDriver extends ImageViewer {
        
    // A menu of ImageSimplifier the user may choose
    private JComboBox methods = new JComboBox( );
    
    // A button to apply the selected method to an image
    private JButton simplify = new JButton( "Simplify" );
    
    // Create the interface by adding a menu for selecting the  simplification
    // technique to be used and a button to apply the selected method
    public SimplifierDriver( ) {      

      // Add all of the available ImageSimplifiers to the menu
      methods.addItem( new RangeSimplifier(64) );
      methods.addItem( new WaveletSimplifier() );
      methods.addItem( new RecWaveletSimplifier() );
      methods.addItem( new WaterfallSimplifier() );
      methods.addItem( new CompositeSimplifier() );
      

      // Add the GUI controls to the window
      JPanel control = new JPanel();
         control.add( methods );
         control.add( simplify );
      contentPane.add( control, BorderLayout.SOUTH );
           
    }
    
    // When the simplify button is pressed, display new ImageViewers displaying
    // the results
    public void buttonClicked( JButton which ) {
        
        if ( which == simplify ) {
            // First compute and display the simplified image so the user can use
            // "Show Histogram" to evaluate the possible image compression
            ImageSimplifier method = (ImageSimplifier) methods.getSelectedItem();
            SImage simpler = method.encode( getOriginal() );
            ImageViewer simple = new ImageViewer( );
            simple.setImage( getOriginal() );
            simple.updateImage( simpler );
            simple.setTitle( "Simplified" );
            
            // Next, compute and display the best approximation of the orignal
            // image that can be derived from the simplified version so the user
            // can evaluate the distortion introduced by lossy compression methods.
            SImage restored = method.decode( simpler );
            ImageViewer restore = new ImageViewer( );
            restore.setImage( getOriginal() );
            restore.updateImage( restored );
            restore.setTitle( "Restored" );
            
        } else {
            
            // If some button other than "Simplify" was clicked, let the version
            // of buttonClicked defined in the ImageViewer superclass perform the
            // requested action.
            super.buttonClicked( which );
        }
    }

}




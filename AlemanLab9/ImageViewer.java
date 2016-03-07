import java.awt.*;
import squint.*;
import javax.swing.*;
import java.io.File;

/*
 * ImageViewer - A class that creates a window in which you can load and manipulate
 *               images.
 */
public class ImageViewer extends GUIManager {
    private final int WINDOW_WIDTH  = 720, WINDOW_HEIGHT = 600;
   
    // Buttons used to load images
    private JButton loadImage = new JButton( "Load Image" );
    private JButton snapShot = new JButton( "Take Selfie" );
    
    // Button used to modify or analyze imabes
    private JButton showHist = new JButton( "Show Histogram" );
    private JButton expandRange = new JButton( "Expand Range" );
    private JButton differ = new JButton( "Show Difference" );
    
    // Used to display the current image
    private JLabel displayImage = new JLabel( "", SwingConstants.CENTER );
   
    // Dialog box used to select image files
    private JFileChooser chooser = 
                      new JFileChooser( new File( System.getProperty("user.dir")) );
  
    // The most recently loaded image
    private SImage original;
    
    // The image currently displayed in the window
    private SImage updated;
    
    // Access to the computer's digital camera
    private Camera pointAndShoot = new Camera();
    
    // Install the buttons and JLabel for image display
    public ImageViewer() {
      // Create window to hold all the components
      this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
      
      contentPane.setLayout( new BorderLayout() );
      contentPane.add( new JScrollPane( displayImage ) , BorderLayout.CENTER );
      
      JPanel controlPane = new JPanel();
      controlPane.setLayout( new GridLayout( 3, 3 ) );
      controlPane.add( loadImage );
      controlPane.add( snapShot );
      controlPane.add( differ );
      controlPane.add( showHist );
      controlPane.add( expandRange );
      
      contentPane.add( controlPane, BorderLayout.NORTH );
    }
        
    // Install a new image to be manipulated
    public void setImage( SImage im ) {
        original = im;
        updateImage( im );
    }
    
    // Display a modified version of the original image
    public void updateImage( SImage newImage ) {
        updated = newImage;
        displayImage.setIcon( updated );     
        
    }
    
    // Get the image currently displayed
    public SImage getImage( ) {
        return updated;
    }
    
    // Get the original version of the image currently displayed
    public SImage getOriginal( ) {
        return original;
    }
    
    // Load or process an image when the button is clicked
    public void buttonClicked( JButton which ) {
                   
        if ( which == loadImage ) {
            // Load an image from a file
            if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {
        
                setImage( new SImage( chooser.getSelectedFile().getAbsolutePath() ) );
           }
        } else if ( which == snapShot ) {
            // Load an image from the computer's camera
            setImage( pointAndShoot.getImage() );

        } else if ( which == showHist ) {
            // Display graphs of the current image's histograms
            new DisplayHistograms( updated );
            
        } else if ( which == expandRange ) {
            // Expand the range of the current image's brightness values 
            updateImage( expand( updated ) );
            
        } else if ( which == differ ) {
            // Create a window displaying the differences between the original and 
            // updated images
            new DisplayDifference( original, updated );
        }
    }  
    
    // Expand the range of pixel brightness values used in an image
    private SImage expand( SImage pic ) {
        return new SImage( expandLayer( pic, SImage.RED ),
                           expandLayer( pic, SImage.GREEN ),
                           expandLayer( pic, SImage.BLUE )
                           );
    }
    
    // Expand the range of pixel brightness values used the specified layer of
    // an image
    private int [][] expandLayer( SImage im, int layer ) {
        int [][] shades = im.getPixelArray( layer );
        int min = getMin( shades );
        int max = getMax( shades );
        int range = max - min + 1;
        
        for ( int x = 0; x < shades.length; x++ ) {
            for ( int y = 0; y < shades[0].length; y++ ) {
                shades[x][y] = (shades[x][y]-min)*255/range;
            }
        }
        return shades;
    }
        
    // Find the largest value in a pixel array
    private int getMin( int [][] shades ) {
        int result = shades[0][0];
        
        for ( int x = 0; x < shades.length; x++ ) {
            for ( int y = 0; y < shades[0].length; y++ ) {
                if ( shades[x][y] < result ) {
                    result = shades[x][y];
                }
            }
        }
        return result;
                
    }
    
    // Find the smallest value in a pixel array
    private int getMax( int [][] shades ) {
        int result = shades[0][0];
        
        for ( int x = 0; x < shades.length; x++ ) {
            for ( int y = 0; y < shades[0].length; y++ ) {
                if ( shades[x][y] > result ) {
                    result = shades[x][y];
                }
            }
        }
        return result;
                
    }
        
}
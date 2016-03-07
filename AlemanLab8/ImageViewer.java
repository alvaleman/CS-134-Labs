sscimport java.awt.*;
import squint.*;
import javax.swing.*;
import java.io.File;

/*
 * ImageViewer - Provides an interface for loading and viewing
 *               images that can be easily extended to include
 *               additional image transformations.
 *               
 *               Alvaro Aleman
 *               11/5/2014
 */
public class ImageViewer extends GUIManager {   
    // Size of window to display
    private final int WINDOW_WIDTH = 530;
    private final int WINDOW_HEIGHT = 550;

    // Buttons used to load images and apply operations to them
    private JButton loadImage = new JButton( "Load Image" );
    private JButton snapShot = new JButton( "Take Selfie" );
    private JButton showHist = new JButton( "Show Histogram" );
    private JButton zoomOut = new JButton("Zoom Out");
    private JButton zoomIn = new JButton("Zoom In");
    private JButton expandRange = new JButton("Expand Range");
    private JButton difference = new JButton("Show Difference");
    
    // File open dialog used to select image files
    private JFileChooser chooser = new JFileChooser( new File( System.getProperty("user.dir")) );

    // Used to display images
    private JLabel displayImage = new JLabel( "", SwingConstants.CENTER );

    // The current image in its original and modified states
    private SImage original;
    private SImage updated;
    
    // The computer's camera    
    private Camera pointAndShoot = new Camera();

    // Create the window and GUI controls for the program
    public ImageViewer() {
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );

        // Switch to a border layout manager
        contentPane.setLayout( new BorderLayout() );
        contentPane.add( new JScrollPane( displayImage ), BorderLayout.CENTER );

        // Create a grid full of control buttons
        JPanel controlPane = new JPanel();
        controlPane.setLayout( new GridLayout(2, 4) );
        controlPane.add( loadImage );
        controlPane.add( snapShot );
        controlPane.add( showHist );
        controlPane.add( zoomOut);
        controlPane.add( zoomIn);
        controlPane.add( expandRange);
        controlPane.add( difference);
      
        //place grid in contentPane 
        contentPane.add( controlPane, BorderLayout.NORTH );

    }

    // Set a new current image and display it
    public void setImage( SImage im ) {
        original = im;
        updateImage( im );
    }

    // Get the original image most recently placed in the viewer
    // by invoking setImage
    public SImage getOriginal( ) {
        return original;
    }

    // Update the image displayed in the viewer
    public void updateImage( SImage newImage ) {
        updated = newImage;
        displayImage.setIcon( updated );     

    }

    // Load or process an image when the button is clicked
    public void buttonClicked( JButton which ) {

        if ( which == loadImage ) {
            // Load an image file chosen by the user
            if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {

                original = new SImage( chooser.getSelectedFile().getAbsolutePath() );
                updateImage( original );
            }   

        } else if ( which == snapShot ) {

            // Load an image taken from the computer's camera
            original = pointAndShoot.getImage();
            updateImage( original );

        } else if ( which == showHist ) {
            // Display a set of histograms of the images colors
            new DisplayHistograms( updated );

        } else if( which == zoomOut) {

            //Readjust image to half the size
            if (updated != null) {

                updateImage(ZoomOut(updated));

            }
        } else if( which == zoomIn) {

            //Readjust image to twice the size
            if (updated != null) {

                updateImage(ZoomIn(updated));

            }   

        } else if(which == expandRange){
            
            //Expand brightness range to include all levels 
            if (updated != null) {

                updateImage(ExpandRange(updated));

            }   
            
        } else if(which == difference){
            
            //Display new image calculated from the difference of current and original image
            if (updated != null) {
                DisplayDifference T = new DisplayDifference(getOriginal() , updated);
               
                
            }    
        }
    } 

    // Adjust the levels to be half the size in a single layer of an image
    public int[][] adjustSize (SImage pic, int layer){
        int width = pic.getWidth();
        int height = pic.getHeight();
        int [][]input = pic.getPixelArray(layer); 
        int [][]output = new int [(width/2)+1][(height/2)+1];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){

                output[x/2][y/2] = input[x][y];
            }

        }
        return output;
    }
    
   // Adjust the levels to be half the size in all the layers of an image
    private SImage ZoomOut(SImage pic) {
        return new SImage( adjustSize( pic, SImage.RED ),
            adjustSize( pic, SImage.GREEN ),
            adjustSize( pic, SImage.BLUE )
        );
    }

    // Adjust the levels to be twice the size in a single layer of an image
    public int[][] adjustBigger (SImage pic, int layer){
        int width = pic.getWidth();
        int height = pic.getHeight();
        int [][]input = pic.getPixelArray(layer); 
        int [][]output = new int [(width*2)][(height*2)];
        for(int x = 0; x < width*2 ; x++){
            for(int y = 0; y < height*2 ; y++){

                output[x][y] = input[x/2][y/2];
            }

        }
        return output;
    }

    // Adjust the levels to be twice the size in all the layers of an image
    private SImage ZoomIn(SImage pic) {
        return new SImage( adjustBigger( pic, SImage.RED ),
            adjustBigger( pic, SImage.GREEN ),
            adjustBigger( pic, SImage.BLUE )
        );
    } 

    //determine maximum value in an image array 
    private int getMax(int [][]input){
        int max = input[0][0];
        int width = input.length;
        int height = input[0].length;
        for ( int x = 1; x < width; x++) {
            for(int y = 0; y < height; y++){

                if ( max < input[x][y]) {
                    max = input[x][y];
                }
            }
        }
        return max;
    }

    //determine minimum value in an image array
    private int getMin(int [][]input){
        
        int min = input[0][0];
        int width = input.length;
        int height = input[0].length;
        for ( int x = 1; x < width; x++) {
            for(int y = 0; y < height; y++){

                if ( min > input[x][y]) {
                    min = input[x][y];
                }
            }
        }
        return min;
    }
    
    // expands the brightness range in a single layer of an image
     public int[][] Expand (SImage pic, int layer){
        int width = pic.getWidth();
        int height = pic.getHeight();
        int [][]input = pic.getPixelArray(layer); 
        int [][]output = new int [width][height];
        int minimum = getMin(input);
        int maximum = getMax(input);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){

                output[x][y] = (input[x][y] - minimum) * 256 / (maximum-minimum+1);
            }

        }
        return output;
    }
    
    // expands the brightness range in all layers of an image
    private SImage ExpandRange(SImage pic) {
        return new SImage( Expand( pic, SImage.RED ),
            Expand( pic, SImage.GREEN ),
            Expand( pic, SImage.BLUE )
        );
    }
}  



import squint.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/*
 * Class ImageViewer - Sets up window, imports picture, and displays adjusted image
 * 
 * Alvaro Aleman
 * 
 * 10/28/2014
 */
public class ImageViewer extends GUIManager
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH = 400, WINDOW_HEIGHT = 400;

    // place your instance variable declarations here
    
    //Button used to load an image
    private JButton LoadImage;

    //Label used to displays image
    private JLabel Label;

    //Variable used to keep track of original imported image
    private SImage ImgVar;

    //Variable used to import image
    private JFileChooser chooser = 
        new JFileChooser(new File(System.getProperty("user.dir")) + "/../AllImages");

    //Creates window and adds basic buttons and labels
    public ImageViewer() {
        // Create window to hold all the components
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
        contentPane.setLayout( new BorderLayout() );
        
        //add button in north section of window
        JPanel mypane;
        mypane = new JPanel ();
        mypane.add ( LoadImage = new JButton ("Load Image"));
        contentPane.add(mypane, BorderLayout.NORTH);

        //add label in center to hold image
        contentPane.add( new JScrollPane( Label = new JLabel("", SwingConstants.CENTER) ), 
            BorderLayout.CENTER );


    }

    /*
     *  Imports picture once button is clicked
     */
    public void buttonClicked( ) {
       //if file choose returns an approved option we load image (incase user clicks cancel)
        if ( chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
            ImgVar = new SImage (chooser.getSelectedFile().getAbsolutePath());
            updateDisplay(ImgVar);
        }

    }

    //method that displays altered image
    public void updateDisplay(SImage mage) {
         Label.setIcon(mage);

    }
    
    //method used to retrieve originally imported image
    public SImage getImage(){
        return ImgVar;
    
    }
}

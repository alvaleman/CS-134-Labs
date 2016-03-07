
import squint.*;
import javax.swing.*;
import java.awt.*;

/*
 * Class AdjustLevels - Alters brightness of imported image
 * 
 * Alvaro Aleman
 * 
 * 10/28/2014
 */
public class AdjustLevels extends ImageViewer
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 400;

    //Label used to keep track of slider value
    private JLabel current;

    //Label used to show user what he or she is changing
    private JLabel range; 

    //slider used to adjust brightness value
    private JSlider slider;

    //variable used to keep track of image
    private SImage Image;
  
    

    public AdjustLevels() {
        // Create panel to hold all the components and place inside contentPane
        JPanel mypane;
        mypane = new JPanel ();
        mypane.add(range = new JLabel ("Brightness level:"));
        mypane.add(current = new JLabel(""));
        mypane.add(slider = new JSlider ( 1, 256, 125));
        contentPane.add(mypane, BorderLayout.SOUTH);
    }

    public void sliderChanged () {
        //displays current level of slider
        int currentValue = slider.getValue();
        current.setText((currentValue) + "");    
        
        //invokes adjustpixel method for each of the primary colors to change brightness of imported image
        Image = new SImage(adjustPixel(slider.getValue(), getImage(), SImage.RED) , 
                            adjustPixel(slider.getValue(), getImage(), SImage.GREEN ),
                            adjustPixel(slider.getValue(), getImage(), SImage.BLUE ));
                            
        //invokes update display method to display altered image in window                    
        updateDisplay(Image);

    }

    //alters pixel array value based on slider level 
    public int[][] adjustPixel (int brightness, SImage pixels, int layer){
        int width = pixels.getWidth();
        int height = pixels.getHeight();
        int [][]input = pixels.getPixelArray(layer); 
        int [][]output = new int [width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                
                output[x][y] = input[x][y] * brightness/ 256;
            }

        }
        return output;
    }

}
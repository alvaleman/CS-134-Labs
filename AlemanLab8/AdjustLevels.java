import java.awt.*;
import squint.*;
import javax.swing.*;

/*
 * AdjustLevels - Extension of ImageViewer that allows user to vary image 
 *             color resolution
 *             
 * Alvaro Aleman    
 *11/5/2014
 */
public class AdjustLevels extends ImageViewer {

    // Controls for the number of levels to use
    private JLabel levelsSetting = new JLabel("", SwingConstants.RIGHT);
    

    // Label placed on slider to indicate its range and current setting
    private JSlider levels = new JSlider(1, 256, 256); 

    // Add slider and label to the bottom of the window
    public AdjustLevels() {

       JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(1, 2));
        sliderPanel.add(levelsSetting);
        sliderPanel.add(levels);

        contentPane.add(sliderPanel, BorderLayout.SOUTH);

        levelsSetting.setText("Levels (" + levels.getMinimum() + "-" +
                               levels.getMaximum() + "): " + levels.getValue());
        

    }

    // Adjust the color resolution of an image when the slider is moved
    public void sliderChanged() {
        levelsSetting.setText("Levels (" + levels.getMinimum() + "-" +
                               levels.getMaximum() + "): " + levels.getValue());
        SImage picture = getOriginal();
        if (picture != null) {
            updateImage(requantize(levels.getValue(), picture));
        }
    }

    // Adjust the number of levels used in all the layers of an image
    public SImage requantize(int numLevels, SImage pic) {
        return new SImage( adjustPixels( numLevels, pic, SImage.RED ),
                           adjustPixels( numLevels, pic, SImage.GREEN ),
                           adjustPixels( numLevels, pic, SImage.BLUE )
                         );
    }

    // Adjust the levels used in a single layer of an image
    public int[][] adjustPixels(int numLevels, SImage pic, int layer) {
        int[][] shades = pic.getPixelArray(layer);
        int bandWidth = 256 / numLevels;

        for (int x = 0; x < pic.getWidth(); x++) {
            for (int y = 0; y < pic.getHeight(); y++) {
                shades[x][y] = shades[x][y] / bandWidth * bandWidth;
            }
        }
        return shades;
    }

   
}
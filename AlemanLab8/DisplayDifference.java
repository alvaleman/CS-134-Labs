
import squint.*;
import javax.swing.*;

/*
 * Class DisplayDifference - Calculates the difference in levels between the two images and 
 * displays it as a new image. 
 * 
 * Alvaro Aleman
 */
public class DisplayDifference extends ImageViewer
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH = 400, WINDOW_HEIGHT = 400;

    //invokes difference method and sets result in setImage 
    public DisplayDifference(SImage image, SImage image2) {
        setImage(difference(image, image2));

    }
    
    //calculates the difference in levels used in a single layer of the two images
    public int[][] CalculateDiff(SImage pic, SImage pic2, int layer){
        //calculate the smaller width and height of the two images
        int width1 = pic.getWidth();
        int height1 = pic.getHeight();
        int width2 = pic2.getWidth();
        int height2 = pic2.getHeight();
        int [][]input1 = pic.getPixelArray(layer); 
        int [][]input2 = pic2.getPixelArray(layer);
        int diffwidth = Math.min(width1, width2);
        int diffheight = Math.min(height1, height2);
        
        //create new image array with minimum values found
        int [][]difference = new int [diffwidth][diffheight];
      
        //fill new image array with value difference between the two images
        for(int x = 0; x < diffwidth; x++){
            for(int y = 0; y < diffheight; y++){
                difference[x][y] = Math.abs(input1[x][y] - input2[x][y]);
            }
        }
        return difference;
    }
    
    //calculates the difference in levels used in all layers of the two images
    private SImage difference(SImage pic, SImage pic2) {
        return new SImage( CalculateDiff( pic, pic2, SImage.RED ),
            CalculateDiff( pic, pic2, SImage.GREEN ),
            CalculateDiff( pic, pic2, SImage.BLUE )
        );
    }

}

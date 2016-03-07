
import squint.*;
import javax.swing.*;

/*
 * Class CompositeSimplifier - Applies two chosen image compression methods to array of chosen image. 
 * Also restores the original image from new encoded Image.
 * 
 * Alvaro Aleman
 * 11/11/14
 */
public class CompositeSimplifier extends ImageSimplifier{
   
    //Two transformations that will be used
    private ImageSimplifier one = new RecWaveletSimplifier();
    private ImageSimplifier two = new WaterfallSimplifier();
 
    //places the two transformations in the JComboBox
    public String toString(){
        return "RecWavelet and Waterfall";
    }
    
    //Transforms Image with the two encodings, one after another
    public SImage encode( SImage original){
        return two.encode(one.encode(original));
    }
    
    //Restores the original image
    public SImage decode( SImage shade ) {
        return one.decode(two.decode(shade));
    }
}

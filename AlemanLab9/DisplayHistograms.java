import java.awt.*;
import squint.*;
import javax.swing.*;

/*
 * DisplayHistograms displays a window containing histograms of the
 * red, green and blue brightness layers of an SImage
 */
public class DisplayHistograms extends GUIManager {
    // Size of window to display
    private final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 270;
    
    // Height to use for histogram graphs
    private final int HIST_HEIGHT = WINDOW_HEIGHT - 80;
    
    // The Colors used
    private final Color BACK_COLOR = Color.WHITE;
    private final Color TICK_COLOR = new Color( 180, 180, 180 );
    private final Color LINE_COLOR = Color.BLACK;
    
      
    // Create window and three histograms
    public DisplayHistograms( SImage source ) {
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
      
        contentPane.setLayout( new BorderLayout() );
        
        // Create panel to hold three graphs
        JPanel graphs = new JPanel();
        graphs.setLayout( new GridLayout(1, 3, 3, 0 ) );
        graphs.setBackground( Color.YELLOW );
        contentPane.add( graphs, BorderLayout.CENTER );
      
        // Create panel to hold bit per pixel information 
        JPanel huffmanPane = new JPanel();
        huffmanPane.setLayout( new GridLayout(1, 3, 3, 0 ) );
        Histogram [] histogram = new Histogram[3];
      
        
        double total = source.getWidth()*source.getHeight();
        for ( int c = SImage.RED; c <= SImage.BLUE; c++ ) {
            histogram[c] = new Histogram( source.getPixelArray( c ) );
            graphs.add( new JLabel( drawHistogram( histogram[c], c ) ) );
            huffmanPane.add( new JLabel( "Bits per pixel = " +  
                                          histogram[c].getHuffmanSize()/total)
                              );
        }
        
     
        contentPane.add( huffmanPane, BorderLayout.SOUTH );
       
    }
    
    // Draw a graph displaying a single histogram
    private SImage drawHistogram( Histogram histogram, int color ) {
        
        // Determine the range of brightness values recorded in the histogram
        int min = histogram.getMin();
        int max = histogram.getMax();
                
        // Determine the most common brightness value
        int maxFreq = histogram.frequency(min);
        for ( int brightness = min; brightness <= max; brightness++ ) {
            if ( histogram.frequency(brightness) > maxFreq ) {
                maxFreq = histogram.frequency(brightness);
            }
        }
                
        // Cludge to make image histograms show full range
        if ( min >= 0 && max < 255 ) {
            min = 0; max = 255;
        }

        // Draw the actual lines of the bar graph
        SImage result = new SImage( max - min + 1 , HIST_HEIGHT, BACK_COLOR );
        Graphics2D pen = result.createGraphics();
		pen.setColor(LINE_COLOR);
        
        for ( int b = min; b <= max; b++ ) {
            // place a light tick mark every 10 positions
            if ( b % 10 == 0 ) {
				pen.setColor(TICK_COLOR);
                pen.drawLine( b - min, HIST_HEIGHT, b - min, 0 );
				pen.setColor(LINE_COLOR);
            }
            
            if ( histogram.frequency(b) > -1 ) {
                pen.drawLine( b - min, HIST_HEIGHT - HIST_HEIGHT*histogram.frequency(b)/maxFreq, 
                              b - min, HIST_HEIGHT );
            }
        }
        return result;
    }   

}

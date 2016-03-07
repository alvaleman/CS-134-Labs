import java.awt.*;
import squint.*;
import javax.swing.*;

/*
 * DisplayHistograms displays a window containing histograms of the
 * red, green and blue brightness layers of an SImage
 */
public class DisplayHistograms extends GUIManager {
    // Size of window to display
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 270;
    
    // Range of values used in pixel arrays
    private final int MAX_SHADE = 255;
    private final int MIN_SHADE = 0;    
    
    // The Colors used
    private final Color BACK_COLOR = Color.WHITE;
    private final Color TICK_COLOR = new Color( 180, 180, 180 );

    // Height to use for histogram graphs
    private final int HIST_HEIGHT = WINDOW_HEIGHT - 80;

    // Create window and three histograms
    public DisplayHistograms(SImage source) {
        this.createWindow(WINDOW_WIDTH, WINDOW_HEIGHT);

        contentPane.setLayout(new BorderLayout());

        // Create panel to hold three graphs
        JPanel graphs = new JPanel();
        graphs.setLayout(new GridLayout(1, 3, 3, 0));
        graphs.setBackground(Color.YELLOW);
        contentPane.add(graphs, BorderLayout.CENTER);

        Histogram[] histogram = new Histogram[3];
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        
        // Create and display the histograms
        for (int c = SImage.RED; c <= SImage.BLUE; c++) {
            histogram[c] = new Histogram(source, c);
            graphs.add(new JLabel(drawHistogram(histogram[c], colors[c])));
        }
    }

    // Draw a graph displaying a single histogram
    private SImage drawHistogram(Histogram histogram, Color c) {

        int maxFreq = 0;

        // Determine the largest occurrence count
        for (int pos = MIN_SHADE; pos <= MAX_SHADE; pos++) {
            if (histogram.frequency(pos) > maxFreq) {
                maxFreq = histogram.frequency(pos);
            }
        }

        // Create an image to draw on and the pen and color needed for drawing
        SImage result = new SImage( MAX_SHADE - MIN_SHADE + 1, 
                                    HIST_HEIGHT, BACK_COLOR);
        Graphics2D pen = result.createGraphics();
        pen.setColor(c);

        // Draw the lines of the graph
        for (int b = MIN_SHADE; b <= MAX_SHADE; b++) {
            // place a light tick mark every 10 positions
            if (b % 10 == 0) {
                pen.setColor(TICK_COLOR);
                pen.drawLine(b - MIN_SHADE, HIST_HEIGHT, b - MIN_SHADE, 0);
                pen.setColor(c);
            }
            pen.drawLine(b - MIN_SHADE, 
                         HIST_HEIGHT - (HIST_HEIGHT * histogram.frequency(b) / maxFreq),
                         b - MIN_SHADE, 
                         HIST_HEIGHT);

        }
        return result;
    }

}

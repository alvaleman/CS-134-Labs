import squint.*;
/*   Histogram- Builds histogram of current image and also computes huffman code
 * 
 *   Alvaro Alean
 */
public class Histogram {

    // The counts of how many times each "brightness" value was found
    private int [] hist;

    // Range of brightness values found in "source"
    private int max, min;

    // Determine the range of values stored in source and build a list of counts
    // of how many times each value occurs
    public Histogram( int[][] source ) {

        // Find the largest and smallest values in source
        max = source[0][0];
        min = max;

        for ( int x = 0; x < source.length; x++ ) {
            for ( int y = 0; y < source[0].length; y++ ) {
                min = Math.min(source[x][y], min);
                max = Math.max(source[x][y], max);
            }
        }

        // Create an array to hold count for values ranging from min to max.
        // The count for the value min will be in hist[0], the count for 
        // value max will be in hist[max-min].  max (and min) may be positive
        // or negative.
        hist = new int[max - min + 1];

        for ( int x = 0; x < source.length; x++ ) {
            for ( int y = 0; y < source[0].length; y++ ) {
                hist[ source[x][y] - min ] ++;
            }
        }
    }

    // Return the number of times a particular value was found in the array
    // passed to the constructor or -1 if the value requested is outside
    // the range of values found in the array.
    public int frequency( int shade ) {
        if ( min <= shade && shade <= max ) {
            return hist[ shade - min ];
        } else {
            return -1;
        }
    }

    // Returns the largest brightness value found in the image
    public int getMax() {
        return max;
    }

    // Returns the smallest brightness value found in the image
    public int getMin() {
        return min;
    }

    /** Returns the number of BITS required to Huffman encode the entire
     *  2D array for which this histogram was computed.
     */
    public int getHuffmanSize() {
        //create new array with length of histogram count
        int[] Weights = new int[hist.length]; 

        //variable used to keep track of remaining non-zero entries
        int remaining = 0;

        //copies all non-zero entries in the histogram array into contiguous positions 
        //in a new weights array
        for (int i=0; i < hist.length; i++){

            if(hist[i] !=0) {
                
                Weights[remaining] = hist[i];

                //sets remaining equal to number of non zero entries
                remaining++;

            }

        }

        //variable used to keep track of total bits required to encode using Huffman 
        int totalBits = 0;

        while(remaining >1 ) {
            //gets index of minimum value from the Weights array
            int minIndex = findMin(Weights, remaining);

            //variable equal to the min value in Weights array 
            int min = Weights[minIndex];
            //remove the minimal element from Weights array 
            Weights[minIndex] = Weights[remaining - 1];
            Weights[(remaining - 1)] = 0;

            //decrease number of remaining Weights
            remaining--;

            //locate minimal value among remaining elements
            minIndex = findMin(Weights, remaining);
            //Adds saved value to the minimum value of remaining elements
            Weights[minIndex] += min; 
            //adds sum to the total number of bits required
            totalBits += Weights[minIndex];

        }

        return totalBits;
    }

    //returnes index of minimum value in an array
    private int findMin(int[] Weights, int remaining){
        //variable used to keep track of position in array
        int minIndex = 0;

        //loop used to determine minimum value and returns index. 
        for(int i = 1; (i < Weights.length) && (Weights[i] !=0); i++){
            if (Weights[i] < Weights[minIndex]){
                minIndex = i;

            }

        }

        return minIndex;
    }

}
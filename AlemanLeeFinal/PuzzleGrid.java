import squint.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
 * Class PuzzleGrid - A GUI Component that holds a grid of buttons that will hold the randomized boggle letters. 
 * Also regulates actions for creating a word when these buttons are clicked. 
 * 
 * Alvaro Aleman and Lia Lee
 * 
 */
public class PuzzleGrid extends GUIManager {   
    // Dimensions of the grid
    public final int WIDTH = 4;
    public final int HEIGHT = 4;

    // Array of all buttons
    private PuzzleButton [] allButtons = new PuzzleButton[ WIDTH*HEIGHT ];

    // Random letter chooser used to fill 4x4 puzzle grid
    private Random letterChooser = new Random();

    //array keeping track of buttons already clicked
    private PuzzleButton [] clicked = new PuzzleButton [16];

    // variable used to keep track of a PuzzleButton
    private PuzzleButton button;

    //int keeping track of number of buttons clicked
    private int buttonsClicked;

    //boolean keeping track if array contains button 
    private boolean contains;

    // Interacting with BoggleGame superclass to send information
    private BoggleGame game;

    // Array containing 16 randomized letters
    private String[] randomLetterArray = new String[ WIDTH*HEIGHT ];    

    // Text of possible constructed word
    private String potentialWord = new String();

    // Dictionary used to check if current constructed String is a possible word
    private Lexicon lexicon = new Lexicon();

    //Used for recursive method 
    // Recursive list of all possible words
    private WordList allWordsList = new WordList();

    // Array keeping track of all buttons checked for recursive method;
    private PuzzleButton[] allPreviousButtons = new PuzzleButton [16];

    // integer keeping track of maximum possible score
    private int maxScore = 0;

    //Sets GridLayout and places PuzzleButtons within it
    public PuzzleGrid(BoggleGame game) {
        //associates game that created puzzleGrid as the instance variable
        this.game =game;
        
        contentPane.setLayout( new GridLayout(HEIGHT, WIDTH, 0, 0) );

        // Create 4x4 grid with empty buttons
        for ( int y = 0; y < HEIGHT; y++ ) {
            for ( int x = 0; x < WIDTH; x++ ) {
                PuzzleButton button = new PuzzleButton( y, x, "" );
                allButtons[ y*WIDTH + x ] = button;
                contentPane.add( button );
            }
        }
    }

    //Method used to pass all buttons on the grid to superclass
    public PuzzleButton [] allbutton() {
        return allButtons;
    }

    // Randomly selects letters from Boggle cube sides to fill the 4x4 PuzzleGrid
    public String[] randomizeLetters() {
        // Domain of Boggle cube sides to select letters from 
        String [][] cubeSides = 
            new String[][]{
                { "A", "A", "C", "I", "O", "T" },
                { "A", "B", "I", "L", "T", "Y" },
                { "A", "B", "J", "M", "O", "Qu" },
                { "A", "C", "D", "E", "M", "P" }, 
                { "A", "C", "E", "L", "S", "R" }, 
                { "A", "D", "E", "N", "V", "Z" }, 
                { "A", "H", "M", "O", "R", "S" }, 
                { "B", "F", "I", "O", "R", "X" }, 
                { "D", "E", "N", "O", "S", "W" }, 
                { "D", "K", "N", "O", "T", "U" }, 
                { "E", "E", "F", "H", "I", "Y" }, 
                { "E", "G", "I", "N", "T", "V" }, 
                { "E", "G", "K", "L", "U", "Y" }, 
                { "E", "H", "I", "N", "P", "S" }, 
                { "E", "L", "P", "S", "T", "U" }, 
                { "G", "I", "L", "R", "U", "W" }
            };

        int currentArraySize = 16;
        int letterPosArray = 0;

        // Randomizing the other 15 letters from Boggle cube sides
        for ( int x = 0; x< 16; x++ ) {
            int cubeNumber = letterChooser.nextInt( currentArraySize );
            int sideNumber = letterChooser.nextInt( 6 );

            // Assigning 1st randomized letter as the 1st button
            randomLetterArray[ letterPosArray ] = cubeSides[ cubeNumber ][ sideNumber ];

            // Replace random row selected with last row of cubeSides
            cubeSides [cubeNumber] = cubeSides [currentArraySize-1];

            // Update size count of current array
            currentArraySize = currentArraySize - 1;

            // Filling a letter in the next position of the array
            letterPosArray = letterPosArray + 1;
        }
        
        return randomLetterArray;
    }
    
    //Sets actions for when a button on the grid is pressed
    public void buttonClicked( JButton button ) {
        // Overriding the buttonClicked method in BoggleGame
        PuzzleButton whichLetter = (PuzzleButton) button;

        // Properly highlighting letters if conditions are met
        if ( clicked[0]==null || buttonsClicked == 0 ){
            // Setting the 1st button clicked green
            whichLetter.setForeground(Color.BLUE);
            clicked[0] = whichLetter;
            buttonsClicked = buttonsClicked +1;

            // Send letter inside button to BoggleGame to update word label
            game.updateLetter( whichLetter.getText() );

        }
        else if (!contains(whichLetter) && whichLetter.isAdjacentTo(clicked[buttonsClicked-1])){
            // Setting other appropriate buttons blue
            clicked[buttonsClicked] = whichLetter;

            //Sets newly clicked button to blue
            whichLetter.setForeground(Color.BLUE);

            //set previous button green
            PuzzleButton previous = clicked[buttonsClicked - 1];
            previous.setForeground(Color.GREEN);

            // Updates the number of buttons clicked
            buttonsClicked++;

            // Send letter inside button to BoggleGame to update word label
            game.updateLetter( whichLetter.getText() );

            //instructions to submit word when button is double-clicked
        } else if(clicked [buttonsClicked - 1] == whichLetter ){

            //checks to see if word is valid and resets the grid to make new word if it is
            if (game.WordChecker()) {
                ResetGrid();

            }

            //Resets grid if user clicks on currently green letter
        }else if( contains(whichLetter)){
            ResetGrid();
        }
    }

    //resets clicked array and buttons on grid back to black
    public void ResetGrid(){
        for(int i=0; i < buttonsClicked; i++){
            PuzzleButton allButtons = clicked[i];  
            allButtons.setForeground(Color.BLACK);
            clicked[i] = null;
        }

        //reset currently clicked buttons 
        PuzzleButton [] clicked = new PuzzleButton [16];
        buttonsClicked = 0;

        //resets current word
        game.ResetWord();
    }

    // Checks to see if a button has already been clicked
    public boolean contains(PuzzleButton which){
        contains = false;
        for(int x=0; x<clicked.length -1 ; x++){
            if( which == clicked[x]){

                contains = true;
            }
        }

        return contains;
    }

    // Placing 16 randomized letters onto their respective buttons
    public void setLetters() {
        
        // If there is no connection present, randomize a letter grid
        if(!game.checkConnection()){

            String [] randomizedLetters = randomizeLetters( );
        }

        // Place buttons representing pieces in a grid layout and array
        for ( int x = 0; x < 16; x++ ) {
            button = allButtons[ x ];

            button.setText(randomLetterArray[x]);
        }

        // Creating a new lsit of all possible words for every new grid
        allWordsList = new WordList();
        maxScore = 0;

        // Recursively check possible words for all 16 buttons ( can comment this out to avoid terminal window)
        for ( int x = 0; x < 16; x++ ) {
            button = allButtons[ x ];
            
            allPreviousButtons[ 0 ] = button;

            recursiveSearch( 0, allPreviousButtons );
        }
    }        
    
    // Recursive search to check all possible words made from a path
    public void recursiveSearch( int count, PuzzleButton[] allPreviousButtons ) {
        //String of possible constructed word
        String potentialWord = obtainPotentialWord( allPreviousButtons, count );

        if ( lexicon.containsPrefix( potentialWord ))  {
            if( lexicon.contains( potentialWord ) && potentialWord.length()>2 && !allWordsList.contains( potentialWord ) ){
                // add word to allWords list when it has been found in the dictionary   
                allWordsList.add( potentialWord);
                 maxScore += allWordsList.determineScore(potentialWord);
            }

            for( int x = 0; x < 16; x++ ) {
                if ( allButtons[x].isAdjacentTo( allPreviousButtons[ count ] ) && !isContained( allPreviousButtons, allButtons[ x ], count )) {
                    // increase count of buttons checked
                    count++;

                    // establish neighbor button as one to check next (for all paths)    
                    allPreviousButtons[ count ] = allButtons[ x ];

                    // check the next button and word sequence
                    recursiveSearch( count, allPreviousButtons ) ;

                    // decrease count to go back to another button to check another possible pathway
                    count--;
                }
            }
        }
    }

    // Extract text from button array
    public String obtainPotentialWord( PuzzleButton[] allButtons, int count ) { 
        String wordText = "";

        for( int x=0; x < count +1 ; x++) {
            wordText = wordText + allButtons[x].getText();
        }

        return wordText;
    }

    // Checking to see if a PuzzleButton is in a PuzzleButton array
    public boolean isContained( PuzzleButton[] allPreviousButtons, PuzzleButton button, int count ) {
        for( int x = 0 ; x < count; x++ ) {
            if ( allPreviousButtons[x] == button ) 
                return true;
        }
        return false;
    }
    
    //returns all possible words to superclass 
    public String returnAllWords() {
        this.allWordsList = allWordsList;

        return allWordsList.toString();
    }

    // Setting pre-assigned randomized letters for 2 player gaming
    public void setLetters2Player( String [] randomizedLetters ) {
        for ( int x = 0; x < 16; x++ ) {
            randomLetterArray[ x ] = randomizedLetters[ x ];
        }
    }

    //Returns maximum possible score
    public int getMaxScore() {
        return maxScore;
    }
    
    //Disables all the buttons on the grid
    public void deactivate( ) {
        for ( int y = 0; y < HEIGHT; y++ ) {
            for ( int x = 0; x < WIDTH; x++ ) {
                button = allButtons[ y*WIDTH + x ];

                button.setEnabled(false);
            }
        }
    }

    //enables all the buttons on the grid
    public void activate( ) {
        for ( int y = 0; y < HEIGHT; y++ ) {
            for ( int x = 0; x < WIDTH; x++ ) {
                button = allButtons[ y*WIDTH + x ];

                button.setEnabled(true);
            }
        }
    }

    //sets winning, losing, or tied message on the buttons
    public void setFinale() {
        // Place buttons representing pieces of win loss in a grid layout and array

        String []win = new String [] {"", "Y","O","U","","W","I","N",":D",":D",":D",":D","!","!","!","!"};

        String []loss = new String []{"", "Y","O","U","L","O","S","E",":(",":(",":(",":(","!","!","!","!"};

        String []tie = new String []{ "", "", "" , "", "SC", "O", "R", "E", "T", "I", "E", "D", "" , "" , "" , ""};

        if( game.getTotalScore() > game.getEnemyScore()) {
            for ( int x = 0; x < 16; x++ ) {
                button = allButtons[ x ];

                button.setText(win[x]);
            }

        } else if ( game.getTotalScore() == game.getEnemyScore()){
            for ( int x = 0; x < 16; x++ ) {
                button = allButtons[ x ];

                button.setText(tie[x]);
            }
        }else{

            for ( int x = 0; x < 16; x++ ) {
                button = allButtons[ x ];

                button.setText(loss[x]);
            }
        }
    }
}
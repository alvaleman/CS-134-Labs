
import squint.*;
import javax.swing.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

/*
 * Class BoggleGame - Creates main GUI interface for a self-constructed Boggle Game, which can be used for 1-player or 2-player use. 
 * 
 * Alvaro Aleman and Lia Lee
 */

public class BoggleGame extends GUIManager
{
    // Initial dimensions of the program window
    private final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 600;

    // Adjusts length of game and JProgressBar
    private final int GAME_DURATION = 180;
    private final int min = 0;

    // Buttons to find new game and to find an opponent
    private JButton newGame = new JButton( "Let's Boggle!");
    private JButton findPartner = new JButton( "Find Opponent");
    private JButton allWords = new JButton( "Display All Words" );

    // Textfield used to keep track of words already created
    private JTextArea words = new JTextArea();

    // Variables keeping track of current score and high score
    private int totalScore = 0;
    private int highScore = 0;

    // Labels used to display generated word, current gamePlay type, and score
    private JLabel gamePlay = new JLabel("<html><b><u>Playing Solitaire </u></b></html>");
    private JLabel createdWord = new JLabel( "");
    private JLabel score = new JLabel("<html><b><u>Score: </u></b></html>");
    private JLabel highestScore = new JLabel( "<html><b><u>  High Score: </u></b></html>" );

    //Labels and textfields used to enter name and group name 
    private JTextField enterName = new JTextField(10);
    private JTextField enterPartner = new JTextField(10);
    private JLabel name = new JLabel("Your name:");
    private JLabel groupName = new JLabel("Partner Group:");

    //progressBar used to display remaining time 
    private JProgressBar remainingTime = new JProgressBar(min, GAME_DURATION);

    //pacemaker used to keep track of time during gamePlay
    private PaceMaker timer;

    // timer tick duration (in seconds)
    private final int TICK_DURATION = 1;

    //Int to keep track of time
    private int timeRemaining = GAME_DURATION;
    
    //int keeping track of number of words accepted
    private int acceptedWords = 0;

    // Keeping track of our word/player 2's word constructed
    private String word, enemyWord; 

    //int keeping track of opponents total score
    private int enemyScore = 0;

    //int keeping track of incoming letters from server
    private int incomingCount = 0;

    //The grid of puzzle Buttons
    private PuzzleGrid buttons;

    //Panel holding the components in their respective screen spaces
    private JPanel controlPanel, controlNorth, northWest, northEast, southWest, southEast;

    //panel holding name/group components to connect to multiplayer game
    private JPanel player, group;

    //instance of lexicon class used to check word validity 
    private Lexicon lexicon = new Lexicon(); 

    //net connection to the server for Boggle pair-play
    private NetConnection connection;

    //port number and server name
    private final int PORT_NUMBER = 13413;
    private String BOGGLE_SERVER = "rath.cs.williams.edu";
    private boolean connected = false;

    //string array to keep track of new 16 letter set for pair-play
    private String [] randomizedLetters = new String [16];

    //recursive lists to store ongoing submitted words
    private WordList ourList = new WordList();
    private WordList enemyList = new WordList();
    
    public BoggleGame() {
        // Create window to hold all the components
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
        contentPane.setLayout( new BorderLayout() );

        // Place the buttons in the center of the window
        buttons = new PuzzleGrid( this );
        contentPane.add( buttons, BorderLayout.CENTER );
        buttons.deactivate();

        //add field to keep track of created words and score
        words.setEditable(false);
        contentPane.add( new JScrollPane(words), BorderLayout.EAST);
        words.setText( "WORDS FOUND:" ); 

        //add labels and progress bar at the top
        controlNorth = new JPanel();
        controlNorth.setLayout (new BorderLayout());
        northWest = new JPanel();
        northWest.setLayout( new BorderLayout() );
        highestScore.setText( "<html><b><u>High Score: </u></b></html>" + highScore );
        northWest.add( highestScore, BorderLayout.WEST );

        controlNorth.add( northWest, BorderLayout.WEST);
        controlNorth.add( createdWord, BorderLayout.CENTER);
        createdWord.setHorizontalAlignment( SwingConstants.CENTER );
        controlNorth.add( remainingTime, BorderLayout.NORTH);

        northEast = new JPanel();
        northEast.add( score );

        score.setText( "<html><b><u>Score: 0 </u></b></html>" );
        controlNorth.add(northEast, BorderLayout.EAST);
        contentPane.add( controlNorth, BorderLayout.NORTH );

        //add control buttons and text field
        controlPanel = new JPanel();
        controlPanel.setLayout( new GridLayout(1, 2));

        southWest = new JPanel();
        southWest.setLayout( new GridLayout(1,3) );
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout( new GridLayout(1,2) );
        southWest.add( allWords );
        panelButtons.add(newGame);
        panelButtons.add(findPartner);
        southWest.add(panelButtons);
        allWords.setEnabled( false );
        controlPanel.add(southWest);

        //add name and partner group information     
        southEast = new JPanel();
        southEast.setLayout(new GridLayout (3, 1)); 

        JPanel gametype = new JPanel();
        gametype.add(gamePlay);
        southEast.add( gametype );

        player = new JPanel();
        player.add( name );
        player.add( enterName );
        southEast.add(player);

        group = new JPanel();
        group.add( groupName );
        group.add( enterPartner );
        southEast.add(group);

        controlPanel.add( southEast);
        contentPane.add( controlPanel, BorderLayout.SOUTH);

        //refreshing page to make sure everything is in its place
        repaint();
    }

    // Actions to carry out when a BoggleGame button has been clicked
    public void buttonClicked( JButton which ) {
        // set option to display all words disabled
        allWords.setEnabled( false );
        
        if(which.getText() == "Give up! :( ") {
            //Ends game when player decides to surrender 
            if ( connected ) {
                connection.close();
            } else 
                endGame();
        }else if(which == newGame){
            // Resets the board for a new game
            buttons.activate();
            buttons.setLetters();

            gamePlay.setText( "<html><b><u>Playing Solitaire </u></b></html>" );

            resetGame();
        } else if (which == allWords) {
            // display all possible letters
            words.append( "\n \nALL POSSIBLE WORDS: \n" + buttons.returnAllWords() + "\n Maximum Score:" + buttons.getMaxScore()  );
        } else {
            // "find opponent" ==> establish connection for pair-play
            connection = new NetConnection ( BOGGLE_SERVER, PORT_NUMBER );

            //Listens for incoming server lines 
            connection.addMessageListener(this);
            
            //Starting command to play game 
            connection.out.println( "PLAY " + enterName.getText().trim() + " "+ enterPartner.getText());

        }           
    }

    public void resetGame() {
        // start timer 
        timer = new PaceMaker( TICK_DURATION, this);
        timeRemaining = GAME_DURATION;

        // reset score and previously made words
        totalScore = 0;
        enemyScore = 0;
        words.setText( "WORDS FOUND:" );
        acceptedWords = 0;
        score.setText("<html><b><u>Score: 0 </u></b></html>");
        ourList = new WordList();
        enemyList = new WordList();

        // reassign label
        newGame.setText("Give up! :( ");

        // disable find partner button
        findPartner.setEnabled(false);
    }

    // Decrement the game timer when game begins
    public void tick() {
        timeRemaining--;
        remainingTime.setValue(timeRemaining);
        if(timeRemaining ==0) 
            if( connected)
                connection.close();
            else
                endGame();
    }

    // Ends the current game session
    public void endGame() {
        // display total score and list of words
        words.append( "\n \n GAME OVER " );
        words.append( "\n \n # of words: " + acceptedWords );

        // show all possible words
        allWords.setEnabled( true );

        // resets current word
        ResetWord();

        // relabels the start button
        newGame.setText("Let's Boggle!");

        // resets find partner button
        findPartner.setEnabled(true);

        // stops tick and sets remaining time to 0
        timer.stop();
        timeRemaining = 0;
        remainingTime.setValue(timeRemaining);

        //disables all letter buttons + sets them back to black
        buttons.deactivate();
        buttons.ResetGrid();

        //resets count of incoming server letters
        incomingCount = 0;

        //setting a high score
        if ( totalScore > highScore ) {
            highScore = totalScore;

            words.append( "\n \n NEW HIGH SCORE!" );

            highestScore.setText( "High Score: " + highScore );
        }
    }
    
    // Updates text of the word being constructed
    public void updateLetter( String l) {
        word = createdWord.getText();
        word = word + l;

        createdWord.setText( word );
    }

    // Checks proper conditions accept a constructed word
    public boolean WordChecker (){
        boolean inDictionary = false;
        if(word.length() >= 3 && lexicon.contains(word) && ! ourList.contains( word )) {
            //update completed words
            updateList(word);

            //update number of accepted words
            acceptedWords++;

            // Add list to WordList
            ourList.add( word );

            //Update total score 
            if( !enemyList.contains(word) ){
                totalScore += ourList.determineScore( word ); 
                updateScore();
            } else {
                enemyScore -= enemyList.determineScore( word );

                updateScore();
            }

            //if multiplayer, send approved word to the server
            if ( connected ) {
                connection.out.println( "WORD " + word );

            }

            //make current created word blank
            ResetWord();

            //labels submitted word as found in the dictionary
            inDictionary = true;
        } 
        
        return inDictionary;
    }

    //resets currently created word 
    public void ResetWord(){
        createdWord.setText("");
        word="";
    }

    //updates list of newly constructed words
    public void updateList(String newWord) {
        words.append( "\n" + newWord);
    }

    // updates game score
    public void updateScore(){
        if (!connected){
            score.setText("Score: " + totalScore );
        }else{
            score.setText("Score: " + totalScore + " vs. " + enemyScore);
        }
    }

    // updates extra necessary multiplayer functions
    public void dataAvailable() {
        String lineFromServer = connection.in.nextLine();

        if ( lineFromServer.startsWith( "START" )) {
            // new set of 16 random letters for each pair-play
            String [] randomizedLetters = new String [16];

            // if connection with server is present, reset game 
            resetGame();

            //makes buttons active
            buttons.activate();

            // Display player 2's name
            String player2Name = lineFromServer.substring( 6 );

            //updates boolean for connection
            connected = true;

            // Edit code to display player 2's name
            gamePlay.setText( "Playing against: " + player2Name);

        } else if ( lineFromServer.startsWith( "WORD" )) {
            // Append player 2's list of constructed words
            
            enemyWord = lineFromServer.substring( 5 );
            enemyList.add( enemyWord );
            
            if( !ourList.contains(enemyWord) ){
                enemyScore += enemyList.determineScore( enemyWord );
                updateScore();
                
            } else {
                //Update total score 
                totalScore -= ourList.determineScore( enemyWord );

                //display score
                updateScore();
            }
            
        } else {
            // String array to keep track of pre-assigned randomized letters
            randomizedLetters[incomingCount] = lineFromServer;
            incomingCount++;

            if (incomingCount==16) {
                // Send set of letters to PuzzleGrid for configuration
                buttons.setLetters2Player( randomizedLetters );
                buttons.setLetters();
            }
        }
    }

    //Boolean to check whether or not there is a connection
    public boolean checkConnection(){
        return connected;
    }

    //Instructions to carry out when connection is closed
    public void connectionClosed( ){       
        //Ends current game session
        endGame();

        // show win or loss
        buttons.setFinale();

        //display words opponent got in pair play
        words.append( "\n OPPONENT WORDS FOUND: \n" + enemyList.toString() );

        //Makes sure the connection is closed if server terminates connection
        connection.close();

        connected = false;
    }

    //used to set finale Grid
    public int getTotalScore() {
        return totalScore;
    }

    //used to set finale Grid
    public int getEnemyScore(){    
        return enemyScore;
    }
}

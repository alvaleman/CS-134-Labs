
import squint.*;
import javax.swing.*;

/*
 * Class EmailReader - Logs you into POP server and retrieves wanted message 
 * 
 * Alvaro Aleman
 */
public class EmailReader extends GUIManager
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH = 700, WINDOW_HEIGHT = 400;
    
    //These values adjust the size of the main text area
    private final int AREA_WIDTH = 50, AREA_HEIGHT = 10;
    
    //These values adjust the size of the log that tracks server responses
    private final int LOG_LINES = 5, LOG_WIDTH= 55;
    
    //These values adjust the size of the text fields in which username and passwords are input
    private final int FIELD_WIDTH = 20; 
    
    //This value adjusts the size of the text field for the wanted mesage number
    private final int MESSAGE_WIDTH = 8;
    
    //Server address
    private final String POP_SERVER = "fuji.cs.williams.edu" ;
    
    //Server port number
    private final int POP_PORT = 110; 

    //Instance variables 
    
    //Used to input the passwrod
    private JTextField password;
    
    //Used to input username
    private JTextField account;
    
    //Used to input wanted message number
    private JTextField number;

    //Used to view wanted message
    private JTextArea message;
    
    //Used to log server's responses
    private JTextArea log; 

    //Used to send username and password to server
    private JButton Login;
    
    //Used to send QUIT command to server & log out
    private JButton Logout;
    
    //Used to retrieve wanted message
    private JButton GetMessage;
    
    //Variable used to establish connection
    private NetConnection connection;
    
    //variable used to retrieve messages sent from server
    private String lineFromServer;
    
    //variable used to obtain number of messages in mail account
    private int numberOnly; 

    
    //Constructor creates window and GUI components and sets the button states
    public EmailReader() {
        // Create window to hold all the components
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
        
        //Adds label for user to know where to place username
        contentPane.add (new JLabel( "Mail Account:" ));
        
        //Creates text field in which to input username
        contentPane.add (account = new JTextField (FIELD_WIDTH));

        //Adds label for user to know where to place password
        contentPane.add( new JLabel ( " Password:" ));
        
        //Creates text field in which to input password
        contentPane.add( password = new JPasswordField (FIELD_WIDTH));
        
        //Adds label for user to know where to place wanted message number
        contentPane.add( new JLabel ( "Message Number To Retrieve:" ));
        
        //Creates text field in which to input wanted message number
        contentPane.add(number = new JTextField (MESSAGE_WIDTH));

        //Creates button to retrieve a message and has it initially disabled
        contentPane.add(GetMessage = new JButton ( "Get Message") );
        GetMessage.setEnabled(false);
        
        //Creates text area where main information and mail messages will be displayed
        message = new JTextArea (AREA_HEIGHT, AREA_WIDTH);
        contentPane.add( new JScrollPane ( message) );

        //creates text area that keeps track of server responses and disables user from editing it
        log = new JTextArea ( LOG_LINES, LOG_WIDTH);
        contentPane.add(new JScrollPane ( log) );
        log.setEditable( false);
        
        //Creates log in button and has it initially enabled
        contentPane.add (Login = new JButton ("Log in"));
        Login.setEnabled(true);
        
        //Creates log out button and has it initially disabled
        contentPane.add (Logout = new JButton ("Log out"));
        Logout.setEnabled(false);

        // Place code to create desired user interface components here.
    }

    /*
     *  Sets actions for each button clicked
     */
    public void buttonClicked(JButton which ) {

        //Create conditions for when a button is clicked
        
        
        if (which == Login) {
            //establishes connection to server
            connection = new NetConnection ( POP_SERVER, POP_PORT);
            lineFromServer = connection.in.nextLine() + "\n";
            
            //Sends username to server and retrieves confirmation
            connection.out.println( "USER " + account.getText() );
            lineFromServer = connection.in.nextLine() + "\n";
            log.append( lineFromServer );
            
            //Sends password to server and retrieves response
            connection.out.println( "PASS " + password.getText() );
            lineFromServer = connection.in.nextLine() + "\n";
            log.append( lineFromServer ); 
            

            if ( lineFromServer.startsWith ("+OK")) {
                // Gives user number of messages available if they successfully log in 
                
                //Sends STAT command to server to verify number of messages
                connection.out.println( "STAT");
                lineFromServer = connection.in.nextLine() + "\n";
                
                //These while loops find the position the first integer is located at in the STAT command and retrieves it
               
                int i = 0;
                while (!Character.isDigit(lineFromServer.charAt(i))) i++;
                int j = i;
                while (Character.isDigit(lineFromServer.charAt(j))) j++;
                
                //Takes the found character and converts it into an int to display
                numberOnly = Integer.parseInt(lineFromServer.substring(i, j));
  
                //Tells user how many messages they have in their mailbox
                message.setText("You have " + numberOnly + " messages.");
                
                //Changes the states of the buttons, enabling log out and message when the user logs in
             
                GetMessage.setEnabled(true);
                Login.setEnabled(false);
                Logout.setEnabled(true);
                
                //Refreshes layout
                repaint ();

            } else {
                //Displays error message if the user was unable to log in
                message.setText("ERROR - Incorrect Username or Password");
            }

        }

        if (which == Logout) { 
            //Sends the quit command to log out of the server and retrieves response
            connection.out.println( "QUIT" );
            lineFromServer = connection.in.nextLine() + "\n";
            
            //Tells user they have successfully logged out
            message.setText( "You are now Logged out.");

            //Closes the connection
            connection.close();

            //Sets the new states for buttons, only making log in available
            GetMessage.setEnabled(false);
            Login.setEnabled(true);
            Logout.setEnabled(false);

        }

        if (which ==GetMessage) {
            //Retrieves wanted message if it is available
            connection.out.println("RETR " + number.getText());
            lineFromServer = connection.in.nextLine() + "\n";
            if(lineFromServer.startsWith ("+OK")) {
                //Displays mail in main text area
                lineFromServer = connection.in.nextPOPResponse();
                message.setText(lineFromServer);

            }
            else{
                //Gives error messages if incorrect number is written
                message.setText("Incorrect Message Number");

            }

        }    

    }
}

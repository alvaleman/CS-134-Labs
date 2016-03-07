import squint.*;
import javax.swing.*;

// MessageMenu --- This program allows its user to view mail messages with
// the help of a menu listing the senders and subjects of the messages.
public class MessageMenu extends GUIManager {
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH  = 650, WINDOW_HEIGHT = 450;
   
    // Used to enter the POP account identifier
    private JTextField user = new JTextField( 20 );
        
    // Used to enter the POP account password
    private JPasswordField pass = new JPasswordField( 15 );
        
    // Pressed to login once account info has been entered
    private JButton logInOut = new JButton( "Log in" );
        
    // List of message descriptions
    private JComboBox messageList = new JComboBox();
                
    // Email messages are displayed in this area
    private JTextArea message = new JTextArea( 20, 50 );
            
    // The connection to the server
    private NetConnection toServer;
        
   /*
    * Create all the GUI components and display the login components
    */
    public void init() {
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
        
        // Create fields for entering the account information
        JPanel curPanel = new JPanel();
        curPanel.add( new JLabel( "Mail Account:" ) );
        curPanel.add( user );
        contentPane.add( curPanel );
        
        curPanel = new JPanel();
        curPanel.add( new JLabel( "Password:" ) );
        curPanel.add( pass );
        contentPane.add( curPanel );
                
        // Install the logout button in the message access pane
        contentPane.add( logInOut );
        
        // Add components for displaying messages 
        messageList.setEnabled( false );
        contentPane.add( messageList );
        contentPane.add( new JScrollPane( message ) );
    }
    
    
    
    
    
   /*
    * When the button is clicked, interact with the POP server to
    * log in, log out, or access the requested message.
    */
    public void buttonClicked( JButton which ) { // Log in
        String uninterestingResponse; 
        
        if ( which == logInOut && toServer == null ) {
            
            // Connect to the server
            toServer = new NetConnection( "fuji.cs.williams.edu", 110 );       
            uninterestingResponse = toServer.in.nextLine();
        
            // Send account information
            toServer.out.println( "USER " + user.getText() );
            uninterestingResponse = toServer.in.nextLine();
            toServer.out.println( "PASS " + new String( pass.getPassword()) );
            
            // Check that the login was accepted by the server
            if ( toServer.in.nextLine().startsWith( "+OK" ) ) {
                
                // Retrieve all of the message in the account to build a message menu
                int messageNum = 1;
                toServer.out.println( "RETR " + messageNum );
                String retrResponse = toServer.in.nextLine();
            
                while ( retrResponse.startsWith( "+OK" ) ) {
                    String messageBody = "";
                    String messageLine = toServer.in.nextLine();
                    while ( ! messageLine.equals( "." ) ) {
                        messageBody = messageBody + messageLine + "\n";
                        messageLine = toServer.in.nextLine();
                    }
                   
                    // extract the from address from the current message
                    String messageDesc = "";
                    int fromStart = messageBody.indexOf( "Date:" );
                    if ( fromStart >= 0 ) {
                        int fromEnd = messageBody.indexOf( "\n", fromStart + 1 );
                        messageDesc = messageBody.substring( fromStart, fromEnd );
                    } 
                    
                    // Add a message entry to the menu
                    messageList.addItem( messageNum + ": " + messageDesc );
 
                    // Get the next message
                    toServer.out.println( "RETR " + messageNum );
                    messageNum = messageNum +1;
                    retrResponse = toServer.in.nextLine();
                    
                }
                
                // Change the log in/out button and activate the message menu
                logInOut.setText( "Log Out" );
                messageList.setEnabled( true );
                          
                
            } else { // If the login attempt was rejected, warn the user
                message.setText( "Login failed.  Check your id and password" );
                toServer.close();
                toServer = null;
            }
            
            
        } else if ( which == logInOut && toServer != null) { // Log out
            
            // Terminate the connection
            toServer.out.println( "QUIT" );
            uninterestingResponse = toServer.in.nextLine();
            toServer.close();
            toServer = null;
            
            // Clear the message area, message menu and change log in button
            message.setText( "" );
            messageList.setEnabled( false );
            messageList.removeAllItems();
            logInOut.setText( "Log in" );

        }
   }
     
   public void menuItemSelected() {
       // Display the requested message
       toServer.out.println( "RETR " + ( messageList.getSelectedIndex() + 1 ) );
       String retrResponse = toServer.in.nextLine();
       if ( retrResponse.startsWith( "+OK" ) ) {
           String messageBody = "";
           String messageLine = toServer.in.nextLine();
           while ( ! messageLine.equals( "." ) ) {
               messageBody = messageBody + messageLine + "\n";
               messageLine = toServer.in.nextLine();
            }
            message.setText( messageBody );
        }
    }
   
   // Start execution when an instance of the program is created
   public MessageMenu() {
       this.init();
   }
}

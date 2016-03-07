
import squint.*;
import javax.swing.*;
import java.util.*;

/*
 * Class XMPPControl - organizes incoming data and creates main window and functions for that window
 * 
 * Alvaro Aleman
 */
public class XMPPControl extends GUIManager
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 200;
    
    //adjusts the size of the main chat area
    private final int CHAT_WIDTH = 65, CHAT_HEIGHT = 5; 
    
    //adjusts size of the text fields
    private final int FIELD_WIDTH = 20; 

    // Instance variables
    
    //Textfields for logging in 
    private JTextField password;    
    private JTextField account;
    
    //Main display area
    private JTextArea Chat;
    
    //Login and Logout buttons to coommunicate with server
    private JButton Login;
    private JButton Logout;
    
    //button to start chat with friend
    private JButton StartChat;
    
    //Combobox that stores all of your current contacts
    private JComboBox Friends; 
    
    //Panel to organiza layout
    private JPanel Mypanel;
    
    //establishes connection to server
    private GTalkConnection toGoogle;
    
    //variable used to grab current text from Textfield
    private String message; 
    
    //gives the fullID of google username
    private String fullID;
    
    //Declare hashmap that will keep track of windows
    private HashMap <String, ChatWindow> windowDictionary;
    
    //instance variable to keep track of incoming data stanza 
    private XMPPStanza s2; 
    
    //String to acquire name of friend   
    private String buddyname;
 
    //Variable to keep track of required window 
    private ChatWindow requestedWindow;
    
    //Constructor creates window and GUI components and sets the button states
    public XMPPControl() {
        // Create window to hold all the components
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
        
        
        /*Adds label for user to know where to place username
        Creates text field in which to input username
        Adds label for user to know where to place password
        Creates text field in which to input password  
        and groups everything together
         */
        
        JPanel mypane;
        mypane = new JPanel ();
        mypane.add (new JLabel( "User ID:" ));
        mypane.add (account = new JTextField (FIELD_WIDTH));        
        mypane.add( new JLabel ( " Password:" ));
        mypane.add( password = new JPasswordField (FIELD_WIDTH));
        contentPane.add(mypane);

        //Adds login and logout buttons and puts them together (also sets initial states)
        JPanel mypane2;
        mypane2 = new JPanel();
        mypane2.add (Login = new JButton ("Log in"));
        Login.setEnabled(true);
        mypane2.add (Logout = new JButton ("Log out"));
        Logout.setEnabled(false);
        contentPane.add(mypane2);
        
        //adds main display area and sets it to not be editable
        Chat = new JTextArea (CHAT_HEIGHT, CHAT_WIDTH);
        contentPane.add( new JScrollPane ( Chat) );
        Chat.setEditable(false);
        
        //adds Combobox that stores contacts and button to start chat
        JPanel mypane4; 
        mypane4 = new JPanel();
        mypane4.add(new JLabel ("Friends")); 
        mypane4.add(Friends = new JComboBox());
        Friends.addItem("------offline------");
        mypane4.add( StartChat = new JButton ("Start Chat"));
        StartChat.setEnabled(false);
        contentPane.add(mypane4);
        

    }

    /*
     *  Sets actions for each button clicked
     */
    public void buttonClicked( JButton Which ) {
        //creates conditions for when a button is clicked
        
        if(Which == Login){
            //connects to google server secure connection and attempts to log in
            System.out.print("\f");
            toGoogle = new GTalkConnection ( account.getText() , password.getText() );
            toGoogle.debugEnabled(true);

            fullID = toGoogle.fullJid();
            if(!fullID.equals("")){
                //conditions for when login is successful
                
                toGoogle.addMessageListener(this);

                Chat.setText("Login Successful!" + "\n" + "Choose a friend and start chatting!" + "\n");
                
                
                //Creates hasmap to keep track of wanted windows 
                windowDictionary = new HashMap <String, ChatWindow> ();
                
                //changes button states
                Login.setEnabled(false);
                Logout.setEnabled(true);
                StartChat.setEnabled(true);
                
                //changed textfield states
                account.setEditable(false);
                password.setEditable(false);
                

            }
            else{
                //conditions for when login is failed 
                Chat.setText("Incorrect Username or Password" );
                
                account.requestFocus();

            }

        }
        
        //terminates connection when logging out. 
        if(Which == Logout){
            //send the command to terminate connection 
            String LogoutMessage = "</stream:stream>";
            toGoogle.out.println(LogoutMessage);
            
            

        }
        
        
        //creates chat window for friend when start chat is pressed
        if(Which == StartChat){
        
            String friendname = Friends.getSelectedItem().toString().trim();
            
            getChatWindow(friendname);
        
        
        }
 

    }

    /*
     *  Respond to Enter key
     */
    public void textEntered(JTextField whichField ) {
        if(whichField == password) {
                Login.doClick();
            }

        if(whichField == account) {
                Login.doClick();
            }


    }

    //Sets actions for when connection is terminated 
    public void connectionClosed( ){
       
        Chat.setText("You have now logged out!"); 
        
        //closes all chat windows when connection is terminated
        for ( ChatWindow aWindow : windowDictionary.values() ) {
                aWindow.setVisible(false);
            
            }
         
         //Sets buttons and textfield back to initial states   
            Friends.removeAllItems();
            Friends.addItem("------offline------");
            
            account.setEditable(true);
            password.setEditable(true);
            
            
            account.setText("");
            password.setText("");
            
  
            Login.setEnabled(true);
            StartChat.setEnabled(false);
            Logout.setEnabled(false);
     
            //Sets cursor on account 
            account.requestFocus();

    }

    //Sets actions for when data packets are received from the google server
    public void dataAvailable( ) {
        //local variable declaration
        String jid = "item jid=";
        String stanza = toGoogle.in.nextStanza();
        XMPPStanza s2 = new XMPPStanza(stanza);
        
        
        //Sets action to retrieve complete contacts at initial login
        if(s2.isRoster()){

            String totalroster = s2.getRosterItems();
            
            //adds each contact to jcombobox
            while(totalroster.indexOf(jid)!=-1) {
                buddyname = totalroster.substring(totalroster.indexOf(jid) + jid.length() + 1, totalroster.indexOf("@gmail.com", totalroster.indexOf(jid)));

                Friends.addItem(buddyname);

                totalroster = totalroster.substring(totalroster.indexOf(jid) + 10);

            }

        }

        //sets action to retrive contacts that are online and place them on top of Combobox
        if (s2.isPresence()){
            Object previousSelection = Friends.getSelectedItem();
            buddyname = s2.getFromJid();
            Friends.removeItem(buddyname);
            //moves offline friends to bottom of combobox
            if(s2.getType().equals("unavailable")){        
                Friends.addItem(buddyname);
            }
            
            //moves online friends to the top of the combobox and selects to top name
            else{
                Friends.insertItemAt( buddyname, 0 );
                Friends.setSelectedItem( previousSelection );
            
            //makes sure your own name is not in combo box
            if(buddyname.equals(account.getText())){
                Friends.removeItem(buddyname);
            }
        }
      }


        //gets name of sender for incoming message and tells other class to display 
        if(s2.isMessage()){

           buddyname = s2.getFromJid().trim();
           getChatWindow(buddyname);
            
           requestedWindow.displayIncomingMessage(s2.getMessageBody());
           
           

        }
        
        
    }

    //This method retrieves or creates appropriate window for chat 
    private void getChatWindow( String JabberID){
    
         if(JabberID.equals("------offline------")){
                
            Chat.append("Invalid Friend!!!" + "\n");
            
         }
         else{
         requestedWindow = windowDictionary.get(JabberID);
        
         //creates new window if hasmap does not have JID of current friend
         if( requestedWindow == null){
            
             requestedWindow = new ChatWindow(account.getText(), JabberID , toGoogle );
             windowDictionary.put( JabberID , requestedWindow);
            
            }
         
            //Makes chat window visible if hashmap already contains current friends JID
         else{
            
             requestedWindow.setVisible(true);
            
            }
        }
    }

}

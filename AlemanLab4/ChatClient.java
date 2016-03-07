
import squint.*;
import javax.swing.*;

/*
 * Class ChatClient - Logs you into Google Chat server and acts as a chat client
 * 
 * Alvaro Aleman
 */
public class ChatClient extends GUIManager
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 500;
    
    //adjusts the size of the main chat area
    private final int CHAT_WIDTH = 65, CHAT_HEIGHT = 20; 
    
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
    
    //Field for message you want to send
    private JTextField Message;
    
    //button to send message
    private JButton SendMessage;
    
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
    
    //Constructor creates window and GUI components and sets the button states

    public ChatClient() {
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
        
        //adds Combobox that stores contacts
        JPanel mypane4; 
        mypane4 = new JPanel();
        mypane4.add(new JLabel ("Friends")); 
        mypane4.add(Friends = new JComboBox());
        Friends.addItem("------offline------");
        contentPane.add(mypane4);
        
        //Adds field and button to send wanted messages and groups them together
        JPanel mypane3; 
        mypane3 = new JPanel();
        mypane3.add(Message = new JTextField (FIELD_WIDTH));
        Message.setEditable(false);
        mypane3.add(SendMessage = new JButton("Send"));
        SendMessage.setEnabled(false);
        contentPane.add(mypane3);

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

                Chat.setText("Login Successful!" + "\n");
                
                //changes button states
                Login.setEnabled(false);
                Logout.setEnabled(true);
                SendMessage.setEnabled(true);
                
                //changed textfield states
                account.setEditable(false);
                password.setEditable(false);
                Message.setEditable(true);
                
                //places cursor on send message
                Message.requestFocus();

            }
            else{
                //conditions for when login is failed 
                Chat.setText("Incorrect Username or Password" );
                
                account.requestFocus();

            }

        }

        if(Which == Logout){
            //send the command to terminate connection 
            String LogoutMessage = "</stream:stream>";
            toGoogle.out.println(LogoutMessage);
            
            //Sets buttons and textfield back to initial states
            Friends.removeAllItems();
            Friends.addItem("------offline------");
            
            account.setEditable(true);
            password.setEditable(true);
            Message.setEditable(false);
            
            account.setText("");
            password.setText("");
            Message.setText("");
  
            Login.setEnabled(true);
            SendMessage.setEnabled(false);
            Logout.setEnabled(false);
     
            //Sets cursor on account 
            account.requestFocus();
            

        }

        if(Which == SendMessage){
            //conditions when SendMessage button is clicked
            String friend =Friends.getSelectedItem().toString();
            message = Message.getText();

            if(friend.equals("------offline------")){
                //Gives the user error message for offline in combobox 
                Chat.append("ERROR: Not a valid friend");
            }

            else {
                //Sends command to server with recipient and message
                String messageTag = "<message to=\"" + friend+"@gmail.com" + "\"" + " from=\"" + fullID +
                    "\"" + " type=\"chat\">" + 
                    "<body>"+ message + "</body>"+ "</message>";

                toGoogle.out.println(messageTag);

                Chat.append("me: " + message + "\n");
            }
            
            //places cursor on message textfield when one message is sent
            Message.setText("");
            Message.requestFocus();
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

        if(whichField == Message ){
                SendMessage.doClick();
            }
    }

    //Sets actions for when connection is terminated 
    public void connectionClosed( ){
       
        Chat.setText("You have now logged out!"); 

        Login.setEnabled(true);
        Logout.setEnabled(false);
        SendMessage.setEnabled(false);

        Login.requestFocus();

    }

    //Sets actions for when data packets are received from the google server
    public void dataAvailable( ) {
        //local variable declaration
        String stanza;
        String buddyname;
        String line;
        String from = "from";
        String rosterID = "jabber:iq:roster";
        String jid = "item jid=";
        String queryend = "</query>";
        String presence = "presence from=";

        stanza = toGoogle.in.nextStanza();
        
        //Sets action to retrieve complete contacts at initial login
        if(stanza.contains(rosterID)){

            String totalroster = stanza.substring(stanza.indexOf(jid), stanza.indexOf(queryend));
            while(totalroster.indexOf(jid)!=-1) {
                buddyname = totalroster.substring(totalroster.indexOf(jid) + jid.length() + 1, totalroster.indexOf("@gmail.com", totalroster.indexOf(jid)));

                Friends.addItem(buddyname);

                totalroster = totalroster.substring(totalroster.indexOf(jid) + 10);

            }

        }

        //sets action to retrive contacts that are online and place them on top of Combobox
        if (stanza.contains(presence) && (!stanza.contains("unavailable")))
        {
            buddyname = stanza.substring(stanza.indexOf(from) + from.length() + 2, stanza.indexOf("@gmail.com"));
            Friends.removeItem(buddyname);
            Friends.insertItemAt( buddyname, 0 );
            Friends.setSelectedItem( buddyname );

            if(buddyname.equals(account.getText())){
                Friends.removeItem(buddyname);
            }

        }

        String body = "body";

         //Sets actions to display incoming messages and replace "&amp" with "&", if "&amp" is detected
        if(stanza.contains("body") ){

            String friendsname = stanza.substring(stanza.indexOf(from) + from.length() + 2, stanza.indexOf("@gmail.com",stanza.indexOf(from) + from.length() + 2 ));
            if(!friendsname.equals(account.getText())){

                String amp = "&amp;";
               
                String newamp = "";

                if(stanza.contains(amp)){
                    
                    while(stanza.indexOf(amp)!=-1) {
                       
                       
                       stanza = stanza.substring(0, stanza.indexOf(amp)) + "& " + stanza.substring(stanza.indexOf(amp) + amp.length() + 1);  
                        
                                                                 
                    }   
                    String messagedelivery = stanza.substring(stanza.indexOf(body) + body.length() + 1, stanza.indexOf("</body>")).trim();
                    Chat.append(friendsname + ": " + messagedelivery + "\n"); 
                       
                }      
                else {String messagedelivery = stanza.substring(stanza.indexOf(body) + body.length() + 1, stanza.indexOf("</body>")).trim();
                    Chat.append(friendsname + ": " + messagedelivery + "\n"); 
                }      
                        
            }

        }

    }
}

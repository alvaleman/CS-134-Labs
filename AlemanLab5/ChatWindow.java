import squint.*;
import javax.swing.*;
import java.awt.*;

/**
 * Constructor for new chat window and displays incoming messages
 * 
 * Alvaro Aleman
 * 10/7/2014
 */
public class ChatWindow extends GUIManager
{
    // Sets window size
    private final int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 350;
    
    //Sets size for main chat area
    private final int CHAT_WIDTH = 45, CHAT_HEIGHT = 15;
    
    //Sets size for typing in message 
    private int TEXT_WIDTH = 35;
    
    //Area used to display main chat
    private JTextArea Chat;
    
    //Text field in which user inputs message to send
    private JTextField Message;
    
    //String used to assign ChatWindow parameter
    private String from; 
    
    //String to assign Chatwindow parameter
    private String friend;
    
    //Variable used to establish connection to google server
    private GTalkConnection GoogleConnect;

    /**
     * Constructs main window and GUI objects
     */
    public ChatWindow(String sender, String recipient, GTalkConnection toGoogle){
        //creates main window
        this.createWindow ( WINDOW_WIDTH, WINDOW_HEIGHT, "chat with " + recipient);
        
        //Enables border layout to place GUI objects in specific location in window
        this.setLayout( new BorderLayout());
        
        //Adds main chat area with line wrap and adjusts to window resizing 
        Chat = new JTextArea (CHAT_HEIGHT, CHAT_WIDTH);
        contentPane.add(new JScrollPane(Chat) , BorderLayout.CENTER);
        Chat.setLineWrap ( true );
        Chat.setEditable(false);
        
        //adds textfield to send wanted message
        JPanel mypane;
        mypane = new JPanel ();
        mypane.add(new JLabel ("Message:"));
        mypane.add(Message = new JTextField (TEXT_WIDTH));
        contentPane.add(mypane, BorderLayout.SOUTH); 
        
        //assigns parameters to instance variables
        from = sender;
        friend = recipient;
        GoogleConnect = toGoogle;
       
    }

    
    //Actions for when text is entered into send message field
    public void textEntered(){
        //gets friend's full Jid
        String fullID = GoogleConnect.fullJid();
        
        //retrieves message from text field
        String message = Message.getText();

        //adjusts message to replace symbols with appropriate code
        message = message.replaceAll( "&" , "&amp;" ); 
        
        message = message.replaceAll( ">" , "&lt;");
        
        message = message.replaceAll( "<" , "&gt;");
        
        //creates string to send to google connection with wanted message
        String messageTag = "<message to=\"" + friend+"@gmail.com" + "\"" + " from=\"" + fullID +
        "\"" + " type=\"chat\">" + "<body>"+ message + "</body>"+ "</message>";

        GoogleConnect.out.println(messageTag);
        
        //appends chat with message that was in textfield and sets textfield blank
        Chat.append(from+ ": " + Message.getText() + "\n");        
        Message.setText("");
        
    
    }

    //sets actions to display incoming messages
    public void displayIncomingMessage(String incomingmessage){
        
        //replaces code with appropriate symbols
        incomingmessage = incomingmessage.replaceAll( "&lt;", ">");
        
        incomingmessage = incomingmessage.replaceAll( "&gt;" , "<");
        
        incomingmessage = incomingmessage.replaceAll( "&amp;" , "&"); 
        
        //Appends chat with message sent by friend
        Chat.append(friend + ": " + incomingmessage + "\n");
           
    
    }
}

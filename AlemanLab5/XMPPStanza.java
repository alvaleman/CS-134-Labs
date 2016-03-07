
/**
 * XMPPStanza- Access to the fields of a packet received from Google XMPP server
 * 
 * Alvaro Aleman
 * 10/6/2014
 */
public class XMPPStanza
{
    // instance variables
    
    //refers to the text of the XML stanza represented by a particular object of this class
    private String s1;

    /**
     * Constructor for objects of class XMPPStanza
     */
    public XMPPStanza(String contents)
    {
        // initialise instance variables
        s1 = contents;
    }

    /**
     * Method that checks the presence of friend roster
     */
    public boolean isPresence()
    {
        
        return s1.startsWith("<presence");
    }

    //Checks stanza for roster of user's account
    public boolean isRoster()
    {
        return s1.startsWith("<iq") && s1.contains("jabber:iq:roster");
    
    }

    //Checks stanza for incoming message
    public boolean isMessage()
    {
        return s1.startsWith("<message") && s1.contains("<body>");
    
    }

    //Gets the type of stanza received
    public String getType()
    {
        if (s1.indexOf("type=") != -1 ){
            
            int typestart = s1.indexOf("type=");
            
            return s1.substring(typestart +6 , s1.indexOf("\"", typestart + 7 ));
        
        
        }
        else{
            return "";
        
        }
    }

    //Gets the Jid of the person who sent the message or presence
    public String getFromJid()
    {
        if(s1.indexOf("from=") != -1){
            
            return s1.substring(s1.indexOf("from") + 6, s1.indexOf("@gmail.com",s1.indexOf("from") ));
        
        
        }
        else{
            return ""; 
        
        }
    
    }


    //Gets the body of the message received from the stanza 
    public String getMessageBody()
    {
        String body = "<body>";
        
        if(s1.indexOf(body) != -1){
        
            
            
            return s1.substring(s1.indexOf(body) + body.length(), s1.indexOf("</body>")).trim();
        
        }
        
        else{
            return null;
        
        }
    
    }

    //gets all of the roster items from the stanza
    public String getRosterItems()
    
    {
       
        String jid = "jabber:iq:roster";
        String queryend = "</query>";
        
        if(s1.contains(jid)){
            
            
            
            return s1.substring(s1.indexOf(jid) + jid.length() + 1, s1.indexOf(queryend));
        
        }
        
        else{
        
            return null;
        
        }
        
    }
}

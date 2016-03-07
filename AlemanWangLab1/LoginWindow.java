
import squint.*;
import javax.swing.*;

/*
 * Class LoginWindow - construct dialog box
 * 
 * Alvaro Aleman and Victoria Wang
 */
public class LoginWindow extends GUIManager
{
   // Change these values to adjust the size of the program's window
   private final int WINDOW_WIDTH = 200, WINDOW_HEIGHT = 400;

 

    //creates text fields and buttons

    public LoginWindow() {
      // Create window to hold all the components
      this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
      contentPane.add( new JLabel( "Username:" ) );
      contentPane.add( new JTextField( 8 ) ) ;
      contentPane.add( new JLabel( "Password:" ) );
      contentPane.add( new JTextField( 8 ) );
      contentPane.add( new JButton( "Authenticate" ) );
   
    }

   /*
    *  when authenticate button is clicked, "login rejected" will appear
    */
    public void buttonClicked( ) {
       contentPane.add( new JLabel( "Login Rejected!" ) );
   
    }
   

   /*
    *  When text is entered into either password or username and the return button on the keyboard
    *  is pressed, "Login Accepted!" will appear.
    */
    public void textEntered( ) {
        contentPane.add( new JLabel ( "Login Accepted!" ));
    }
    

   
    
  
    public void menuItemSelected( ) {
       
    }
}

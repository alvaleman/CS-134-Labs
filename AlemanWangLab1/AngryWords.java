
import squint.*;
import javax.swing.*;

/*
 * Class AngryWords - create a knock-off version of Mad Libs
 * 
 * Alvaro Aleman and Victoria Wang
 */
public class AngryWords extends GUIManager
{
   // Change these values to adjust the size of the program's window
   private final int WINDOW_WIDTH = 400, WINDOW_HEIGHT = 200;
   private final int FINAL_WINDOW_WIDTH = 400, FINAL_WINDOW_HEIGHT = 150;

   private JTextField noun1;
   private JTextField adj1;
   private JTextField noun2;
   private JTextField adj2;
   private JTextField verb1;
   private JTextField adj3;
   private JTextField noun3;
   

    //declaring the names

    public AngryWords() {
      // Create window to hold all the components
      this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );
      noun1 = new JTextField( 10);
      contentPane.add ( new JLabel ( "NOUN" ) );
      contentPane.add ( noun1 );
      adj1 = new JTextField (10);
      contentPane.add ( new JLabel ( "ADJECTIVE" ) );
      contentPane.add ( adj1 ); 
      noun2 = new JTextField (10);
      contentPane.add ( new JLabel ( "NOUN" ) );
      contentPane.add ( noun2 ); 
      adj2 = new JTextField (10);
      contentPane.add ( new JLabel ( "ADJECTIVE" ) );
      contentPane.add ( adj2 ); 
      verb1 = new JTextField (10);
      contentPane.add ( new JLabel ( "VERB  " ) );
      contentPane.add ( verb1 ); 
      adj3 = new JTextField (10);
      contentPane.add ( new JLabel ( "ADJECTIVE" ) );
      contentPane.add ( adj3 ); 
      noun3 = new JTextField (10);
      contentPane.add ( new JLabel ( "NOUN" ) );
      contentPane.add ( noun3 ); 
      contentPane.add( new JButton ( "Compose" ) );
      
   
      // adding text fields and their descriptions
    }

   /*
    *  takes entered text and puts it into the madlib
    */
    public void buttonClicked( ) {
        this.setSize( FINAL_WINDOW_WIDTH, FINAL_WINDOW_HEIGHT); 
        contentPane.removeAll();
        this.repaint();
        contentPane.add( new JLabel( "Remember to bring your " + noun1.getText() + " to computer science class." ) );
        contentPane.add( new JLabel( "Make sure it's a " + adj1.getText() + " one." ) );
        contentPane.add( new JLabel( "Now, we don't mean your " + noun2.getText() + "." ) );
        contentPane.add( new JLabel( "If you bring that, then Bill and Brent will get very " + adj2.getText() + "." ) );
        contentPane.add( new JLabel( "So " + verb1.getText() + " to class." ) );
        contentPane.add( new JLabel( "Soon you'll be a " + adj3.getText() + " " + noun3.getText() + "!" ) );
    }
   

   
}

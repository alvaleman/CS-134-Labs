
import squint.*;
import javax.swing.*;

/*
 * Class WordList - Keeps track of currently accepted words using a recursive method. 
 * 
 * Alvaro Aleman and Lia Lee
 */
public class WordList 
{
   // Is this an empty word element
   private boolean empty;
   
   // New word being added to the list
   private String first;
   
   // Remaining words in the list
   private WordList next;
   
   //Initial empty wordlist
    public WordList() {
      empty = true;
    }

    // Creating a new word list
    public WordList( String word, WordList existing ) {
        first = word;
        next = existing;
        empty = false;
    }
   
    // Determine whether the list is empty
    public boolean isEmpty() {
       return empty;
    }
   
    // Adding a new word to the list
    public void add( String addWord ) {
        if ( empty ) {
            first = addWord;
            empty = false;
            next = new WordList();
            
        } else {
            next.add ( addWord );
        }
    }
    
   // Checking to see if WordList already contains checked word
    public boolean contains( String word ) {
        if ( empty ) {
            return false;
        } else if ( word.equals( first ) ) {
            return true;
        } else {
            return next.contains( word );
        }
    }
    
    // Produce a string containing all accepted words
    public String toString() {
        if ( empty ) {
            return "";
        } else {
            return first + "\n" + next;
        }
    }
    
    //Method to determine score for a given word 
    public int determineScore( String word ){
        int Score = 0;
        
        if ( word.length() == 3 || word.length() == 4 ) {
            Score = 1;
        } else if ( word.length() == 5 ) {
            Score = 2;
        } else if (word.length() == 6 ) {
            Score = 3;
        } else if (word.length() == 7 ) {
            Score = 5;
        } else {
            Score = 11;
        }
        
        return Score;
    }

    
}

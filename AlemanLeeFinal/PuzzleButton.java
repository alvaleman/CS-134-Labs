import javax.swing.JButton;

/*
 * Class PuzzleButton -  A version of JButton specialized to serve as elements of a puzzle. 
 * 
 * Alvaro Aleman and Lia Lee
 */

public class PuzzleButton extends JButton {

    // Remember the location of this button in the puzzle grid
    private int row;
    private int column;

    // Remember letter(s) inside of the button
    private String text;
    
    // Create a new button
    public PuzzleButton( int row, int column, String text ) {
        this.row = row;
        this.column = column;
        this.text = text;
        this.setText(text);

    }

    // Return the row number of this button
    public int getRow() {
        return row;
    }

    // Return the column number of this button
    public int getColumn() {
        return column;
    }

    // Return the text inside the button
    public String getLetter() {
        return text;
    }

    // Return true if other button is adjacent to this button horizontally or vertically
    public boolean isAdjacentTo( PuzzleButton other) {
        return other != this && (
            Math.abs( row - other.getRow() ) + Math.abs( column - other.getColumn() ) == 1 
            || (Math.abs(  row - other.getRow() ) + Math.abs( column - other.getColumn() ) == 2 &&
                (Math.abs( row - other.getRow()) == 1 || Math.abs( column - other.getColumn()) == 1 )));
    }
}
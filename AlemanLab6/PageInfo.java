/*
 * Class PageInfo - Holds information about the links to and from a single page
 * 
 */
public class PageInfo {

    // The web address of the page this object describes
    private Url url;
    
    // Counts of the numbers of links to and from this page
    private int linkToCount;
    private int linkFromCount;
    
    // Whether the page has been visited (the linkFromCount is meaningless if visited is false)
    private boolean visited;

    // Initialize a new PageInfo object for the specified Url
    public PageInfo( Url url) {
        // initialize instance variables
        this.url = url;
        linkToCount = 0;
        linkFromCount = 0;
    }
    
    // Get the Url for the page described
    public Url getUrl() {
        return url;
    }

    // Add one to the counts of links found to this page.
    public void increaseLinksTo() {
        linkToCount = linkToCount + 1;
    }

    // Add one to the counts of links found from this page.
    public void increaseLinksFrom() {
        linkFromCount = linkFromCount + 1;
    }

    // Get the number of links found to this page
    public int linksTo() {
        return linkToCount;
    }

    // Get the number of links found from this page (if it has been visited)
    public int linksFrom() { 
        return linkFromCount;
    }
    
    // Record the fact that the program has scanned the HTML of this page.
    public void setVisited() {
        visited = true;
    }
    
    // Produce a string summarizing the characteristics of this web page
    public String toString() {
        if ( visited ) {
            return url + " has " + linksTo() + " references and contains " + linksFrom() + " links";
        } else {
            return url + " has " + linksTo() + " references";
        }
    }
}

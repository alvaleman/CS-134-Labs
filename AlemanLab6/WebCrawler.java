import squint.*;
import javax.swing.*;
import java.awt.*;

/*
 * Class WebCrawler - Roams the pages of a web site counting pages, links and servers
 * 
 */
public class WebCrawler extends GUIManager
{
    // Change these values to adjust the size of the program's window
    private final int WINDOW_WIDTH  = 900, WINDOW_HEIGHT = 500;

    // Limit on number of pages to fetch per minute (DO NOT MAKE THIS BIGGER THAN 60)
    private final int FETCH_RATE = 60;

    // Allows user to enter a starting address or default to CS department home page
    private JTextField startingUrlField = new JTextField( "http://www.cs.williams.edu", 20 );

    // Label used to display counts of pages visited and found
    private JLabel UrlCount = new JLabel("", SwingConstants.CENTER );

    // Text areas used to display information about pages found and host names found
    private final int URL_DISPLAY_HEIGHT = 10, HOST_DISPLAY_HEIGHT = 10;
    private final int DISPLAY_WIDTH = 70;
    private JTextArea urlDisplay = new JTextArea( URL_DISPLAY_HEIGHT, DISPLAY_WIDTH );
    private JTextArea hostDisplay = new JTextArea( HOST_DISPLAY_HEIGHT, DISPLAY_WIDTH );

    // Buttons used to start, step, and stop the crawling process
    private JButton startButton = new JButton( "Start At:");
    private JButton fetchButton = new JButton( "Fetch next");
    private JButton autoPilot = new JButton( "Fetch Pages Automatically" );

    // All of the pages we have found.
    private PageList pagesFound = new PageList( );

    // All of the URLs of pages we have found but not yet visited
    private UrlList unvisitedUrls = new UrlList( );

    // All of the servers we have found in the links we have found
    private HostList hostsFound = new HostList( );

    // The next page we should visit
    private Url toFetch;

    // The number of pages that have been visited.
    private int visited;

    // Object that actually fetches a page given its URL
    PageGrabber fetcher = new PageGrabber();

    // Timer used to pace process of visiting pages when running automatically
    PaceMaker timer;

    
    /*
     *  Produce an interface that enables user to activate or suspend the search for
     *  pages and displays statistics when the search is paused or complete.
     */
    public WebCrawler() {
        // Create window to hold all the components
        this.createWindow( WINDOW_WIDTH, WINDOW_HEIGHT );

        // Use a border layout for main window with empty labels on edges for a nicer look
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout( mainLayout);
        mainLayout.setVgap( 2 );
        mainLayout.setHgap( 4 );
        contentPane.add( new JLabel(), BorderLayout.EAST );
        contentPane.add( new JLabel(), BorderLayout.WEST );
        contentPane.add( new JLabel(), BorderLayout.SOUTH );

        // Place the control buttons and visit count in a grid at the top of the window
        JPanel controlPane = new JPanel( new GridLayout( 3, 1 ) );

        JPanel curPane = new JPanel();
        curPane.add( startButton );
        curPane.add( startingUrlField );
        autoPilot.setEnabled( false );
        curPane.add( autoPilot );
        controlPane.add( curPane );

        controlPane.add( UrlCount );

        fetchButton.setEnabled( false );
        controlPane.add( fetchButton );

        contentPane.add( controlPane, BorderLayout.NORTH );

        // Put scrollable text areas displaying URL and host lists vertically stacked
        // in the window with their vertical share of the window adjustable by the user.
        Dimension minimumSize = new Dimension(100, 50);

        JPanel urlPanel = new JPanel( new BorderLayout() );
        urlPanel.add( new JLabel( "Pages found within site", SwingConstants.CENTER ), BorderLayout.NORTH );
        urlPanel.add( new JScrollPane( urlDisplay ), BorderLayout.CENTER  );
        urlPanel.setMinimumSize(minimumSize);

        JPanel hostPanel = new JPanel( new BorderLayout() );
        hostPanel.add( new JLabel( "Hosts found within site", SwingConstants.CENTER ), BorderLayout.NORTH);
        hostPanel.add( new JScrollPane( hostDisplay ), BorderLayout.CENTER  ); 
        hostPanel.setMinimumSize(minimumSize);  

        JSplitPane textAreaPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, urlPanel, hostPanel);

        contentPane.add( textAreaPane, BorderLayout.CENTER );
        textAreaPane.setDividerLocation(0.70);

    }

    
    
    // Repond appropriately to the buttons to control the crawling process
    public void buttonClicked( JButton which ) {
        if ( which == startButton ) {
            // The start button allows the user to specify where the search should start
            // To achieve this we put the initial URL in the Visit button and add it to
            // the page and host lists.
            toFetch = new Url( startingUrlField.getText() );
            fetchButton.setText( "Visit " + toFetch );
            PageInfo firstPage = new PageInfo( toFetch );
            pagesFound = new PageList( firstPage, pagesFound );
            HostInfo firstHost = new HostInfo( toFetch.getHost() );
            hostsFound = new HostList( firstHost, hostsFound );
            firstHost.increasePageCount();

            startButton.setEnabled( false );
            fetchButton.setEnabled( true );
            autoPilot.setEnabled( true );

        } else if ( which == fetchButton ) {
            // Visit just one page from the unvisited list when the fetch button is pressed
            visitOnePage();
            
            // See if we have run out of pages
            if ( toFetch == null ) {
                fetchButton.setEnabled( false );
                autoPilot.setEnabled( false );
            }
            updateStats();
        } else {
            // When the "auto" button is pressed, either start or stop the ongoing crawling process
            if ( timer == null ) {
                // If there is no timer, start one to drive the crawling process
                autoPilot.setText( "Pause Crawling" );
                fetchButton.setEnabled( false );
                timer = new PaceMaker( 60.0/FETCH_RATE , this);
            } else {
                // Pause the crawling process and update the statistics displayed.
                autoPilot.setText( "Fetch Pages Automatically" );
                fetchButton.setEnabled( true );
                timer.stop();
                timer = null;
                updateStats();
            }

        }
    }

    
    
    // While in automatic mode, this method's code is executed periodically visiting one page each time
    public void tick() {
        visitOnePage();

        // See if we have run out of pages
        if ( toFetch == null ) {
            timer.stop();
            timer = null;
            autoPilot.setEnabled( false );
        }
    }

    
    
    // Update the display components to reflect the most recent versions of the statistics
    // gathered by the program
    private void updateStats() {

        // Display the list of all pages found
        urlDisplay.setText( pagesFound.toString() );

        // Display the summary statistics about pages
        urlDisplay.append( "\n\nMost referenced page is " + pagesFound.mostReferenced() + "\n" );
        
        // Note: The 0.0000001's in the next two lines ensure that divide by zero does not occur
        // and tell Java that a non-integer result is expected.
        urlDisplay.append( "Average links from each page = " + pagesFound.totalLinksFrom()/(visited  + 0.0000001)+ "\n" );
        urlDisplay.append( "Average links to each page = " + pagesFound.totalLinksTo()/(pagesFound.size() + 0.0000001) + "\n" );

        // Display the list of all servers found
        hostDisplay.setText( hostsFound.toString() );
    }

    
    
    // Visit one page from the unvisited list 
    private void visitOnePage() {

        // Request the text of the page's HTML
        String pageHTML = fetcher.get(toFetch);

        // If HTML for the page is received, scan it for links to other pages while updating our
        // lists of hosts and pages
        if ( pageHTML != null ) {
            getUrls( pageHTML, toFetch );
        } 

        // Update the count of pages visited
        visited = visited + 1;
        UrlCount.setText( visited + " of " + pagesFound.size() + " page(s) visited.");

        // Determine the next page to visit or disable crawling if all pages found have been visited.
        if ( ! unvisitedUrls.empty() ) {
            toFetch = unvisitedUrls.getFirst();
            unvisitedUrls = unvisitedUrls.removeFirst();
            fetchButton.setText( "Visit " + toFetch );
        } else {
            toFetch = null;
            fetchButton.setText( "DONE" );
        }   

    }
    
    
    
    // Scan through the HTML source code describing a web page to find and record anly links to other
    // pages within the cs.williams.edu web presence
    public void getUrls( String source, Url site ) {
        // Create a list to keep track of all the Urls included in links in this page
        UrlList pagesLinks = new UrlList();

        // Retrieve the page descriptor from the pagesFound list
        PageInfo sourceInfo = pagesFound.get( site );
        sourceInfo.setVisited();

        // Make a lower case version of the page so that both HREF and href will look the same
        String lower = source.toLowerCase();

        // Look for the first link
        int linkPos = lower.indexOf( "href=\"" );

        while ( linkPos >= 0 ) {
            linkPos = linkPos + "href=\"".length();

            // Look for the end of the Url in the current link
            int linkEnd = lower.indexOf( "\"", linkPos );
            if ( linkEnd >= 0 ) {
                String link = source.substring( linkPos, linkEnd );

                // Remove any anchors from the end of Urls so they won't be treated as separate pages
                if ( link.contains( "#" ) ) {
                    link = link.substring( 0, link.lastIndexOf( "#" ));
                }

                // Create a Url out of the text of the link since this enable us to handle relative and
                // absolute links consistently
                Url newUrl = new Url( site, link );

                // If this is a link to another CS document that has not already been linked from this page record it.
                if ( newUrl.getProtocol().equals( "http" ) &&  newUrl.getHost().endsWith( "cs.williams.edu" ) &&
                              ! pagesLinks.contains(newUrl) ) {
                    sourceInfo.increaseLinksFrom();
                    updatePagesFound( newUrl );

                    pagesLinks = new UrlList( newUrl, pagesLinks );
                }
            } 

            // Find the next link
            linkPos = lower.indexOf( "href=\"" , linkPos );
        }
    }

    
    
    // When a link is found, this method will either add it to the pagesFound list or update the count
    // of the number of times it has been found
    private void updatePagesFound( Url newUrl ) {
        
        // Try to get the page's descriptor from the pagesFound list
        PageInfo info = pagesFound.get( newUrl );
        if ( info == null ) {
            // If it was not in the pagesFound list, add it to the list of pages that need to be visited,
            // and see if its host should be added to the host list
            unvisitedUrls = new UrlList( newUrl, unvisitedUrls );

            info = new PageInfo( newUrl );
            pagesFound = new PageList( info, pagesFound );

            updateHostsFound( newUrl );

        } 
        info.increaseLinksTo();
    }

    // When the first reference to a page is found, this method will either add its host's name to the
    // hostsFound list or increase the count of the number of pages found on that host.
    private void updateHostsFound( Url newUrl ) {            
        
        // Try to get the host's descriptor from the hostsFound list
        String hostName = newUrl.getHost();
        HostInfo host = hostsFound.get( hostName );
        
        if ( host == null ) {
            // If the host was not already in the list, add it.
            host = new HostInfo( hostName );
            hostsFound = new HostList( host, hostsFound );
        } 
        host.increasePageCount();
    }

    // Make sure that we stop crawling if the user closes the program window.
    public void windowClosing() {
        if ( timer != null ) {
            timer.stop();
            timer = null;
        }
    }

}

/*
 * Class HostInfo - Holds a web server's name and a count of the number of pages it hosts
 * 
 */
public class HostInfo {

    // The server's domain name
    private String address;
    
    // How many pages have been found that are provided by this server
    private int pageCount;

    /*
     * Construct a new host description
     */
    public HostInfo( String address) {
        // initialise instance variables
        this.address = address;
        pageCount = 0;
    }
    
    
    // Get the host's address
    public String getAddress() {
        return address;
    }

    
    // Add one to the number of pages stored by this host
    public void increasePageCount() {
        pageCount = pageCount + 1;
    }
    
    
    // Produce a string describing this web server
    public String toString() {
        return "Host " + address + " holds " + pageCount + " pages";
    }

}

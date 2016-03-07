/*
 * Class HostList - Represent a list of web servers and counts of their documents
 * Alvaro Aleman
 * 10/21/201
 */
public class HostList {
    
    private boolean empty;
    
    private HostInfo first;
    
    private HostList rest;
    
    // Create an empty list of host descriptors
    public HostList() {
        empty = true;
    }
    
    // Create a new list by adding info about one page to an existing list
    public HostList( HostInfo info, HostList existing) {
        empty = false; 
        first = info;
        rest = existing;     
    }
        
    // Try to get the object describing a host with the given address. This method
    // will return null if no description of the specified host is in the list
    public HostInfo get( String address ) {
         if ( empty ) {
            return null;
        } else if ( address.equals( first.getAddress() ) ) {
            return first;
        } else {
            return rest.get( address );
        }
    }
    
    // Produce a string containing short descriptions of all of the hosts in the list     
    public String toString() {
        if ( empty ) {
            return "";
        } else {
            return first + "\n" + rest;
        }
    }
}

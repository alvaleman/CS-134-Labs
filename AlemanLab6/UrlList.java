/*
 * Class HostList - Represent a list of Uniform Resource Locators (Urls)
 * alvaro aleman 
 */
public class UrlList {
    
    // Is this an empty list
    private boolean isEmpty;
    
    // The first Url in this list
    private Url first;
    
    // The remaining members of the list
    private UrlList rest;
    
    
   // Create an empty list of Urls
    public UrlList() {
       isEmpty = true;
    }
    
    // Create a new list by adding a new Url to an existing list
    public UrlList( Url newUrl, UrlList existing ) {
       isEmpty = false;
       first = newUrl;
       rest = existing;
    }
    
    
    // Determine whether the list is empty
    public boolean empty() {
        return isEmpty;
    }
    
    
    // Determine whether the list contains a specific item
    public boolean contains( Url item ) {
        return ! isEmpty && (item.equals( first ) || rest.contains( item ));
    }
    
    
    // Get the first element of the list
    public Url getFirst() {
        return first;
    }
    
    
    // Get the list obtained by removing the first element from this list
    public UrlList removeFirst() {
        return rest;
    }
    
    
}

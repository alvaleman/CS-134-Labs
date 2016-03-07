/*
 * Class PageList - Represent a list of pages and counts of their incoming and outgoing links
 * 
 */
public class PageList {

    // Is this an empty list
    private boolean empty;

    // Information about the first page in the list
    private PageInfo first;

    // The remaining members of the list
    private PageList rest;

    // Create an empty list of page descriptors
    public PageList() {
        empty = true;
    }

    // Create a new list by adding info about one page to an existing list
    public PageList( PageInfo info, PageList existing ) {

        first = info;
        rest = existing;
        empty = false;
    }

    // Determine whether this list is empty
    public boolean isEmpty() {
        return empty;
    }

    // Try to get the object describing a page with the given Url. This method
    // will return null if no description of the specified page is in the list
    public PageInfo get( Url page ) {
        if ( empty ) {
            return null;
        } else if ( page.equals( first.getUrl() ) ) {
            return first;
        } else {
            return rest.get( page );
        }
    }

    // Determine the number of pages described by this list
    public int size() {

        if (empty) {
            return 0;
        }
        return 1 + rest.size();

    }

    // Determine the total number of incoming links found in all of the
    // pages in the list
    public int totalLinksTo() {
        if (empty) {
            return 0;
        }
        return first.linksTo() + rest.totalLinksTo();

    }

    // Determine the total number of outgoing links found to all of the
    // pages in the list
    public int totalLinksFrom() {

        if (empty) {
            return 0;
        }
        return first.linksFrom() + rest.totalLinksFrom();

    }

    // Find the page in the list to which there are the most incoming links
    public PageInfo mostReferenced() {

        if (rest.isEmpty()) {
            return first;
        }
        PageInfo mostRest = rest.mostReferenced();
        if (mostRest.linksTo() < first.linksTo()) {
            return first;
        }
        return mostRest;
    }

    // Produce a string containing short descriptions of all of the pages in the list
    public String toString() {
        if ( empty ) {
            return "";
        } else {
            return first + "\n" + rest;
        }
    }

}

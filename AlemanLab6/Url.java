import java.net.MalformedURLException;
import java.net.URL;

// This class mimics the standard Java URL class but handles all exceptions internally
public class Url {

    // The underlying URL
    private java.net.URL addr = null;

    // The subcomponents of the URL
    private String host = "";
    private String path = "";
    private String full = "";
    private String protocol = "";

    // Builds a URL for a non-relative Url.
    private static URL makeURL( String addr ) {
        try {
            return new java.net.URL( addr );
        } catch ( Exception e ) {
            return null;
        }
    }

    // Builds a URL for a possibly relative Url.
    private static URL makeURL( Url base, String addr ) {
        try {
            return new java.net.URL( base.addr, addr );
        } catch ( Exception e ) {
            return null;
        }
    }

    // Package an existing URL as a Url object.
    public Url( java.net.URL address ) {
        addr = address;
        if ( addr != null ) {
            host = addr.getHost();
            path = addr.getPath();
            full = addr.toString();
            protocol = addr.getProtocol();
        }
    }

    // Construct a new Url from a single string
    public Url( String Url ) {
        this( makeURL( Url ) );
    }

    // Construct a new Url from a string and a Url for the page containing the string
    public Url( Url context, String Url )
    {
        this( makeURL( context, Url) );
    }

    // Return the host component of a URL
    public String getHost() {
        return host;
    }

    // Return the path component of a URL
    public String getPath() {
        return path;
    }

    // Return the protocol component of a URL
    public String getProtocol() {
        return protocol;
    }
    
    // Return a String representation of a complete URL.
    public String toString() {
        return full;
    }
    
    // Determine if this Url equals another object
    public boolean equals( Object other ) {
        return full.equals(other.toString() ); 
    }

}

import squint.NetConnection;

// A class that provides an easy way to fetch a web page
public class PageGrabber {

    // The default HTTP port number
    private  int HTTP_PORT = 80;

    // A list of web servers that we should not try to contact
    private  String badHosts = " csdoc.cs.williams.edu\n";

    // Construct a new PageGrabber
    public PageGrabber() {
        // There really isn't anything for the constructor to do

    }

    // Fetch the web page at the address provided by urlToGet
    public String get( Url urlToGet ) {

        if ( ! badHosts.contains( " " + urlToGet.getHost() + "\n" ) ) {
            // The Internet is a dangerous place, so try to fetch a page but return
            // null if the attempt fails
            try { 

                // Establish a connection to the server named in the Url
                NetConnection toServer = new NetConnection( urlToGet.getHost(), HTTP_PORT );

                // Make sure the path is not empty
                String path = urlToGet.getPath();
                if ( path.length() == 0 ) {
                    path = "/";
                }

                // Send a request to the server designed to avoid anything that isn't ASCII
                toServer.out.println( "GET " + path + " HTTP/1.0" );
                toServer.out.println( "Host: " + urlToGet.getHost() );
                toServer.out.println( "Accept: text/plain, text/html" );
                toServer.out.println( "Accept-Encoding: identity" );
                toServer.out.println( "" );

                // For large HTML documents, a StringBuffer is more efficient than a String
                StringBuffer response = new StringBuffer();

                // Need to keep track of whether we are still looking at header lines
                // and have seen one that indicates this is an HTML document
                boolean inHeader = true;
                boolean isHTML = false;

                // Process repsonse lines as they arrive examining relevant headers and collecting body
                while ( toServer.in.hasNextLine() && (inHeader || isHTML ) ) {
                    String line = toServer.in.nextLine();
                    if ( inHeader ) {
                        String lower = line.toLowerCase();
                        inHeader = lower.length() > 0;
                        if ( lower.startsWith( "content-type:" ) ) {
                            isHTML = lower.contains( "text/html" );
                        }
                    } else {
                        response.append( line + "\n");
                    }
                }
                toServer.close();

                // Return parsed document if response was identified as HTML
                if ( isHTML ) {
                    return response.toString();
                } else {
                    return null;
                }

            } catch (IllegalStateException e ) {
                // If we are unable to contact a server once, put it on our list of hosts to avoid
                badHosts = " " + urlToGet.getHost() + "\n" + badHosts;
                return null;
            } catch ( Exception e ) {
                System.out.println( e );
                return null;
            }
        } else {
            return null;
        }
    }
}

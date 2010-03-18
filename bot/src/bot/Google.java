package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class Google {

    public String search(String query) throws Exception {
        // Set a text only USER_AGENT string, since google returns 443 with the default java UA.
        System.setProperty("http.agent", "Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");

        String sourceUrlString = "http://www.google.com/search?q=" + query.replaceAll(" ", "%20");

        Source source = new Source(new URL(sourceUrlString).openStream());
        // Parse the entire page right away.
        source.fullSequentialParse();

        // The first search result is in the class named "r".
        Element firstResultElement = source.getFirstElementByClass("r");
        // Fetch it and remove all the annoying newline characters and clean up.
        return firstResultElement.getRenderer().toString().replaceAll("\\n", " ").replaceAll("<", "").replaceAll(">", "").replaceAll("\\r", ":");
    }
}

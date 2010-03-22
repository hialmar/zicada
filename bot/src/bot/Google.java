package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class Google {

    HelperClass helper = new HelperClass();

    public String search(String query) throws Exception {
        // Set a text-only browser USER_AGENT string, since google returns 443 with the default java UA.
        System.setProperty("http.agent", "Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");

        if (query.isEmpty()) {
            return "Usage: !google <query>";
        } else {
            query = helper.htmlEnc(query);
            String sourceUrlString = "http://www.google.com/search?q=" + query;

            Source source = new Source(new URL(sourceUrlString).openStream());
            // Parse the entire page right away.
            source.fullSequentialParse();

            // The first search result is in the class named "r".
            Element firstResultElement = source.getFirstElementByClass("r");
            // Fetch it and remove all the annoying newline characters by using the HelperClass.
            return helper.stripNewLine(firstResultElement.getRenderer().toString());

        }
    }
}

package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class TelefonKatalogen {

    public String getAddress(String query) throws Exception {

        String sourceUrlString = "http://www.gulesider.no/tk/search.c?q=" + query;
        Source source = new Source(new URL(sourceUrlString).openStream());
        // Parse the entire page right away.
        source.fullSequentialParse();
        // The first search result is in the class named "r".
        Element firstResultElement = source.getFirstElementByClass("address");
        // Fetch it and remove all the annoying newline characters and clean up.
        return firstResultElement.getRenderer().toString().replaceAll("\\n", "").replaceAll("\\r", "");
    }

        public String getNumber(String query) throws Exception {
        String sourceUrlString = "http://www.gulesider.no/tk/search.c?q=" + query.replaceAll(" ", "+");
        Source source = new Source(new URL(sourceUrlString).openStream());
        // Parse the entire page right away.
        source.fullSequentialParse();
        // The first search result is in the class named "r".
        Element firstResultElement = source.getFirstElementByClass("number");
        // Fetch it and remove all the annoying newline characters and clean up.
        String result = firstResultElement.getRenderer().toString().replaceAll("\\n", "").replaceAll("\\r", "");
        return result.substring(0, 12);
    }

}
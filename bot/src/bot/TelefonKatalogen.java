package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class TelefonKatalogen {

    HelperClass helper = new HelperClass();

    public String getTlfData(String query) throws Exception {

        if (query.isEmpty()) {
            return "Usage: !tlf <name/number/address>";
        } else {
            query = helper.htmlEnc(query);
            String sourceUrlString = "http://www.gulesider.no/tk/search.c?q=" + query;
            Source source = new Source(new URL(sourceUrlString).openStream());

            // Parse the entire page right away.
            source.fullSequentialParse();

            Element numberElement = source.getFirstElementByClass("number");
            Element addressElement = source.getFirstElementByClass("address");
            Element nameElement = source.getFirstElementByClass("name");

            // Make sure we have anything to return
            if (source.getTextExtractor().setIncludeAttributes(true).toString().contains("ingen treff")) {
                return "No results available";
            } else {

                // number and address tend to have \r and \n, strip using helper.
                String number = helper.stripNewLine(numberElement.getRenderer().toString().substring(0, 12));
                String address = helper.stripNewLine(addressElement.getRenderer().toString());
                String name = nameElement.getRenderer().toString();

                // some formatting, could use improvement
                return number + ", " + name.substring(1, name.indexOf("MER") - 1) + "," + address;
            }
        }
    }
}

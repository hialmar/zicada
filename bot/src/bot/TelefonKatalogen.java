package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class TelefonKatalogen {

    HelperClass helper = new HelperClass();

    public String getNumber(String query) throws Exception {

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

            if (source.getTextExtractor().setIncludeAttributes(true).toString().contains("ingen treff")) {
                return "No results available";
            } else {
                String number = helper.stripNewLine(numberElement.getRenderer().toString().substring(0, 11));
                String address = helper.stripNewLine(addressElement.getRenderer().toString());
                String name = nameElement.getRenderer().toString();
                return number + ", " + name.substring(1, name.indexOf("MER") - 1) + "," + address;
            }
        }
    }
}

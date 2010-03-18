package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class TelefonKatalogen {

    public String getNumber(String query) throws Exception {
        if (query.isEmpty()) {
            return "Usage: !tlf <name/number/address>";
        } else {
            // Encode norwegian characters
            String sourceUrlString = "http://www.gulesider.no/tk/search.c?q=" + query.toLowerCase().replaceAll(" ", "%20").replaceAll("ø", "%F8").replaceAll("æ", "%E6").replaceAll("å", "%E5");
            Source source = new Source(new URL(sourceUrlString).openStream());
            // Parse the entire page right away.
            source.fullSequentialParse();

            Element numberElement = source.getFirstElementByClass("number");
            Element addressElement = source.getFirstElementByClass("address");
            Element nameElement = source.getFirstElementByClass("name");
            if (source.getTextExtractor().setIncludeAttributes(true).toString().contains("ingen treff")) {
                return "No results available";
            } else {
                String number = numberElement.getRenderer().toString().replaceAll("\\n", "").replaceAll("\\r", "").trim().substring(0, 11);
                String address = addressElement.getRenderer().toString().replaceAll("\\n", "").replaceAll("\\r", "").trim();
                String name = nameElement.getRenderer().toString().replaceAll("\\n", "").replaceAll("\\r", "");
                return number + ", " + name.substring(1, name.indexOf("MER")-1) + ", " + address;
            }
        }
    }
}

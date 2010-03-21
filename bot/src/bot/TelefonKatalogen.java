package bot;

import net.htmlparser.jericho.*;
import java.net.*;

public class TelefonKatalogen {

    private String name;
    private String number;
    private String address;
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

            // Make sure we have anything to return
            if (source.getTextExtractor().setIncludeAttributes(true).toString().contains("ingen treff")) {
                return "No results available";

            // Handle things differently if we're sent to gule sider bedrift
            } else if (source.getTextExtractor().setIncludeAttributes(true).toString().contains("Treff i firmanavn")) {

                Element numberElement = source.getFirstElementByClass("mainTlf");
                Element addressElement = source.getFirstElementByClass("mainAdr");

                number = helper.stripNewLine(numberElement.getRenderer().toString());
                address = helper.stripNewLine(addressElement.getRenderer().toString());
                name = "Business";

            // Default case, retrieve info
            } else {
                Element numberElement = source.getFirstElementByClass("number");
                Element addressElement = source.getFirstElementByClass("address");
                Element nameElement = source.getFirstElementByClass("name");

                number = helper.stripNewLine(numberElement.getRenderer().toString().substring(0, 12));
                address = helper.stripNewLine(addressElement.getRenderer().toString());
                name = nameElement.getRenderer().toString();
                name = name.substring(1, name.indexOf("MER") - 1);

            }
            return number + ", " + name + "," + address;
        }
    }
}

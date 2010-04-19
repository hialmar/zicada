package bot;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

// TODO: Auto-generated Javadoc
/**
 * The Class TelefonKatalogen.
 */
public class TelefonKatalogen {

	/** The name. */
	private String name;
	
	/** The number. */
	private String number;
	
	/** The address. */
	private String address;
	
	/** The source. */
	private Source source;

	/**
	 * Gets the tlf data.
	 *
	 * @param query the query
	 * @return the tlf data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getTlfData(String query) throws IOException {

		if (query.isEmpty()) {
			return "Usage: !tlf <name/number/address>";
		} else {
			query = HelperClass.htmlEnc(query);
			String sourceUrlString = "http://www.gulesider.no/tk/search.c?q="
					+ query;
			try {
				source = new Source(new URL(sourceUrlString).openStream());

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			// Parse the entire page right away.
			source.fullSequentialParse();

			// Make sure we have anything to return
			if (source.getTextExtractor().setIncludeAttributes(true).toString()
					.contains("ingen treff")) {
				return "No results available";

				// Handle things differently if we're sent to gule sider bedrift
			} else if (source.getTextExtractor().setIncludeAttributes(true)
					.toString().contains("Treff i firmanavn")) {

				Element numberElement = source
						.getFirstElementByClass("mainTlf");
				Element addressElement = source
						.getFirstElementByClass("mainAdr");

				number = HelperClass.stripNewLine(numberElement.getRenderer()
						.toString());
				address = HelperClass.stripNewLine(addressElement.getRenderer()
						.toString());
				name = "Business";

				// Default case, retrieve info
			} else {
				Element numberElement = source.getFirstElementByClass("number");
				Element addressElement = source
						.getFirstElementByClass("address");
				Element nameElement = source.getFirstElementByClass("name");

				number = HelperClass.stripNewLine(numberElement.getRenderer()
						.toString().substring(0, 12));
				address = HelperClass.stripNewLine(addressElement.getRenderer()
						.toString());
				name = nameElement.getRenderer().toString();
				name = name.substring(1, name.indexOf("MER") - 1);

			}
			return number + ", " + name + "," + address;
		}
	}
}

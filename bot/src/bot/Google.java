package bot;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

// TODO: Auto-generated Javadoc
/**
 * The Class Google.
 */
public class Google {
	
	/** The source. */
	private Source source;

	/**
	 * Search.
	 *
	 * @param query the query
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String search(String query) throws IOException {

		if (query.isEmpty()) {
			return "Usage: !google <query>";
		} else {
			query = HelperClass.htmlEnc(query);
			String sourceUrlString = "http://www.google.com/search?q=" + query;
			try {
				source = new Source(new URL(sourceUrlString).openStream());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			// Parse the entire page right away.
			source.fullSequentialParse();

			// The first search result is in the class named "r".
			Element firstResultElement = source.getFirstElementByClass("r");
			// Fetch it and remove all the annoying newline characters by using
			// the HelperClass.
			return HelperClass.stripNewLine(firstResultElement.getRenderer()
					.toString());

		}
	}
}

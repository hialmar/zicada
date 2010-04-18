package bot;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class Google {
	private Source source;

	public String search(String query) throws IOException {
		// Set a text-only browser USER_AGENT string, since google returns 443
		// with the default java UA.
		System.setProperty("http.agent",
				"Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");

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

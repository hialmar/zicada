/*
 *     This file is part of zicbot.

    zicbot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    zicbot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with zicbot.  If not, see <http://www.gnu.org/licenses/>.

 */
package bot;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * Class for parsing html from the various websites used by the bot.
 */
public class HtmlParser {

	private String name;
	private String number;
	private String address;
	private Source source;

	/**
	 * Gets data from http://www.telefonkatalogen.no/
	 * 
	 * @param query
	 *            the query
	 * @return the tlf data
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
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

	/**
	 * Google search.
	 *
	 * @param query the query
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String googleSearch(String query) throws IOException {
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

	/**
	 * Gets a quote from http://www.bash.org/
	 *
	 * @return the bash quote
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getBashQuote() throws IOException {
		String sourceUrlString = "http://bash.org/?random";
		try {
			source = new Source(new URL(sourceUrlString).openStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// Parse the entire page right away.
		source.fullSequentialParse();

		// The first search result is in the class named "qt".
		Element firstResultElement = source.getFirstElementByClass("qt");
		// Fetch it and remove all the annoying newline characters by using
		// the HelperClass.
		return HelperClass.stripNewLine(firstResultElement.getRenderer()
				.toString());
	}
}

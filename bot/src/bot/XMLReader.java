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


import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class XMLReader.
 */
public class XMLReader {
	
	String result;

	/**
	 * The Class SaxHandler extends DefaultHandler. This is the preferred way of using the SAX parser.
	 */
	public static final class SaxHandler extends DefaultHandler {

		private String cityData = "";
		private String conditionData = "";
		private String tempData = "";
		private String windData = "";
		private String humidityData = "";
		
		public String getCityData() {
			return cityData;
		}

		public String getConditionData() {
			return conditionData;
		}

		public String getHumidityData() {
			return humidityData;
		}

		public String getTempData() {
			return tempData;
		}

		public String getWindData() {
			return windData;
		}

		@Override
		public void startElement(String uri, String localName, String wName,
				Attributes attrs) throws SAXException {

			if (wName.equals("city")) {
				// get the corresponding attribute from data=""
				cityData = attrs.getValue("data");
			}
			if (wName.equals("condition")) {
				// get the corresponding attribute from data=""
				conditionData = attrs.getValue("data");
			}
			if (wName.equals("temp_c")) {
				// get the corresponding attribute from data=""
				tempData = attrs.getValue("data");
			}
			if (wName.equals("wind_condition")) {
				// get the corresponding attribute from data=""
				windData = attrs.getValue("data");
			}
			if (wName.equals("humidity")) {
				// get the corresponding attribute from data=""
				humidityData = attrs.getValue("data");
			}
		}
	}

	private URL url;

	/**
	 * Parses the data.
	 *
	 * @param city the city
	 * @return a string containing the data we get
	 * @throws MalformedURLException
	 */
	public String parseData(String city) throws MalformedURLException {
		city = HelperClass.htmlEnc(city);
		try {
			url = new URL("http://www.google.com/ig/api?weather=" + city);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		try {
			// creates and returns new instance of SAX-implementation:
			SAXParserFactory factory = SAXParserFactory.newInstance();

			// create SAX-parser...
			SAXParser parser = factory.newSAXParser();
			// .. define our handler:
			SaxHandler handler = new SaxHandler();

			// and parse:
			parser.parse(url.openStream(), handler);
			// store parsed data in array
			
			if(handler.getCityData().equals("")){
				result = "No such city found. Usage: !weather <city>";
			} else {
				result = handler.getCityData() + ": " + handler.getConditionData() + ", " + handler.getTempData() + "C, " + handler.getWindData() + ", " + handler.getHumidityData();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return result;
	}
}

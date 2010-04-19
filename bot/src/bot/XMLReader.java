package bot;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLReader.
 */
public class XMLReader {
	
	/** The result. */
	String result;

	/**
	 * The Class SaxHandler.
	 */
	public static final class SaxHandler extends DefaultHandler {

		/** The city data. */
		private String cityData = "";
		
		/** The temp data. */
		private String tempData = "";
		
		/** The wind data. */
		private String windData = "";
		
		/** The humidity data. */
		private String humidityData = "";

		/**
		 * Gets the city data.
		 *
		 * @return the city data
		 */
		public String getCityData() {
			return cityData;
		}

		/**
		 * Gets the humidity data.
		 *
		 * @return the humidity data
		 */
		public String getHumidityData() {
			return humidityData;
		}

		/**
		 * Gets the temp data.
		 *
		 * @return the temp data
		 */
		public String getTempData() {
			return tempData;
		}

		/**
		 * Gets the wind data.
		 *
		 * @return the wind data
		 */
		public String getWindData() {
			return windData;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String wName,
				Attributes attrs) throws SAXException {

			if (wName.equals("city")) {
				// get the corresponding attribute from data=""
				cityData = attrs.getValue("data");
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

	/** The url. */
	private URL url;

	/**
	 * Parses the data.
	 *
	 * @param city the city
	 * @return the string
	 * @throws MalformedURLException the malformed url exception
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
			
			result = "Location: " + handler.getCityData() + ", Temp: " + handler.getTempData() + "C" + ", "
			+ handler.getWindData() + " " + handler.getHumidityData();

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return result;
	}
}

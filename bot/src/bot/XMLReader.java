package bot;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReader {

	public static final class SaxHandler extends DefaultHandler {
		private String cityData = "";
		private String tempData = "";
		private String windData = "";

		public String getCityData() {
			return cityData;
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
			if (wName.equals("temp_c")) {
				// get the corresponding attribute from data=""
				tempData = attrs.getValue("data");
			}
			if (wName.equals("wind_condition")) {
				// get the corresponding attribute from data=""
				windData = attrs.getValue("data");
			}
		}
	}

	public String[] parseData(String city) throws MalformedURLException {
		URL url = new URL("http://www.google.com/ig/api?weather=" + city);
		String[] resultArray = new String[3];
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
			resultArray[0] = handler.getCityData();
			resultArray[1] = handler.getTempData();
			resultArray[2] = handler.getWindData();

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return resultArray;
	}
}

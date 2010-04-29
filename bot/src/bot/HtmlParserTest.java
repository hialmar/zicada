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

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class HtmlParserTest {

	@Test
	public void testGetTlfData() {
		HtmlParser parsetest = new HtmlParser();
		try {
			assertNotNull(parsetest.getTlfData(""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGoogleSearch() {
		HtmlParser parsetest = new HtmlParser();
		try {
			assertNotNull(parsetest.googleSearch(""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBashQuote() {
		HtmlParser parsetest = new HtmlParser();
		try {
			assertNotNull(parsetest.getBashQuote());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

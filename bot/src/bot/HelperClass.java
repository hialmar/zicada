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

/**
 * The Class HelperClass.
 */
public class HelperClass {

	/**
	 * Html enc, used to encode whitespace and local characters to html format
	 *
	 * @param query the query
	 * @return the string
	 */
	public static String htmlEnc(String query) {
		return query.toLowerCase().replaceAll(" ", "%20").replaceAll("\\+",
				"%2b").replaceAll("å", "%F8").replaceAll("æ", "%E6")
				.replaceAll("ø", "%E5");

	}

	/**
	 * Strip new line.
	 *
	 * @param query the query
	 * @return the result
	 */
	public static String stripNewLine(String query) {
		return query.toLowerCase().replaceAll("\\n", " ").replaceAll("\\r", "");
	}
	
}

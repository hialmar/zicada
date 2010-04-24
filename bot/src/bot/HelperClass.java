package bot;


// TODO: Auto-generated Javadoc
/**
 * The Class HelperClass.
 */
public class HelperClass {

	/**
	 * Html enc.
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
	 * @return the string
	 */
	public static String stripNewLine(String query) {
		return query.toLowerCase().replaceAll("\\n", " ").replaceAll("<", "")
				.replaceAll(">", "").replaceAll("\\r", "");
	}
	
}

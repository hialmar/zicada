package bot;

/**
 * The Main Class
 */
public class Main {

	/**
	 * The main method.
	 * This method simply checks for the right arguments and
	 * instantiates an IRC object using them.
	 *
	 * @param args bot servername port nickname username channel
	 * @throws Exception the exception
	 */
	public static void main(String[] args) {
		Irc irc;
		if (args.length != 5) {
			System.err.println("Usage: java -jar bot servername port nickname username channel");
			irc = new Irc("irc.homelien.no", "6667", "zicbot", "zicbot", "#zictest");
//			System.exit(0);
		} else {
			irc = new Irc(args[0], args[1], args[2], args[3], "#" + args[4]);
		}
		irc.setAdmin("zicada");
		irc.setAdmin("b9");
		try {
			irc.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

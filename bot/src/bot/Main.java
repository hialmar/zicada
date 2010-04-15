package bot;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws Exception {
		Irc irc = new Irc();
		if (args.length == 0) {
			System.err
					.println("Using default arguments \n "
							+ "Usage: java -jar bot servername port nickname username channel");

			irc.setConfig("irc.homelien.no", "6667", "zicbot2", "zicbot",
					"#zictest");
		} else {
			irc.setConfig(args[0], args[1], args[2], args[3], "#" + args[4]);
		}
		try {
			irc.initialize();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

}

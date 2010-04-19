package bot;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * The Main Class.
 */
public class Main {

	private Properties p;
	private static String server;
	private static String port;
	private static String nick;
	private static String user;
	private static String[] admins;
	private static String[] channels;

	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public void getConfig() {
		try {
			p = new Properties();
			p.load(new FileInputStream("config.ini"));
		} catch (Exception e) {
			System.out.println(e);
		}
		server = p.getProperty("server");
		port = p.getProperty("port");
		nick = p.getProperty("nick");
		user = p.getProperty("user");
		admins = p.getProperty("admins").split(",");
		channels = p.getProperty("channels").split(",");
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.getConfig();
		System.out.println("Logging onto " +server);
		Irc irc = new Irc(server, nick, port, user);

		for (String admin : admins) {
			irc.setAdmin(admin.trim());
			System.out.println("Registered admin: " + admin);
		}
		System.out.println("Connecting...");
		for (String channel : channels) {
			irc.setChannel(channel.trim());
		}
		irc.initialize();
	}
}

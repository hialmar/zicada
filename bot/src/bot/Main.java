package bot;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * The Main Class.
 */
public class Main {

	private Properties p;
	private static String configFile;
	private static String ircserver;
	private static String ircport;
	private static String botnick;
	private static String ircuser;
	private static String[] admins;
	private static String[] channels;
	
	private String driver;
	private String players_sqlserver;
	private String players_database;
	private String players_username;
	private String players_password;
	private DbConnection connection;

	/**
	 * Instantiates a new main.
	 */
	public Main() {
		try {
			p = new Properties();
			p.load(new FileInputStream(configFile));
		} catch (Exception e) {
			System.out.println(e);
		}
		ircserver = p.getProperty("server");
		ircport = p.getProperty("port");
		botnick = p.getProperty("nick");
		ircuser = p.getProperty("user");
		admins = p.getProperty("admins").split(",");
		channels = p.getProperty("channels").split(",");
		
		players_sqlserver = p.getProperty("players_sqlserver");
		driver = p.getProperty("driver");
		players_database = p.getProperty("players_database");
		players_username = p.getProperty("players_username");
		players_password = p.getProperty("players_password");
	}
	
	/**
	 * Gets the db connection.
	 *
	 * @return the db connection
	 */
	public DbConnection getDbConnection() {
		try {
			connection = new DbConnection(driver, players_sqlserver, 
					players_database, players_username, players_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
		
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		if(args.length == 0){
			System.err.println("Usage: java -jar bot.jar <configfile>");
			System.exit(0);
		} else {
			configFile = args[0];
		}
		new Main();
		System.out.println("Logging onto " +ircserver);
		Irc irc = new Irc(ircserver, botnick, ircport, ircuser);

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

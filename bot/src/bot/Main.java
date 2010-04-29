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
	 * Parses the config file.
	 */
	public Main() {
		try {
			p = new Properties();
			p.load(new FileInputStream(configFile));
		} catch (Exception e) {
			System.out.println("There was a problem loading the config file :");
			System.exit(0);
		}
		try {
			ircserver = p.getProperty("server");
			ircport = p.getProperty("port");
			botnick = p.getProperty("nick");
			if (botnick.length() > 8) {
				throw new IllegalArgumentException("Botnick cannot be more than 8 characters long");
			}
			ircuser = p.getProperty("user");
			admins = p.getProperty("admins").split(",");
			channels = p.getProperty("channels").split(",");
			
			players_sqlserver = p.getProperty("players_sqlserver");
			driver = p.getProperty("driver");
			players_database = p.getProperty("players_database");
			players_username = p.getProperty("players_username");
			players_password = p.getProperty("players_password");
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Config file not recognized");
			System.exit(0);
		}

	}
	
	/**
	 * Returns a new database connection based on the contents of the config file
	 *
	 * @return the db connection
	 */
	public DbConnection getDbConnection() {
		try {
			connection = new DbConnection(driver, players_sqlserver, 
					players_database, players_username, players_password);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return connection;
		
	}

	/**
	 * The main method.
	 *
	 * @param args The name of the config file to use.
	 * @throws Exception the exception
	 */
	public static void main(String[] args) {
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

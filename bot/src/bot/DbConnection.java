package bot;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class DbConnection. This class is used to instantiate a database object. It
 * has generic methods for running database queries. Specific methods used by
 * the bot also go here.
 */
public class DbConnection {

	private Connection connection;
	private String driver;
	private String server;
	private String database;
	private String username;
	private String password;

	/**
	 * Instantiates a new db connection.
	 *
	 * @param driver
	 *            This can be any JDBC compatible driver. By default, the bot
	 *            uses the Mysql connector driver.
	 * @param server
	 *            The hostname of the database server we wish to connect to.
	 * @param database
	 *            The name of the database we would like to open.
	 * @param username
	 *            The username for the database.
	 * @param password
	 *            The password for the database.
	 * @throws Exception
	 *             IO Exception printed to stdout.
	 */
	public DbConnection(String driver, String server, String database,
			String username, String password) throws Exception {
		this.driver = driver;
		this.server = server;
		this.database = database;
		this.username = username;
		this.password = password;
		try {
			Class.forName(this.driver).newInstance();
			connect();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Connect.
	 *
	 * @throws Exception
	 *             If we can't connect to the database.
	 */
	public void connect() throws Exception {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + server
					+ "/" + database, username, password);
		} catch (Exception e) {
			System.out.println("Could not connect to database server");
			throw e;
		}
	}

	/**
	 * Run query. This method runs a check to see wether the connection is still
	 * valid, and creates a new one if it is not.
	 *
	 * @param sql
	 *            The SQL statement we would like to run.
	 * @return ResultSet containing the results.
	 * @throws Exception
	 *             Stack Trace to stdout.
	 */
	public ResultSet runQuery(String sql) throws Exception {
		Statement stmt = null;
		ResultSet res = null;
		try {
			if (!connection.isValid(0)) {
				connect();
			}
			stmt = connection.createStatement();
			res = stmt.executeQuery(sql);
			return res;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the players who are currently logged into any of the servers in ALFA
	 * (http://www.alandfaraway.org)
	 *
	 * @return the currently logged in players
	 * @throws Exception
	 *             Stack Trace to stdout.
	 */
	public String getPlayers() throws Exception {
		int tsm = 0;
		int bg = 0;
		int ms = 0;
		int tsmdms = 0;
		int bgdms = 0;
		int msdms = 0;

		try {
			ResultSet tsmResult = runQuery("SELECT Count(characters.Name) AS tsm_players FROM characters INNER JOIN servers ON characters.ServerID = servers.ID INNER JOIN pwdata ON pwdata.`Name` = servers.`Name` AND pwdata.`Key` = 'ACR_TIME_SERVERTIME' AND pwdata.`Last` >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE) WHERE IsOnline = 1 AND ServerID = 3");
			ResultSet bgResult = runQuery("SELECT Count(characters.Name) AS bg_players FROM characters INNER JOIN servers ON characters.ServerID = servers.ID INNER JOIN pwdata ON pwdata.`Name` = servers.`Name` AND pwdata.`Key` = 'ACR_TIME_SERVERTIME' AND pwdata.`Last` >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE) WHERE IsOnline = 1 AND ServerID = 10");
			ResultSet msResult = runQuery("SELECT Count(characters.Name) AS ms_players FROM characters INNER JOIN servers ON characters.ServerID = servers.ID INNER JOIN pwdata ON pwdata.`Name` = servers.`Name` AND pwdata.`Key` = 'ACR_TIME_SERVERTIME' AND pwdata.`Last` >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE) WHERE IsOnline = 1 AND ServerID = 9");
			ResultSet tsmDmResult = runQuery("select Count(*) AS tsm_dms FROM players INNER JOIN characters ON players.id = characters.playerid INNER JOIN servers ON characters.ServerID = servers.ID INNER JOIN pwdata ON pwdata.`Name` = servers.`Name` AND pwdata.`Key` = 'ACR_TIME_SERVERTIME' AND pwdata.`Last` >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE) WHERE characters.isonline = 1 AND players.isdm = 1 AND characters.serverid = 3");
			ResultSet bgDmResult = runQuery("select Count(*) AS bg_dms FROM players INNER JOIN characters ON players.id = characters.playerid INNER JOIN servers ON characters.ServerID = servers.ID INNER JOIN pwdata ON pwdata.`Name` = servers.`Name` AND pwdata.`Key` = 'ACR_TIME_SERVERTIME' AND pwdata.`Last` >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE) WHERE characters.isonline = 1 AND players.isdm = 1 AND characters.serverid = 10");
			ResultSet msDmResult = runQuery("select Count(*) AS ms_dms FROM players INNER JOIN characters ON players.id = characters.playerid INNER JOIN servers ON characters.ServerID = servers.ID INNER JOIN pwdata ON pwdata.`Name` = servers.`Name` AND pwdata.`Key` = 'ACR_TIME_SERVERTIME' AND pwdata.`Last` >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE) WHERE characters.isonline = 1 AND players.isdm = 1 AND characters.serverid = 9");

			tsmResult.next();
			tsm = tsmResult.getInt("tsm_players");

			bgResult.next();
			bg = bgResult.getInt("bg_players");

			msResult.next();
			ms = msResult.getInt("ms_players");

			tsmDmResult.next();
			tsmdms = tsmDmResult.getInt("tsm_dms");

			bgDmResult.next();
			bgdms = bgDmResult.getInt("bg_dms");

			msDmResult.next();
			msdms = msDmResult.getInt("ms_dms");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		tsm -= tsmdms;
		bg -= bgdms;
		ms -= msdms;

		String tsmplWord;
		String bgplWord;
		String msplWord;
		String tsmdmWord;
		String bgdmWord;
		String msdmWord;

		if (tsm == 1) {
			tsmplWord = "player";
		} else {
			tsmplWord = "players";
		}
		if (bg == 1) {
			bgplWord = "player";
		} else {
			bgplWord = "players";
		}
		if (ms == 1) {
			msplWord = "player";
		} else {
			msplWord = "players";
		}
		if (tsmdms == 1) {
			tsmdmWord = "DM";
		} else {
			tsmdmWord = "DMs";
		}
		if (bgdms == 1) {
			bgdmWord = "DM";
		} else {
			bgdmWord = "DMs";
		}
		if (msdms == 1) {
			msdmWord = "DM";
		} else {
			msdmWord = "DMs";
		}

		return "Silver Marches has " + tsm + " " + tsmplWord
				+ " and " + tsmdms + " " + tsmdmWord +
				", Baldurs Gate has " + bg + " " + bgplWord
				+ " and " + bgdms + " " + bgdmWord +
				", Moonshaes has " + ms + " " + msplWord
				+ " and " + msdms + " " + msdmWord + ".";
	}

	/**
	 * Close connection.
	 *
	 * @throws SQLException
	 *             sQL exception
	 */
	public void closeConnection() throws SQLException {
		try {
			connection.close();
		} catch (SQLException ex) {
			throw ex;
		}
	}
}

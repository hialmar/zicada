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

	/** The connection. */
	private Connection connection;

	/**
	 * The driver. This can be any JDBC compatible driver. By default, the bot
	 * uses the Mysql connector driver.
	 */
	private String driver;

	/**
	 * The server. The hostname of the database server we wish to connect to.
	 */
	private String server;

	/**
	 * The database. The name of the database we would like to open.
	 */
	private String database;

	/**
	 * The username. The username for the database.
	 */
	private String username;

	/**
	 * The password. The password for the database.
	 */
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
			System.out.println(e);
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
		} catch (Exception ex) {
			System.out.println("Could not connect to database server");
			throw ex;
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
		int tsmdms = 0;
		int bgdms = 0;
		
		try {
			ResultSet tsmResult = runQuery("SELECT Count(Name) AS tsm_players FROM characters WHERE IsOnline = 1 AND ServerID = 3");
			ResultSet bgResult = runQuery("SELECT Count(Name) AS bg_players FROM characters WHERE IsOnline = 1 AND ServerID = 10");
			ResultSet tsmDmResult = runQuery("select Count(*) AS tsm_dms FROM players INNER JOIN characters ON players.id = characters.playerid WHERE characters.isonline = 1 AND players.isdm = 1 AND characters.serverid = 3");
			ResultSet bgDmResult = runQuery("select Count(*) AS bg_dms FROM players INNER JOIN characters ON players.id = characters.playerid WHERE characters.isonline = 1 AND players.isdm = 1 AND characters.serverid = 10");

			tsmResult.next();
			tsm = tsmResult.getInt("tsm_players");
			bgResult.next();
			bg = bgResult.getInt("bg_players");
			tsmDmResult.next();
			tsmdms = tsmDmResult.getInt("tsm_dms");
			bgDmResult.next();
			bgdms = bgDmResult.getInt("bg_dms");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		tsm -= tsmdms;
		bg -= bgdms;
		String tsmplWord;
		String bgplWord;
		String tsmdmWord;
		String bgdmWord;
		
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
		
		return "Silver Marches has " + tsm + " " + tsmplWord + " and " + tsmdms + " " + tsmdmWord + 
			   ", Baldurs Gate has " + bg + " " + bgplWord + " and " + bgdms + " " + bgdmWord;
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

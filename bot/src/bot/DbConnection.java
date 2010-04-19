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
		try {
			ResultSet tsmResult = runQuery("SELECT Count(Name) AS tsm_players FROM characters WHERE IsOnline = 1 AND ServerID = 3");
			ResultSet bgResult = runQuery("SELECT Count(Name) AS bg_players FROM characters WHERE IsOnline = 1 AND ServerID = 10");
			tsmResult.next();
			bgResult.next();
			tsm = tsmResult.getInt("tsm_players");
			bg = bgResult.getInt("bg_players");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int total = tsm + bg;
		return "TSM: " + tsm + " BG: " + bg + " Total: " + total;

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

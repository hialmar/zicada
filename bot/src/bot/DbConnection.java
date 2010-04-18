package bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
	
	private Connection connection;
	private String driver;
	private String server;
	private String database;
	private String username;
	private String password;

	public DbConnection(String driver, String server, String database,
			String username, String password) throws Exception {
		this.driver = driver;
		this.server = server;
		this.database = database;
		this.username = username;
		this.password = password;
		Class.forName(this.driver).newInstance();
		connect();

	}
	public void connect() throws Exception {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + server
					+ "/" + database, username, password);
		} catch (Exception ex) {
			System.out.println("Could not connect to database server");
			throw ex;
		}
	}

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

	public void closeConnection() throws SQLException {
		try {
			connection.close();
		} catch (SQLException ex) {
			throw ex;
		}
	}
}

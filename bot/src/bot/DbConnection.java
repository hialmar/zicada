package bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    private Connection connection;
    public DbConnection(String driver,String server,String database,String username,String password) throws Exception {
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, username, password);
        } catch (Exception ex) {
            System.out.println("Could not connect to database");
            throw ex;
        }
    }
    
    public ResultSet runQuery(String sql){
        Statement stmt = null;
        ResultSet res = null;
        try {
            stmt = connection.createStatement();
            res = stmt.executeQuery(sql);
            return res;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public String getPlayers() {
        int tsm = 0;
        int bg = 0;
    	ResultSet tsmResult = runQuery("SELECT Count(Name) AS tsm_players FROM characters WHERE IsOnline = 1 AND ServerID = 3");
    	ResultSet bgResult = runQuery("SELECT Count(Name) AS bg_players FROM characters WHERE IsOnline = 1 AND ServerID = 10");
    	try {
    		tsmResult.next();
    		bgResult.next();
			tsm = tsmResult.getInt("tsm_players");
			bg = bgResult.getInt("bg_players");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int total = tsm + bg;
    	return "TSM: " + tsm + " BG: "+ bg + " Total: " + total;
    	
    }
    
    public void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }
}

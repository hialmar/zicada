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
    
    public String getPlayers(){
        String sql = "SELECT Count(Name) AS bg_players FROM characters WHERE IsOnline = 1 AND ServerID = 10" ;
        String sql2 = "SELECT Count(Name) AS tsm_players FROM characters WHERE IsOnline = 1 AND ServerID = 3" ;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet res = null;
        ResultSet res2 = null;
        int tsm = 0;
        int bg = 0;
        try {
            stmt = connection.createStatement();
            stmt2 = connection.createStatement();
            res = stmt.executeQuery(sql);
            res2 = stmt2.executeQuery(sql2);
            
            while(res.next()){
            	bg = res.getInt("bg_players");
            }
            
            while(res2.next()) {
            	tsm = res2.getInt("tsm_players");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
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

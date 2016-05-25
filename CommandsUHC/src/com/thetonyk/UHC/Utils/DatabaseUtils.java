package com.thetonyk.UHC.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

public class DatabaseUtils {

    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
    	
    	if (connection != null) {
    		
    		if (connection.isValid(1)) return connection;
    		
    		connection.close();
   
    	}
    	
    	try {
        	
            Class.forName("com.mysql.jdbc.Driver");
            
        } catch (Exception exception) {
        	
            Main.uhc.getLogger().severe("[DatabaseUtils] Unable to load the JDBC Driver.");
            
        }
    	
    	try {
    		
    		connection = DriverManager.getConnection("jdbc:mysql://localhost/commandspvp", PassUtils.user, PassUtils.pass);
    		
    	} catch (SQLException exception) {
    		
    		Bukkit.getLogger().severe("[DatabaseUtils] Unable to load the JDBC Driver.");
    		return null;
    		
    	}
    	
    	return connection;
    	
    }
    
    public static ResultSet sqlQuery (String request) {
    	
    	ResultSet result = null;
    	
    	try {
    		
    		result = DatabaseUtils.getConnection().createStatement().executeQuery(request);
    		
    	} catch (SQLException exception) {
    		
    		return null;
    		
    	}
    	
    	return result;
    	
    }
    
    public static void sqlInsert (String request) {
    	
    	new BukkitRunnable() {
    		
    		public void run() {
    			
		    	try {
		    		
		    		DatabaseUtils.getConnection().createStatement().executeUpdate(request);
		    		
		    	} catch (SQLException exception) {
		    		
		    		return;
		    		
		    	}
		    	
    		}
    	
    	}.runTaskAsynchronously(Main.uhc);
    	
    }

}



package com.thetonyk.UHC.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

public class DatabaseUtils {

    private static Connection connection;
    
    public static Connection getConnection() {
    	
    	if (connection != null) return connection;
    	
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
    		
    		exception.printStackTrace();
    		return null;
    		
    	}
    	
    	return result;
    	
    }
    
    public static void asynSqlQuery (String request, Callback<List<Map<String, String>>> callback) {

    	new BukkitRunnable() {
    		
    		public void run() {
    			
    			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
    			
    			try {
    	    		
    				ResultSet tempResult = DatabaseUtils.getConnection().createStatement().executeQuery(request);
    				Map<String, String> entry = new HashMap<String, String>();
    				
    				while (tempResult.next()) {
    					
    					entry.clear();
    					
    					for (int i = 1; i <= tempResult.getMetaData().getColumnCount(); i++) {
    						
    						String value = null;
    						
    						switch (tempResult.getMetaData().getColumnType(i)) {
    						
    							case Types.VARCHAR:
    							case Types.CHAR:
    							case Types.LONGVARCHAR:
    							case Types.LONGNVARCHAR:
    								value = tempResult.getString(i);
    								break;
    							case Types.BIGINT:
    								value = String.valueOf(tempResult.getLong(i));
    								break;
    							case Types.TINYINT:
    							case Types.SMALLINT:
    							case Types.INTEGER:
    								value = String.valueOf(tempResult.getInt(i));
    								break;
    							default:
    								break;
    								
    						}
    						
    						entry.put(tempResult.getMetaData().getColumnName(i), value);

    					}
    					
    					result.add(entry);
    					
    				}
    				
    				tempResult.close();
    	    		
    	    	} catch (SQLException exception) {
    	    		
    	    		exception.printStackTrace();
    	    		
    	    	}
    			
    			new BukkitRunnable() {
    				
    				public void run() {
    				
    					callback.onSuccess(result);
    					
    				}
    				
    			}.runTask(Main.uhc);
    			
    		}
    		
    	}.runTaskAsynchronously(Main.uhc);
    	
    }
    
    public interface Callback<T> {
    	
        void onSuccess(T done);
        void onFailure(Throwable cause);
        
    }
    
    public static void sqlInsert (String request) {
    	
    	new BukkitRunnable() {
    		
    		public void run() {
    			
		    	try {
		    		
		    		DatabaseUtils.getConnection().createStatement().executeUpdate(request);
		    		
		    	} catch (SQLException exception) {
		    		
		    		exception.printStackTrace();
		    		
		    	}
		    	
    		}
    	
    	}.runTaskAsynchronously(Main.uhc);
    	
    }
    
    public static Boolean exist (String request) {
    	
    	Boolean exist = false;
    	
    	try {
    		
    		ResultSet result = DatabaseUtils.getConnection().createStatement().executeQuery(request);
    		
    		exist = result.next();
    		
    		result.close();
    		
    	} catch (SQLException exception) {
    		
    		exception.printStackTrace();
    		return false;
    		
    	}
    	
    	return exist;
    	
    }

}



package com.thetonyk.UHC.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thetonyk.UHC.Main;

public class DatabaseUtils {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
    	
        if (connection != null) {
        	
        	if (connection.isValid(1)) {
        		
        		return connection;
        		
        	} else {

        		connection.close();
        	
        	}
        	
        }

        try {
        	
            Class.forName("com.mysql.jdbc.Driver");
            
        } catch (ClassNotFoundException ex) {
        	
            Main.uhc.getLogger().severe("[DatabaseUtils] §cUnable to load the JDBC Driver.");
            
        }
        
        connection = DriverManager.getConnection("jdbc:mysql://localhost/commandspvp", PassUtils.user, PassUtils.pass);

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
    
    public static void sqlInsert (String request) {
    	
    	try {
    		
    		DatabaseUtils.getConnection().createStatement().executeUpdate(request);
    		
    	} catch (SQLException exception) {
    		
    		exception.printStackTrace();
    		
    	}
    	
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



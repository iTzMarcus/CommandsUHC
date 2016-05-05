package com.thetonyk.UHC.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.thetonyk.UHC.MessengerListener;

public class GameUtils {
	
	public enum Status {
		
		NONE, READY, OPEN, TELEPORT, PLAY, END;
		
	}
	
	public static Status getStatus() {
		
		Status status = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT status FROM uhc WHERE server = '" + MessengerListener.lastServer + "';");
			
			if (req.next()) status = Status.valueOf(req.getString("status"));
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get status of the uhc on server" + MessengerListener.lastServer + ".");
			
		}
		
		return status;
		
	}
	
	public static void setStatus(Status newStatus) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET status = '" + newStatus.toString() + "' WHERE server = '" + MessengerListener.lastServer + "';");
		
	}

	public static String getWorld() {
		
		String world = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT world FROM uhc WHERE server = '" + MessengerListener.lastServer + "';");
			
			if (req.next()) world = req.getString("world");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get world of the uhc on server" + MessengerListener.lastServer + ".");
			
		}
		
		return world;
		
	}
	
	public static void setWorld(String newWorld) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET world = '" + newWorld + "' WHERE server = '" + MessengerListener.lastServer + "';");
		
	}
	
	public static Boolean getTeleported() {
		
		Boolean teleported = false;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT teleported FROM uhc WHERE server = '" + MessengerListener.lastServer + "';");
			
			if (req.next()) teleported = req.getBoolean("teleported");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get teleported state of the uhc on server" + MessengerListener.lastServer + ".");
			
		}
		
		return teleported;
		
	}
	
	public static void setTeleported(Boolean teleported) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET teleported = '" + (teleported ? "1" : "0") + "' WHERE server = '" + MessengerListener.lastServer + "';");
		
	}

}

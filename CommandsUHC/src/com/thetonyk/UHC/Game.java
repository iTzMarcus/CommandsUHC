package com.thetonyk.UHC;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.thetonyk.UHC.Listener.MessengerListener;
import com.thetonyk.UHC.Utils.DatabaseUtils;

public class Game {
	
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
	
	public enum Status {
		
		NONE, READY, OPEN, TELEPORT, PLAY, END;
		
	}

}

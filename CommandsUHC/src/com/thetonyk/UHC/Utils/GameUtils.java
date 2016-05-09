package com.thetonyk.UHC.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.MessengerListener;

public class GameUtils {
	
	public static Map<UUID, Map<String, String>> players = new HashMap<UUID, Map<String, String>>();
	public static Map<UUID, Location> locations = null;
	public static int slots = 100;
	
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
		
		return world == "" ? null : world;
		
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
	
	public static void setupPlayers() {
		
		GameUtils.players.clear();
		
		for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
			
			GameUtils.players.put(player.getUniqueId(), new HashMap<String, String>());
			GameUtils.players.get(player.getUniqueId()).put("death", "false");
			GameUtils.players.get(player.getUniqueId()).put("teleported", "false");
			GameUtils.players.get(player.getUniqueId()).put("onGround", "false");
			
		}
		
	}
	
	public static int getPlayers() {
		
		int players = (GameUtils.getStatus() == Status.NONE || GameUtils.getStatus() == Status.OPEN || GameUtils.getStatus() == Status.READY) ? Bukkit.getOnlinePlayers().size() : Bukkit.getWhitelistedPlayers().size();
		
		if (GameUtils.getStatus() == Status.NONE || GameUtils.getStatus() == Status.OPEN || GameUtils.getStatus() == Status.READY) {
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				if (player.getGameMode() != GameMode.SPECTATOR) continue;
				
				players--;
				
			}
			
		}
		
		return players;
		
	}

}

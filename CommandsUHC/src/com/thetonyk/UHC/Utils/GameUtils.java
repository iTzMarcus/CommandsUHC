package com.thetonyk.UHC.Utils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Features.DisplayTimers;
import com.thetonyk.UHC.Features.DQLogout;

public class GameUtils {
	
	public static Boolean reset = false;
	
	private static Status status = GameUtils.getStatusSQL();
	private static String world = GameUtils.getWorldSQL();
	private static Boolean teleported = GameUtils.getTeleportedSQL();
	private static Map<UUID, Map<String, String>> players = GameUtils.getPlayersSQL();
	private static Map<UUID, Location> locations = GameUtils.getLocationsSQL();
	private static int slots = GameUtils.getSlotsSQL();
	private static int pve = GameUtils.getPVESQL();
	private static Map<UUID, Integer> kills = GameUtils.getKillsSQL();
	
	public static String getServer() {
		
		return Main.uhc.getConfig().getString("server");
		
	}
	
	public enum Status {
		
		NONE, READY, OPEN, TELEPORT, PLAY, END;
		
	}
	
	public static Status getStatus() {
		
		return GameUtils.status != null ? GameUtils.status : GameUtils.getStatusSQL();
		
	}
	
	private static Status getStatusSQL() {
		
		Status status = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT status FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) status = Status.valueOf(req.getString("status"));
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get status of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return status;
		
	}
	
	public static void setStatus(Status newStatus) {
		
		GameUtils.status = newStatus;
		GameUtils.setStatusSQL(newStatus);
		
	}
	
	private static void setStatusSQL(Status newStatus) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET status = '" + newStatus.toString() + "' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static String getWorld() {
		
		return GameUtils.world != null ? GameUtils.world : GameUtils.getWorldSQL();
		
	}

	private static String getWorldSQL() {
		
		String world = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT world FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) world = req.getString("world");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get world of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return world == "" ? null : world;
		
	}
	
	public static void setWorld(String newWorld) {
		
		GameUtils.world = newWorld;
		GameUtils.setWorldSQL(newWorld);
		
	}
	
	private static void setWorldSQL(String newWorld) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET world = '" + newWorld + "' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static Boolean getTeleported() {
		
		return GameUtils.teleported != null ? GameUtils.teleported : GameUtils.getTeleportedSQL();
		
	}
	
	private static Boolean getTeleportedSQL() {
		
		Boolean teleported = false;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT teleported FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) teleported = req.getBoolean("teleported");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get teleported state of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return teleported;
		
	}
	
	public static void setTeleported(Boolean teleported) {
		
		GameUtils.teleported = teleported;
		GameUtils.setTeleportedSQL(teleported);
		
	}
	
	private static void setTeleportedSQL(Boolean teleported) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET teleported = '" + (teleported ? "1" : "0") + "' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static void setupPlayers() {
		
		if (GameUtils.players == null) GameUtils.players = new HashMap<UUID, Map<String, String>>();
		
		for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
			
			if (GameUtils.getSpectate(player.getUniqueId())) continue;
			
			GameUtils.players.put(player.getUniqueId(), new HashMap<String, String>());
			GameUtils.players.get(player.getUniqueId()).put("death", "false");
			GameUtils.players.get(player.getUniqueId()).put("teleported", "false");
			GameUtils.players.get(player.getUniqueId()).put("onGround", "false");
			GameUtils.players.get(player.getUniqueId()).put("spectate", "false");
			
		}
		
		GameUtils.setPlayersSQL(GameUtils.players);
		
	}
	
	public static void addPlayer(UUID player) {
		
		if (GameUtils.players == null) GameUtils.players = new HashMap<UUID, Map<String, String>>();
		
		GameUtils.players.put(player, new HashMap<String, String>());
		GameUtils.players.get(player).put("death", "false");
		GameUtils.players.get(player).put("teleported", "false");
		GameUtils.players.get(player).put("onGround", "false");
		GameUtils.players.get(player).put("spectate", "false");
		
		GameUtils.setPlayersSQL(GameUtils.players);
		
	}
	
	private static void resetPlayers() {
		
		GameUtils.players = null;
		DatabaseUtils.sqlInsert("UPDATE uhc SET players = '' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static Map<UUID, Map<String, String>> getPlayers() {
		
		return GameUtils.players != null ? GameUtils.players : GameUtils.getPlayersSQL();
		
	}
	
	private static Map<UUID, Map<String, String>> getPlayersSQL() {
		
		String players = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT players FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) players = req.getString("players");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get players of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return players == "" ? new HashMap<UUID, Map<String, String>>() : new Gson().fromJson(players, new TypeToken<Map<UUID, Map<String, String>>>(){}.getType());
		
	}
	
	private static void setPlayers(Map<UUID, Map<String, String>> players) {
		
		GameUtils.players = players;
		GameUtils.setPlayersSQL(players);
		
	}
	
	private static void setPlayersSQL(Map<UUID, Map<String, String>> players) {
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET players = '" + gson.toJson(players) + "' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	private static Map<String, String> getPlayer(UUID uuid) {
		
		if (!GameUtils.getPlayers().containsKey(uuid)) return null;
		
		return GameUtils.getPlayers().get(uuid);
		
	}
	
	public static void setDeath(UUID uuid, Boolean death) {
		
		Map<UUID, Map<String, String>> players = GameUtils.getPlayers();
		
		if (!players.containsKey(uuid)) return;
		
		players.get(uuid).put("death", death.toString());
		
		GameUtils.setPlayers(players);
		
	}
	
	public static Boolean getDeath(UUID uuid) {
		
		if (!GameUtils.getPlayers().containsKey(uuid) || !GameUtils.getPlayer(uuid).containsKey("death")) return false;
		
		return Boolean.parseBoolean(GameUtils.getPlayer(uuid).get("death"));
		
	}
	public static void setTeleported(UUID uuid, Boolean teleported) {
		
		Map<UUID, Map<String, String>> players = GameUtils.getPlayers();
		
		if (!players.containsKey(uuid)) return;
		
		players.get(uuid).put("teleported", teleported.toString());
		
		GameUtils.setPlayers(players);
		
	}
	
	public static Boolean getTeleported(UUID uuid) {
		
		if (!GameUtils.getPlayers().containsKey(uuid) || !GameUtils.getPlayer(uuid).containsKey("teleported")) return false;
		
		return Boolean.parseBoolean(GameUtils.getPlayer(uuid).get("teleported"));
		
	}
	
	public static void setOnGround(UUID uuid, Boolean onGround) {
		
		Map<UUID, Map<String, String>> players = GameUtils.getPlayers();
		
		if (!players.containsKey(uuid)) return;
		
		players.get(uuid).put("onGround", onGround.toString());
		
		GameUtils.setPlayers(players);
		
	}
	
	public static Boolean getOnGround(UUID uuid) {
		
		if (!GameUtils.getPlayers().containsKey(uuid) || !GameUtils.getPlayer(uuid).containsKey("onGround")) return false;
		
		return Boolean.parseBoolean(GameUtils.getPlayer(uuid).get("onGround"));
		
	}
	
	public static void setSpectate(UUID uuid, Boolean spectate) {
		
		Map<UUID, Map<String, String>> players = GameUtils.getPlayers();
		
		if (!players.containsKey(uuid)) return;
		
		players.get(uuid).put("spectate", spectate.toString());
		
		GameUtils.setPlayers(players);
		
	}
	
	public static Boolean getSpectate(UUID uuid) {
		
		if (!GameUtils.getPlayers().containsKey(uuid) || !GameUtils.getPlayer(uuid).containsKey("spectate")) return false;
		
		return Boolean.parseBoolean(getPlayer(uuid).get("spectate"));
		
	}
	
	public static Map<UUID, Location> getLocations() {
		
		return GameUtils.locations != null ? GameUtils.locations : GameUtils.getLocationsSQL();
		
	}
	
	private static Map<UUID, Location> getLocationsSQL() {
		
		String locations = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT locations FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) locations = req.getString("locations");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get locations of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		if (locations == "") return new HashMap<UUID, Location>(); 
		
		Map<UUID, Map<String, Object>> serializedLocations = new Gson().fromJson(locations, new TypeToken<Map<UUID, Map<String, Object>>>(){}.getType());
		Map<UUID, Location> unserializedLocations = new HashMap<UUID, Location>();
		
		for (Map.Entry<UUID, Map<String, Object>> location : serializedLocations.entrySet()) {
			
			Location newLocation = new Location(Bukkit.getWorld((String) location.getValue().get("world")), (double) location.getValue().get("x"), (double) location.getValue().get("y"), (double) location.getValue().get("z"));
			
			unserializedLocations.put(location.getKey(), newLocation);
			
		}
		
		return unserializedLocations;
		
	}
	
	public static void setLocations(Map<UUID, Location> locations) {
		
		GameUtils.locations = locations;
		GameUtils.setLocationsSQL(locations);
		
	}
	
	private static void setLocationsSQL(Map<UUID, Location> locations) {
		
		Map<UUID, Map<String, Object>> serializedLocations = new HashMap<UUID, Map<String, Object>>();
		
		for (Map.Entry<UUID, Location> location : locations.entrySet()) {
			
			Map<String, Object> serializedLocation = new HashMap<String, Object>();
			
			serializedLocation.put("world", location.getValue().getWorld().getName());
			serializedLocation.put("x", location.getValue().getX());
			serializedLocation.put("y", location.getValue().getY());
			serializedLocation.put("z", location.getValue().getZ());
			
			serializedLocations.put(location.getKey(), serializedLocation);
			
		}
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET locations = '" + gson.toJson(serializedLocations) + "' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static void addLocation(UUID uuid, Location location) {
		
		Map<UUID, Location> locations = GameUtils.getLocations();
		
		locations.put(uuid, location);
		
		GameUtils.setLocations(locations);
		
	}
	
	public static Location getLocation(UUID uuid) {
		
		Map<UUID, Location> locations = getLocations();
		
		if (!locations.containsKey(uuid)) return null;
		
		return locations.get(uuid);
		
	}
	
	private static void resetLocations() {
		
		GameUtils.locations = null;
		DatabaseUtils.sqlInsert("UPDATE uhc SET locations = '' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static int getSlots() {
		
		return GameUtils.slots > 0 ? GameUtils.slots : GameUtils.getSlotsSQL();
		
	}
	
	private static int getSlotsSQL() {
		
		int slots = 100;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT slots FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) slots = req.getInt("slots");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get slots of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return slots;
		
	}
	
	public static void setSlots(int slots) {
		
		GameUtils.slots = slots;
		GameUtils.setSlotsSQL(slots);
		
	}
	
	private static void setSlotsSQL(int slots) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET slots = " + slots + " WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static int getTime() {
		
		int time = 0;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT time FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) time = req.getInt("time");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get time of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return time;
		
	}
	
	public static void setTime(int time) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET time = " + time + " WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static int getPVP() {
		
		int pvp = 900;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT pvp FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) pvp = req.getInt("pvp");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get pvp of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return pvp;
		
	}
	
	public static void setPVP(int pvp) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET pvp = " + pvp + " WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static int getMeetup() {
		
		int meetup = 3600;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT meetup FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) meetup = req.getInt("meetup");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get meetup of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return meetup;
		
	}
	
	public static void setMeetup(int meetup) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET meetup = " + meetup + " WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static int getPVE() {
		
		return GameUtils.pve;
		
	}
	
	private static int getPVESQL() {
		
		int pve = 0;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT pve FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) pve = req.getInt("pve");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get pve deaths of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return pve;
		
	}
	
	public static void setPVE(int pve) {
		
		GameUtils.pve = pve;
		GameUtils.setPVESQL(pve);
		
	}
	
	private static void setPVESQL(int pve) {
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET pve = " + pve + " WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static Map<UUID, Integer> getKills() {
		
		return GameUtils.kills != null ? GameUtils.kills : GameUtils.getKillsSQL();
		
	}
	
	private static Map<UUID, Integer> getKillsSQL() {
		
		String kills = null;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT kills FROM uhc WHERE server = '" + GameUtils.getServer() + "';");
			
			if (req.next()) kills = req.getString("kills");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[Game] Error to get kills of the uhc on server" + GameUtils.getServer() + ".");
			
		}
		
		return kills == "" ? new HashMap<UUID, Integer>() : new Gson().fromJson(kills, new TypeToken<Map<UUID, Integer>>(){}.getType());
		
	}
	
	public static void setKills(Map<UUID, Integer> kills) {
		
		GameUtils.kills = kills;
		GameUtils.setKillsSQL(kills);
		
	}
	
	private static void setKillsSQL(Map<UUID, Integer> kills) {
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		
		DatabaseUtils.sqlInsert("UPDATE uhc SET kills = '" + gson.toJson(kills) + "' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	private static void resetKills() {
		
		GameUtils.kills = null;
		DatabaseUtils.sqlInsert("UPDATE uhc SET kills = '' WHERE server = '" + GameUtils.getServer() + "';");
		
	}
	
	public static int getPlayersCount() {
		
		int players = (GameUtils.getStatus() == Status.NONE || GameUtils.getStatus() == Status.OPEN || GameUtils.getStatus() == Status.READY) ? Bukkit.getOnlinePlayers().size() : getAlives().size();
		
		if (GameUtils.getStatus() != Status.TELEPORT || GameUtils.getStatus() != Status.PLAY || GameUtils.getStatus() != Status.END) return players;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (player.getGameMode() != GameMode.SPECTATOR) continue;
			
			players--;
			
		}
		
		return players;
		
	}
	
	public static List<UUID> getAlives() {
		
		List<UUID> alives = new ArrayList<UUID>();
		
		for (UUID player : GameUtils.getPlayers().keySet()) {
			
			if (GameUtils.getDeath(player) || GameUtils.getSpectate(player)) continue;
			
			alives.add(player);
			
		}
		
		return alives;
		
	}
	
	public static void resetGame() {
		
		GameUtils.setStatus(Status.NONE);		
		Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().setSize(WorldUtils.getSize(GameUtils.getWorld()));
		Bukkit.getWorld(GameUtils.getWorld()).setPVP(false);
		GameUtils.setWorld("");
		GameUtils.setTeleported(false);
		GameUtils.resetPlayers();
		GameUtils.setSlots(100);
		GameUtils.resetLocations();
		GameUtils.setTime(0);
		GameUtils.setPVP(900);
		GameUtils.setMeetup(3600);
		GameUtils.setPVE(0);
		GameUtils.resetKills();
		if (DisplayTimers.timer != null) DisplayTimers.timer.cancel();
		DisplayTimers.timer = null;
		DisplayTimers.time = 0;
		DisplayTimers.pvpTime = 900;
		DisplayTimers.meetupTime = 3600;
		DQLogout.reset();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			for (Player hidden : player.spigot().getHiddenPlayers()) {
				
				if (!hidden.isOnline()) continue;
				
				player.showPlayer(hidden);
				
			}
			
			player.setGameMode(GameMode.ADVENTURE);
			PlayerUtils.clearInventory(player);
			PlayerUtils.clearXp(player);
			PlayerUtils.feed(player);
			PlayerUtils.heal(player);
			PlayerUtils.clearEffects(player);
			player.setMaxHealth(20.0);
			player.teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
			
		}
		
		GameUtils.reset = true;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.kickPlayer("§8⫸ §7Server is resetting the game §8⫷");
			
		}
		
		File[] folders = Bukkit.getWorld("lobby").getWorldFolder().listFiles();
		
		new BukkitRunnable() {
			
			public void run() {
		
				for (int i = 0; i < folders.length; i++) {
					
					if (!folders[i].isDirectory()) continue;
					
					if (!folders[i].getName().equalsIgnoreCase("playerdata") && !folders[i].getName().equalsIgnoreCase("stats")) continue;
					
					File[] files = folders[i].listFiles();
					
					for (int y = 0; y < files.length; y++) {
						
						if (files[y].isDirectory()) continue;
						
						files[y].delete();
						
					}
					
				}
				
				new BukkitRunnable() {
				
					public void run() {
					
						GameUtils.reset = false;
				
					}
				
				}.runTask(Main.uhc);
			
			}
		
		}.runTaskAsynchronously(Main.uhc);
		
	}

}

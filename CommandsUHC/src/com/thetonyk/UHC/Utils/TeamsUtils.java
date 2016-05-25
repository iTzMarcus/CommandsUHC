package com.thetonyk.UHC.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Features.DisplayNametags;
import com.thetonyk.UHC.Inventories.PlayerInventory;

public class TeamsUtils {

	public static Map<UUID, String> players = new HashMap<UUID, String>();
	public static Map<UUID, List<UUID>> invitations = new HashMap<UUID, List<UUID>>();
	
	public static void config() {
		
		players.clear();
		invitations.clear();
		PlayerInventory.prefix.clear();
		
		DatabaseUtils.sqlInsert("TRUNCATE TABLE `uhc_teams`;");
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (ChatColor color : ChatColor.values()) {
			
			if (!color.isColor() || color == ChatColor.GRAY) continue;
			
			list.add(color.toString());
			
		}
		
		ArrayList<String> tempList = new ArrayList<String>();
		
		for (ChatColor format : ChatColor.values()) {
			
			if (!format.isFormat() || format == ChatColor.RESET || format == ChatColor.MAGIC) continue;
				
			for (String color : list) {
				
				tempList.add(color + format.toString());
				
			}
			
		}
		
		list.addAll(tempList);
		
		Collections.shuffle(list);
		
		for (int i = 0; i < list.size(); i++) {
			
			String teamName = "UHC" + (i + 1);
			
			final int index = i;
			
			new BukkitRunnable() {
				
				public void run() {
				
					DatabaseUtils.sqlInsert("INSERT INTO uhc_teams (`id`, `name`, `prefix`, `exist`, `members`, `server`) VALUES ('" + (index + 1) + "', '" + teamName + "', '" + list.get(index) + "', 0, '', '" + GameUtils.getServer() + "');");
			
				}
			
			}.runTaskLater(Main.uhc, 2);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
			
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayNametags.updateNametag(player);
					
				}
		
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}
	
	public static void reload() {
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE server = '" + GameUtils.getServer() + "';");
			
			while (teams.next()) {
				
				if (teams.getInt("exist") == 0) continue;
				
				if (teams.getString("members").length() > 0) {
				
					for (String member : teams.getString("members").split(";")) {
						
						players.put(UUID.fromString(member), teams.getString("name"));
						
					}
				
				}
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch all teams.");
			
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
		
			DisplayNametags.updateNametag(player);
			
		}
		
	}
	
	public static void createTeam(UUID player) {
		
		int id = 0;
		String name = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE server = '" + GameUtils.getServer() + "';");
			
			while (teams.next()) {
				
				if (teams.getInt("exist") != 0) continue;
	
				id = teams.getInt("id");
				name = teams.getString("name");
				break;		
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch all teams.");
			
		}
		
		DatabaseUtils.sqlInsert("UPDATE uhc_teams SET exist = 1, members = '" + player + ";' WHERE id = '" + id + "' AND server = '" + GameUtils.getServer() + "';");
		players.put(player, name);
		
		new BukkitRunnable() {
			
			public void run() {
				
				if (Bukkit.getPlayer(player) == null) return;
			
				DisplayNametags.updateNametag(Bukkit.getPlayer(player));
		
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}
	
	public static String getTeam(UUID player) {
		
		if (players.containsKey(player)) return players.get(player);
		
		return null;
		
	}
	
	public static void joinTeam(UUID player, String team) {
		
		String members = null;
		int id = 0;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + GameUtils.getServer() + "';");
			
			if (teams.next()) {
				
				members = teams.getString("members");
				id = teams.getInt("id");
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch team '" + team + "'.");
			
		}
		
		DatabaseUtils.sqlInsert("UPDATE uhc_teams SET members = '" + members + player + ";' WHERE id = '" + id + "' AND server = '" + GameUtils.getServer() + "';");
		players.put(player, team);
		
		if (Bukkit.getPlayer(player) == null) return;
		
		new BukkitRunnable() {
			
			public void run() {
			
				DisplayNametags.updateNametag(Bukkit.getPlayer(player));
		
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}
	
	public static void leaveTeam(UUID player) {
		
		String team = players.get(player);
		String members = null;
		int id = 0;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + GameUtils.getServer() + "';");
				
			if (teams.next()) {
				
				id = teams.getInt("id");
				members = teams.getString("members");
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch team '" + team + "'.");
			
		}
		
		if (members.split(";").length < 2) {
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET exist = 0, members = '' WHERE id = '" + id + "' AND server = '" + GameUtils.getServer() + "';");
			
		} else {
			
			String[] membersList = members.split(";");
			String newMembers = "";
			
			for (int i = 0; i < membersList.length; i++) {
				
				if (!membersList[i].equalsIgnoreCase(player.toString())) newMembers = newMembers + membersList[i] + ";";
				
			}
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET members = '" + newMembers + "' WHERE id = '" + id + "' AND server = '" + GameUtils.getServer() + "';");
			
		}
		
		if (players.containsKey(player)) players.remove(player);
		
		if (Bukkit.getPlayer(player) == null) return;
		
		new BukkitRunnable() {
			
			public void run() {
			
				DisplayNametags.updateNametag(Bukkit.getPlayer(player));
		
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}
	
	public static int getTeamsLeft() {
		
		int count = 0;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE server = '" + GameUtils.getServer() + "';");
			
			while (teams.next()) {
				
				if (teams.getInt("exist") == 0) count++;
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch all teams to count.");
			
		}
		
		return count;
		
	}
	
	public static String getTeamPrefix(UUID player) {
		
		String prefix = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + getTeam(player) + "' AND server = '" + GameUtils.getServer() + "';");
			
			if (teams.next()) prefix = teams.getString("prefix");
				
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch all teams to count.");
			
		}
		
		return prefix;
		
	}
	
	public static void sendMessage(String team, String message) {
		
		String members = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + GameUtils.getServer() + "';");
			
			if (teams.next()) members = teams.getString("members");
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch members of team '" + team + "'.");
			return;
			
		}
		
		if (members == null || members.isEmpty() || members.length() < 1) return;
		
		for (String player : members.split(";")) {
			
			if (Bukkit.getPlayer(UUID.fromString(player)) == null) continue;
				
			Bukkit.getPlayer(UUID.fromString(player)).sendMessage(message);
			
		}
		
	}
	
	public static List<UUID> getTeamMembers(String team) {
		
		List<UUID> list = new ArrayList<UUID>();
		
		for (Map.Entry<UUID, String> entry : players.entrySet()) {
			
			if (!entry.getValue().equalsIgnoreCase(team)) continue;
			
			list.add(entry.getKey());
			
		}

		return list;
		
	}
	
	public static void setColors() {
		
		PlayerInventory.prefix.clear();
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (ChatColor color : ChatColor.values()) {
			
			if (!color.isColor() || color == ChatColor.GRAY) continue;
			
			list.add(color.toString());
			
		}
		
		ArrayList<String> tempList = new ArrayList<String>();
		
		for (ChatColor format : ChatColor.values()) {
			
			if (!format.isFormat() || format == ChatColor.RESET || format == ChatColor.MAGIC) continue;
				
			for (String color : list) {
				
				tempList.add(color + format.toString());
				
			}
			
		}
		
		list.addAll(tempList);
		
		Collections.shuffle(list);
		
		for (int i = 0; i < list.size(); i++) {
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET prefix = '" + list.get(i) + "' WHERE id = " + (i + 1) + " AND server = '" + GameUtils.getServer() + "';");
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
			
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayNametags.updateNametag(player);
					
				}
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}
	
}
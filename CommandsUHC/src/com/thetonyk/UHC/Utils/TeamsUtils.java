package com.thetonyk.UHC.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.MessengerListener;
import com.thetonyk.UHC.Features.DisplayNametags;

public class TeamsUtils {

	public static Map<String, String> players = new HashMap<String, String>();
	public static Map<String, List<String>> invitations = new HashMap<String, List<String>>();
	
	public static void config() {
		
		players.clear();
		invitations.clear();
		
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
			
			DatabaseUtils.sqlInsert("INSERT INTO uhc_teams (`id`, `name`, `prefix`, `exist`, `members`, `server`) VALUES ('" + (i + 1) + "', '" + teamName + "', '" + list.get(i) + "', 0, '', '" + MessengerListener.lastServer + "');");
			
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayNametags.updateNametag(player);
			
		}
		
	}
	
	public static void reload() {
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE server = '" + MessengerListener.lastServer + "';");
			
			while (teams.next()) {
				
				if (teams.getInt("exist") == 0) continue;
				
				for (String member : teams.getString("members").split(";")) {
					
					players.put(member, teams.getString("name"));
					
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
	
	public static void createTeam(String player) {
		
		int id = 0;
		String name = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE server = '" + MessengerListener.lastServer + "';");
			
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
		
		DatabaseUtils.sqlInsert("UPDATE uhc_teams SET exist = 1, members = '" + player + ";' WHERE id = '" + id + "' AND server = '" + MessengerListener.lastServer + "';");
		
		players.put(player, name);
		
		if (Bukkit.getPlayer(player) != null) DisplayNametags.updateNametag(Bukkit.getPlayer(player));
		
	}
	
	public static String getTeam(String player) {
		
		if (players.containsKey(player)) return players.get(player).toString();
		
		return null;
		
	}
	
	public static void joinTeam(String player, String team) {
		
		String members = null;
		int id = 0;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + MessengerListener.lastServer + "';");
			
			if (teams.next()) {
				
				members = teams.getString("members");
				id = teams.getInt("id");
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch team '" + team + "'.");
			
		}
		
		DatabaseUtils.sqlInsert("UPDATE uhc_teams SET members = '" + members + player + ";' WHERE id = '" + id + "' AND server = '" + MessengerListener.lastServer + "';");
		
		players.put(player, team);
		if (Bukkit.getPlayer(player) != null) DisplayNametags.updateNametag(Bukkit.getPlayer(player));
		
	}
	
	public static void leaveTeam(String player) {
		
		String team = players.get(player).toString();
		String members = null;
		int id = 0;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + MessengerListener.lastServer + "';");
				
			if (teams.next()) {
				
				id = teams.getInt("id");
				members = teams.getString("members");
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch team '" + team + "'.");
			
		}
		
		if (members.split(";").length < 2) {
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET exist = 0, members = '' WHERE id = '" + id + "' AND server = '" + MessengerListener.lastServer + "';");
			
		} else {
			
			String[] membersList = members.split(";");
			String newMembers = "";
			
			for (int i = 0; i < membersList.length; i++) {
				
				if (!membersList[i].equalsIgnoreCase(player)) newMembers = newMembers + membersList[i] + ";";
				
			}
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET members = '" + newMembers + "' WHERE id = '" + id + "' AND server = '" + MessengerListener.lastServer + "';");
			
		}
		
		if (players.containsKey(player)) players.remove(player);
		
		if (Bukkit.getPlayer(player) != null) DisplayNametags.updateNametag(Bukkit.getPlayer(player));
		
	}
	
	public static int getTeamsLeft() {
		
		int count = 0;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE server = '" + MessengerListener.lastServer + "';");
			
			while (teams.next()) {
				
				if (teams.getInt("exist") == 0) count++;
				
			}
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch all teams to count.");
			
		}
		
		return count;
		
	}
	
	public static String getTeamPrefix(String player) {
		
		String prefix = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + getTeam(player) + "' AND server = '" + MessengerListener.lastServer + "';");
			
			if (teams.next()) prefix = teams.getString("prefix") + "§r";
				
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch all teams to count.");
			
		}
		
		return prefix;
		
	}
	
	public static void sendMessage(String team, String message) {
		
		String members = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + MessengerListener.lastServer + "';");
			
			if (teams.next()) members = teams.getString("members");
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamsUtils] Error to fetch members of team '" + team + "'.");
			return;
			
		}
		
		if (members.isEmpty()) return;
		
		for (String player : members.split(";")) {
			
			if (Bukkit.getPlayer(player) == null) continue;
				
			Bukkit.getPlayer(player).sendMessage(message);
			
		}
		
	}
	
	public static List<String> getTeamMembers(String team) {
		
		String members = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + MessengerListener.lastServer + "';");
			
			if (teams.next()) members = teams.getString("members");
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamUtils] Error to fetch all teams.");
			return null;
			
		}
		
		List<String> list = new ArrayList<String>();
		
		for (String member : members.split(";")) {
			
			list.add(member);
			
		}

		return list;
		
	}
	
	public static void setColors() {
		
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
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET prefix = '" + list.get(i) + "' WHERE id = " + (i + 1) + " AND server = '" + MessengerListener.lastServer + "';");
			
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayNametags.updateNametag(player);
			
		}
		
	}
	
}

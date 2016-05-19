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
import com.thetonyk.UHC.Utils.DatabaseUtils.Callback;

public class TeamsUtils {

	public static Map<UUID, String> players = new HashMap<UUID, String>();
	public static Map<UUID, List<UUID>> invitations = new HashMap<UUID, List<UUID>>();
	
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
			
			DatabaseUtils.sqlInsert("INSERT INTO uhc_teams (`id`, `name`, `prefix`, `exist`, `members`, `server`) VALUES ('" + (i + 1) + "', '" + teamName + "', '" + list.get(i) + "', 0, '', '" + GameUtils.getServer() + "');");
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
		
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayNametags.updateNametag(player);
					
				}
		
			}
		
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	public static void reload() {
		
		DatabaseUtils.asynSqlQuery("SELECT * FROM uhc_teams WHERE server = '" + GameUtils.getServer() + "';", new Callback<List<Map<String, String>>>() {

			@Override
			public void onSuccess(List<Map<String, String>> results) {
				
				for (Map<String, String> result : results) {
					
					if (Integer.parseInt(result.get("exist")) == 0) continue;
					
					for (String member : result.get("members").split(";")) {
						
						players.put(UUID.fromString(member), result.get("name"));
						
					}
					
				}
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayNametags.updateNametag(player);
					
				}
				
			}

			@Override
			public void onFailure(Throwable cause) {
				
				
				
			}
			
		});
		
	}
	
	public static void createTeam(UUID player) {
		
		DatabaseUtils.asynSqlQuery("SELECT * FROM uhc_teams WHERE server = '" + GameUtils.getServer() + "';", new Callback<List<Map<String, String>>>() {

			@Override
			public void onSuccess(List<Map<String, String>> results) {
				
				for (Map<String, String> result : results) {
					
					if (Integer.parseInt(result.get("exist")) != 0) continue;
					
					DatabaseUtils.sqlInsert("UPDATE uhc_teams SET exist = 1, members = '" + player + ";' WHERE id = '" + Integer.parseInt(result.get("id")) + "' AND server = '" + GameUtils.getServer() + "';");
					players.put(player, result.get("name"));
					
					if (Bukkit.getPlayer(player) == null) break;
					
					new BukkitRunnable() {

						public void run() {
							
							DisplayNametags.updateNametag(Bukkit.getPlayer(player));
							
						}
						
					}.runTaskLater(Main.uhc, 1);
					
					break;
					
				}
				
			}

			@Override
			public void onFailure(Throwable cause) {
				
				
				
			}
			
		});
		
	}
	
	public static String getTeam(UUID player) {
		
		return players.containsKey(player) ? players.get(player) : null;
		
	}
	
	public static void joinTeam(UUID player, String team) {
		
		DatabaseUtils.asynSqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + GameUtils.getServer() + "';", new Callback<List<Map<String, String>>>() {

			@Override
			public void onSuccess(List<Map<String, String>> results) {
				
				for (Map<String, String> result : results) {
					
					DatabaseUtils.sqlInsert("UPDATE uhc_teams SET members = '" + result.get("members") + player + ";' WHERE id = '" + Integer.parseInt(result.get("id")) + "' AND server = '" + GameUtils.getServer() + "';");
					players.put(player, team);
					
					if (Bukkit.getPlayer(player) == null) break;
					
					new BukkitRunnable() {

						public void run() {
							
							DisplayNametags.updateNametag(Bukkit.getPlayer(player));
							
						}
						
					}.runTaskLater(Main.uhc, 1);
					
					break;
					
				}
				
			}

			@Override
			public void onFailure(Throwable cause) {
				
				
				
			}
			
		});
		
	}
	
	public static void leaveTeam(UUID player) {
		
		DatabaseUtils.asynSqlQuery("SELECT * FROM uhc_teams WHERE name = '" + players.get(player) + "' AND server = '" + GameUtils.getServer() + "';", new Callback<List<Map<String, String>>>() {

			@Override
			public void onSuccess(List<Map<String, String>> results) {
				
				for (Map<String, String> result : results) {
					
					String members = result.get("members");
					
					if (members.split(";").length < 2) {
						
						DatabaseUtils.sqlInsert("UPDATE uhc_teams SET exist = 0, members = '' WHERE id = '" + Integer.parseInt(result.get("id")) + "' AND server = '" + GameUtils.getServer() + "';");
						
					} else {
						
						String[] membersList = members.split(";");
						String newMembers = "";
						
						for (int i = 0; i < membersList.length; i++) {
							
							if (!membersList[i].equalsIgnoreCase(player.toString())) newMembers = newMembers + membersList[i] + ";";
							
						}
						
						DatabaseUtils.sqlInsert("UPDATE uhc_teams SET members = '" + newMembers + "' WHERE id = '" + Integer.parseInt(result.get("id")) + "' AND server = '" + GameUtils.getServer() + "';");
						
					}
					
					if (players.containsKey(player)) players.remove(player);
					
					if (Bukkit.getPlayer(player) == null) break;
					
					new BukkitRunnable() {

						public void run() {
							
							DisplayNametags.updateNametag(Bukkit.getPlayer(player));
							
						}
						
					}.runTaskLater(Main.uhc, 1);
					
					break;
					
				}
				
			}

			@Override
			public void onFailure(Throwable cause) {
				
				
				
			}
			
		});
		
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
		
		DatabaseUtils.asynSqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + GameUtils.getServer() + "';", new Callback<List<Map<String, String>>>() {

			@Override
			public void onSuccess(List<Map<String, String>> results) {
				
				for (Map<String, String> result : results) {
					
					if (result.get("members").length() < 1) return;
					
					for (String player : result.get("members").split(";")) {
						
						if (Bukkit.getPlayer(UUID.fromString(player)) == null) continue;
						
						Bukkit.getPlayer(UUID.fromString(player)).sendMessage(message);
						return;
						
					}
					
				}
				
			}

			@Override
			public void onFailure(Throwable cause) {
				
				
				
			}
			
		});
		
	}
	
	public static List<UUID> getTeamMembers(String team) {
		
		String members = null;
		
		try {
			
			ResultSet teams = DatabaseUtils.sqlQuery("SELECT * FROM uhc_teams WHERE name = '" + team + "' AND server = '" + GameUtils.getServer() + "';");
			
			if (teams.next()) members = teams.getString("members");
			
			teams.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[TeamUtils] Error to fetch all teams.");
			return null;
			
		}
		
		List<UUID> list = new ArrayList<UUID>();
		
		for (String member : members.split(";")) {
			
			if (member.length() < 1) continue;
			
			list.add(UUID.fromString(member));
			
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
			
			DatabaseUtils.sqlInsert("UPDATE uhc_teams SET prefix = '" + list.get(i) + "' WHERE id = " + (i + 1) + " AND server = '" + GameUtils.getServer() + "';");
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
		
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayNametags.updateNametag(player);
					
				}
		
			}
		
		}.runTaskLater(Main.uhc, 1);
		
	}
	
}

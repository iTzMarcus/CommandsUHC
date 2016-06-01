package com.thetonyk.UHC.Features;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class DisplayNametags implements Listener {
	
	public DisplayNametags() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayNametags.updateNametag(player);
			
		}
		
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		updateNametag(event.getPlayer());
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			Team team = online.getScoreboard().getTeam(event.getPlayer().getName());
			
			if (team == null) continue;
				
			team.unregister();
			
		}
		
	}
	
	public static void updateNametag(Player player) {
		
		UUID uuid = player.getUniqueId();
		String fixedRank = PlayerUtils.getRank(uuid).getPrefix();
		if (fixedRank.length() > 12) fixedRank.replaceAll("§8", "");
		String prefix = fixedRank + ((TeamsUtils.getTeam(uuid) != null) ? TeamsUtils.getTeamPrefix(uuid) : "§7");
		String teamDisplayName = TeamsUtils.getTeam(uuid) == null ? player.getName() : TeamsUtils.getTeam(uuid);
		Map<UUID, Integer> ids = GameUtils.getIDs();
		String teamName = TeamsUtils.getTeam(uuid) == null ? String.valueOf(ids.get(uuid)) : TeamsUtils.getTeam(uuid) + "-" + String.valueOf(ids.get(uuid));
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			Scoreboard scoreboard = players.getScoreboard();
			Team team = scoreboard.getTeam(teamName);
			
			if (team == null) team = scoreboard.registerNewTeam(teamName);
			
			team.setDisplayName(teamDisplayName);
			team.setPrefix(prefix);
			team.setSuffix("§7");
			team.addEntry(player.getName());
			
			if (players.equals(player)) continue;
	
			UUID playerUUID = players.getUniqueId();
			String playerFixedRank = PlayerUtils.getRank(playerUUID).getPrefix();
			if (playerFixedRank.length() > 12) playerFixedRank.replaceAll("§8", "");
			String playerPrefix = playerFixedRank + ((TeamsUtils.getTeam(playerUUID) != null) ? TeamsUtils.getTeamPrefix(playerUUID) : "§7");
			String playerTeamDisplayName = TeamsUtils.getTeam(playerUUID) == null ? players.getName() : TeamsUtils.getTeam(playerUUID);
			String playerTeamName = TeamsUtils.getTeam(playerUUID) == null ? String.valueOf(ids.get(playerUUID)) : TeamsUtils.getTeam(playerUUID) + "-" + String.valueOf(ids.get(playerUUID));
			
			scoreboard = player.getScoreboard();
			team = scoreboard.getTeam(playerTeamName);
			
			if (team == null) team = scoreboard.registerNewTeam(playerTeamName);
			
			team.setDisplayName(playerTeamDisplayName);
			team.setPrefix(playerPrefix);
			team.setSuffix("§7");
			team.addEntry(players.getName());
		
		}
		
		if (GameUtils.getSpectate(uuid)) player.setPlayerListName("§r" + PlayerUtils.getRank(uuid).getPrefix() + "§7§o" + player.getName());
		else player.setPlayerListName(PlayerUtils.getRank(uuid).getPrefix() + ((TeamsUtils.getTeam(uuid) != null) ? TeamsUtils.getTeamPrefix(uuid) : "§7") + player.getName());
		
	}
	
}

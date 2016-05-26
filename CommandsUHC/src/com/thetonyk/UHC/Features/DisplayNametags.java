package com.thetonyk.UHC.Features;

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
		String prefix = PlayerUtils.getRank(uuid).getPrefix() + ((TeamsUtils.getTeam(uuid) != null) ? TeamsUtils.getTeamPrefix(uuid) : "§7");
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			Scoreboard scoreboard = players.getScoreboard();
			Team team = scoreboard.getTeam(player.getName());
			
			if (team == null) {
				
				team = scoreboard.registerNewTeam(player.getName());
				team.setDisplayName(player.getName());
				
			}
			
			team.setPrefix(prefix);
			team.setSuffix("§7");
			team.addEntry(player.getName());
			
			if (players.equals(player)) continue;
			
			scoreboard = player.getScoreboard();
			team = scoreboard.getTeam(players.getName());
			UUID playerUUID = players.getUniqueId();
			String playerPrefix = PlayerUtils.getRank(playerUUID).getPrefix() + ((TeamsUtils.getTeam(playerUUID) != null) ? TeamsUtils.getTeamPrefix(playerUUID) : "§7");
			
			if (team == null) {
				
				team = scoreboard.registerNewTeam(players.getName());
				team.setDisplayName(players.getName());
				
			}
			
			team.setPrefix(playerPrefix);
			team.setSuffix("§7");
			team.addEntry(players.getName());
		
		}
		
		if (GameUtils.getSpectate(uuid)) player.setPlayerListName("§r" + PlayerUtils.getRank(uuid).getPrefix() + "§7§o" + player.getName());
		else player.setPlayerListName(prefix + player.getName());
		
	}
	
}

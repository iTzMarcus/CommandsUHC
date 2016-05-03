package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.NameTagVisibility;

import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class TeamsNametags implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		updateNametag(event.getPlayer());
		
	}
	
	public void onLeave(PlayerQuitEvent event) {
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			if (online.getScoreboard().getTeam(event.getPlayer().getName()) == null) continue;
				
			online.getScoreboard().getTeam(event.getPlayer().getName()).unregister();
			
		}
		
	}
	
	public static void updateNametag(Player player) {
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			if (players.getScoreboard().getTeam(player.getName()) == null) {
				
				players.getScoreboard().registerNewTeam(player.getName());
				players.getScoreboard().getTeam(player.getName()).setAllowFriendlyFire(true);
				players.getScoreboard().getTeam(player.getName()).setCanSeeFriendlyInvisibles(true);
				players.getScoreboard().getTeam(player.getName()).setDisplayName(player + " team");
				players.getScoreboard().getTeam(player.getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
				
			}
			
			players.getScoreboard().getTeam(player.getName()).setPrefix(PlayerUtils.getRank(player.getName()).getPrefix() + ((TeamsUtils.getTeam(player.getName()) != null) ? TeamsUtils.getTeamPrefix(player.getName()) : "§7"));
			players.getScoreboard().getTeam(player.getName()).setSuffix("§f");
			players.getScoreboard().getTeam(player.getName()).addEntry(player.getName());
			
			if (player.getScoreboard().getTeam(players.getName()) == null) {
				
				player.getScoreboard().registerNewTeam(players.getName());
				player.getScoreboard().getTeam(players.getName()).setAllowFriendlyFire(true);
				player.getScoreboard().getTeam(players.getName()).setCanSeeFriendlyInvisibles(true);
				player.getScoreboard().getTeam(players.getName()).setDisplayName(players.getName() + " team");
				player.getScoreboard().getTeam(players.getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
				
			}
			
			player.getScoreboard().getTeam(players.getName()).setPrefix(PlayerUtils.getRank(players.getName()).getPrefix() + ((TeamsUtils.getTeam(players.getName()) != null) ? TeamsUtils.getTeamPrefix(players.getName()) : "§7"));
			player.getScoreboard().getTeam(players.getName()).setSuffix("§f");
			player.getScoreboard().getTeam(players.getName()).addEntry(players.getName());
		
		}
		
	}
	
}

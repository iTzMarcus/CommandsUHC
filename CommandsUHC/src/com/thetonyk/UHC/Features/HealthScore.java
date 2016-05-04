package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.thetonyk.UHC.Main;

public class HealthScore implements Listener {
	
	public HealthScore() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.getScoreboard().registerNewObjective("below", "dummy");
			player.getScoreboard().getObjective("below").setDisplaySlot(DisplaySlot.BELOW_NAME);
			player.getScoreboard().getObjective("below").setDisplayName("§4♥");
			player.getScoreboard().registerNewObjective("list", "dummy");
			player.getScoreboard().getObjective("list").setDisplaySlot(DisplaySlot.PLAYER_LIST);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player scoreboard : Bukkit.getOnlinePlayers()) {
				
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						scoreboard.getScoreboard().getObjective("below").getScore(player.getName()).setScore((int) (((player.getHealth()) / 2) * 10));
						scoreboard.getScoreboard().getObjective("list").getScore(player.getName()).setScore((int) (((player.getHealth()) / 2) * 10));
						
					}
					
				}
				
			}
			
		}.runTaskTimer(Main.uhc, 1, 1);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		event.getPlayer().getScoreboard().registerNewObjective("below", "dummy");
		event.getPlayer().getScoreboard().getObjective("below").setDisplaySlot(DisplaySlot.BELOW_NAME);
		event.getPlayer().getScoreboard().getObjective("below").setDisplayName("§4♥");
		event.getPlayer().getScoreboard().registerNewObjective("list", "dummy");
		event.getPlayer().getScoreboard().getObjective("list").setDisplaySlot(DisplaySlot.PLAYER_LIST);
		
	}

}

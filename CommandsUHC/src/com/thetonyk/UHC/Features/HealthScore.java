package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.thetonyk.UHC.Main;

public class HealthScore implements Listener {
	
	public HealthScore() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			update(player);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					Scoreboard scoreboard = player.getScoreboard();
					Objective below = scoreboard.getObjective("below");
					Objective list = scoreboard.getObjective("list");
				
					for (Player score : Bukkit.getOnlinePlayers()) {
						
						int health = (int) ((score.getHealth() / 2) * 10);
						below.getScore(score.getName()).setScore(health);
						list.getScore(score.getName()).setScore(health);
						
					}
					
				}
				
			}
			
		}.runTaskTimer(Main.uhc, 1, 1);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		update(event.getPlayer());
		
	}
	
	private static void update(Player player) {
		
		Scoreboard scoreboard = player.getScoreboard();
		Objective below = scoreboard.getObjective("below");
		Objective list = scoreboard.getObjective("list");
		
		if (below == null) {
			
			below = scoreboard.registerNewObjective("below", "dummy");
			below.setDisplaySlot(DisplaySlot.BELOW_NAME);
			below.setDisplayName("§4♥");
			
		}
		
		if (list == null) {
			
			list = scoreboard.registerNewObjective("list", "dummy");
			list.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			list.setDisplayName("§4♥");
			
		}
		
	}

}

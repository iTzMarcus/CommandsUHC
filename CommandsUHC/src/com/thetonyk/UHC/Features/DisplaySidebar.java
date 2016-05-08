package com.thetonyk.UHC.Features;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class DisplaySidebar implements Listener {
	
	private static int pve = 0;
	private static Map<UUID, Integer> kills = new HashMap<UUID, Integer>();
	
	public DisplaySidebar() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.getScoreboard().registerNewObjective("sidebar", "dummy");
			player.getScoreboard().getObjective("sidebar").setDisplaySlot(DisplaySlot.SIDEBAR);
			player.getScoreboard().getObjective("sidebar").setDisplayName("§a§lUHC §8⫸ §7CommandsPVP");
			update(player);
			
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		event.getPlayer().getScoreboard().registerNewObjective("sidebar", "dummy");
		event.getPlayer().getScoreboard().getObjective("sidebar").setDisplaySlot(DisplaySlot.SIDEBAR);
		event.getPlayer().getScoreboard().getObjective("sidebar").setDisplayName("§a§lUHC §8⫸ §7CommandsPVP");
		update(event.getPlayer());
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		if (!GameUtils.getWorld().equalsIgnoreCase(event.getEntity().getWorld().getName())) return;
		
		if (event.getEntity().getKiller() == null) {
			
			pve++;
			return;
			
		}
		
		if (TeamsUtils.getTeam(event.getEntity().getName()) != null &&TeamsUtils.getTeam(event.getEntity().getName()).equalsIgnoreCase(event.getEntity().getKiller().getName())) return;
		
		kills.put(event.getEntity().getKiller().getUniqueId(), (kills.containsKey(event.getEntity().getKiller().getUniqueId())) ? (kills.get(event.getEntity().getKiller().getUniqueId()) + 1) : 1);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			update(player);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				update(event.getEntity());
				
			}
			
		}.runTaskLater(Main.uhc, 10);
		
	}
	
	public static void update(Player player) {
		
		for (String entry : player.getScoreboard().getEntries()) {
			
			if (!entry.startsWith("  ")) continue;
			
			player.getScoreboard().resetScores(entry);
			
		}
		
		if (GameUtils.getStatus() != Status.PLAY && GameUtils.getStatus() != Status.END) {

			player.getScoreboard().getObjective("sidebar").getScore("   ").setScore(5);
			player.getScoreboard().getObjective("sidebar").getScore("  §6Help").setScore(4);
			player.getScoreboard().getObjective("sidebar").getScore("    §8⫸ §7/rules").setScore(3);
			player.getScoreboard().getObjective("sidebar").getScore("    §8⫸ §7/helpop").setScore(2);
			player.getScoreboard().getObjective("sidebar").getScore("  ").setScore(1);
			player.getScoreboard().getObjective("sidebar").getScore("  §b@CommandsPVP").setScore(0);
			return;
			
		}
		
		player.getScoreboard().getObjective("sidebar").getScore("    ").setScore(pve + 1);
		player.getScoreboard().getObjective("sidebar").getScore("  §6PVE §8⫸ §a" + pve).setScore(pve);
		
		for (UUID killer : kills.keySet()) {
			
			player.getScoreboard().getObjective("sidebar").getScore("  §8⫸ " + PlayerUtils.getRank(PlayerUtils.getName(PlayerUtils.getId(killer))).getPrefix() + ((TeamsUtils.getTeam(PlayerUtils.getName(PlayerUtils.getId(killer))) != null) ? TeamsUtils.getTeamPrefix(PlayerUtils.getName(PlayerUtils.getId(killer))) : "§7") + PlayerUtils.getName(PlayerUtils.getId(killer))).setScore(kills.get(killer));
			
		}
		
		player.getScoreboard().getObjective("sidebar").getScore("  ").setScore(-1);
		player.getScoreboard().getObjective("sidebar").getScore("  §b@CommandsPVP").setScore(-2);
		
	}

}

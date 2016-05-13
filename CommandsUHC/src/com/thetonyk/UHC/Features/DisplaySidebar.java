package com.thetonyk.UHC.Features;

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
		
		if (GameUtils.getStatus() != Status.PLAY || GameUtils.getDeath(event.getEntity().getUniqueId())) return;
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					update(player);
					
				}
				
			}
			
		}.runTaskLater(Main.uhc, 10);
		
		if (event.getEntity().getKiller() == null) {
			
			addPVE();
			return;
			
		}
		
		if (TeamsUtils.getTeam(event.getEntity().getName()) != null &&TeamsUtils.getTeam(event.getEntity().getName()).equalsIgnoreCase(event.getEntity().getKiller().getName())) return;
		
		if (event.getEntity().getKiller().equals(event.getEntity())) return;
		
		Map<UUID, Integer> kills = GameUtils.getKills();
		
		kills.put(event.getEntity().getKiller().getUniqueId(), (kills.containsKey(event.getEntity().getKiller().getUniqueId())) ? (kills.get(event.getEntity().getKiller().getUniqueId()) + 1) : 1);
		
		GameUtils.setKills(kills);
		
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
		
		player.getScoreboard().getObjective("sidebar").getScore("    ").setScore(99);
		player.getScoreboard().getObjective("sidebar").getScore("  §6PVE §8⫸ §a" + GameUtils.getPVE()).setScore(98);
		
		for (UUID killer : GameUtils.getKills().keySet()) {
			
			player.getScoreboard().getObjective("sidebar").getScore("  " + (GameUtils.getDeath(killer) ? "§c☠ " : "  ") + PlayerUtils.getRank(PlayerUtils.getName(PlayerUtils.getId(killer))).getPrefix() + ((TeamsUtils.getTeam(PlayerUtils.getName(PlayerUtils.getId(killer))) != null) ? TeamsUtils.getTeamPrefix(PlayerUtils.getName(PlayerUtils.getId(killer))) : "§7") + PlayerUtils.getName(PlayerUtils.getId(killer))).setScore(GameUtils.getKills().get(killer));
			
		}
		
		player.getScoreboard().getObjective("sidebar").getScore("  ").setScore(-1);
		player.getScoreboard().getObjective("sidebar").getScore("  §b@CommandsPVP").setScore(-2);
		
	}
	
	public static void addPVE() {
		
		GameUtils.setPVE(GameUtils.getPVE() + 1);
		
	}

}

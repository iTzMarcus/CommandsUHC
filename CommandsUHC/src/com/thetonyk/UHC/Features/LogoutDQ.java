package com.thetonyk.UHC.Features;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class LogoutDQ implements Listener {

	private static Map<UUID, BukkitRunnable> offlineTimers = new HashMap<UUID, BukkitRunnable>();
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		if (!GameUtils.getWorld().equalsIgnoreCase(event.getPlayer().getWorld().getName()))
		
		if (!event.getPlayer().isWhitelisted()) return;
		
		BukkitRunnable counter = new BukkitRunnable() {
			
			public void run() {
				
				event.getPlayer().setWhitelisted(false);
				Bukkit.broadcastMessage(Main.PREFIX + PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getPlayer().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getPlayer().getName()) : "§7") + event.getPlayer().getName() + "§7" + " died offline");
				DisplaySidebar.addPve();
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplaySidebar.update(player);
					
					if (PlayerUtils.getNosoundState(player) == 1) continue;
					
					player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
					
				}
				
				new BukkitRunnable() {
					
					public void run() {
						
						Bukkit.broadcastMessage(Main.PREFIX + "There are §a" + Bukkit.getWhitelistedPlayers().size() + " §7players alive.");
					
					}
					
				}.runTaskLater(Main.uhc, 1);
				
				offlineTimers.put(event.getPlayer().getUniqueId(), null);
				
			}
			
		};
		
		int time = DisplayTimers.getTimeLeftMeetup() > 0 ? 18000 : 6000;
		
		offlineTimers.put(event.getPlayer().getUniqueId(), counter);
		offlineTimers.get(event.getPlayer().getUniqueId()).runTaskLater(Main.uhc, time);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		if (!event.getPlayer().isWhitelisted()) return;
		
		if (!offlineTimers.containsKey(event.getPlayer().getUniqueId()) || offlineTimers.get(event.getPlayer().getUniqueId()) == null) return;
		
		offlineTimers.get(event.getPlayer().getUniqueId()).cancel();
		offlineTimers.put(event.getPlayer().getUniqueId(), null);
		
	}
	
	public static void reset() {
		
		for (UUID uuid : offlineTimers.keySet()) {
			
			if (offlineTimers.get(uuid) == null) continue;
			
			offlineTimers.get(uuid).cancel();
			
		}
		
		offlineTimers.clear();
		
	}
	
}

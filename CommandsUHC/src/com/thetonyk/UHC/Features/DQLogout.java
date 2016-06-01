package com.thetonyk.UHC.Features;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.StartEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class DQLogout implements Listener {

	private static Map<UUID, BukkitRunnable> offlineTimers = new HashMap<UUID, BukkitRunnable>();
	private static Map<UUID, Long> offlineTime = new HashMap<UUID, Long>();
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		Status status = GameUtils.getStatus();
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (status != Status.PLAY) return;
		
		if (GameUtils.getDeath(uuid)) return;
		
		startTimer(player);
		
	}
	
	@EventHandler
	public void onStart(StartEvent event) {
		
		for (UUID player : GameUtils.getPlayers().keySet()) {
			
			if (GameUtils.getDeath(player)) continue;
			
			OfflinePlayer offline = Bukkit.getOfflinePlayer(player);
			
			if (offline.isOnline()) continue;
			
			startTimer(offline);
			
		}
		
	}
	
	@EventHandler
	public void onEnable(PluginEnableEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
				
		for (UUID player : GameUtils.getAlives()) {
			
			if (Bukkit.getPlayer(player) != null) continue;
				
			DQLogout.startTimer(Bukkit.getOfflinePlayer(player));
			
		}
		
		DisplayTimers.startTimer();
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Status status = GameUtils.getStatus();
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (status != Status.PLAY) return;
		
		if (GameUtils.getDeath(uuid)) return;
		
		if (!offlineTimers.containsKey(uuid) || offlineTimers.get(uuid) == null) return;
		
		offlineTimers.get(uuid).cancel();
		offlineTime.remove(uuid);
		
	}
	
	public static void reset() {
		
		for (UUID uuid : offlineTimers.keySet()) {
			
			if (offlineTimers.get(uuid) == null) continue;
			
			offlineTimers.get(uuid).cancel();
			
		}
		
		offlineTimers.clear();
		offlineTime.clear();
		
	}
	
	public static void startTimer(OfflinePlayer player) {
		
		UUID uuid = player.getUniqueId();
		
		BukkitRunnable counter = new BukkitRunnable() {
		
			public void run() {
				
				player.setWhitelisted(false);
				offlineTimers.remove(uuid);
				offlineTime.remove(uuid);
				
				if (GameUtils.getDeath(uuid)) return;
				
				GameUtils.setDeath(uuid, true);
				Bukkit.broadcastMessage(Main.PREFIX + PlayerUtils.getRank(uuid).getPrefix() + ((TeamsUtils.getTeam(uuid) != null) ? TeamsUtils.getTeamPrefix(uuid) : "§7") + player.getName() + "§7" + " died offline");
				DisplaySidebar.addPVE();
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplaySidebar.update(player);
					
					if (PlayerUtils.getNosoundState(player) == 1) continue;
					
					player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
					
				}
				
				Bukkit.broadcastMessage(Main.PREFIX + "There are §a" + GameUtils.getAlives().size() + " §7players alive.");
				
			}
			
		};
		
		int time = 0;
		if (DisplayTimers.getTimeLeftMeetup() > 300) time = 18000;
		else if (DisplayTimers.getTimeLeftMeetup() > 0) time = 12000;
		else time = 600;
		
		long date = new Date().getTime();
		
		offlineTime.put(uuid, date);
		offlineTimers.put(uuid, counter);
		offlineTimers.get(uuid).runTaskLater(Main.uhc, time);
			
	}
	
	public static int offlineSince(UUID uuid) {
		
		if (!offlineTime.containsKey(uuid)) return 0;
		
		long date = new Date().getTime();
		long time = date - offlineTime.get(uuid);
		int seconds = (int) time / 1000;
		
		return seconds;
		
	}
	
}

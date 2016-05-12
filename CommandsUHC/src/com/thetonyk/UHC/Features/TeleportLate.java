package com.thetonyk.UHC.Features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.StartEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class TeleportLate implements Listener {
	
	List<UUID> players = new ArrayList<UUID>();
	BukkitRunnable start = null;
	Map<UUID, BukkitRunnable> timers = new HashMap<UUID, BukkitRunnable>();
	
	@EventHandler
	public void onStart(StartEvent event) {
		
		players.clear();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			players.add(player.getUniqueId());
			
		}
		
		start = new BukkitRunnable() {
			
			public void run() {
				
				for (UUID uuid : new ArrayList<UUID>(players)) {
					
					GameUtils.setOnGround(uuid, true);;
					players.remove(uuid);
					
				}
				
				start = null;
				
			}
			
		};
		
		start.runTaskLater(Main.uhc, 600);
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		if (GameUtils.getOnGround(event.getPlayer().getUniqueId())) return;
		
		if (start != null) {
		
			if (players.contains(event.getPlayer().getUniqueId())) {
			
				players.remove(event.getPlayer().getUniqueId());
			
			}
			
		}
		
		if (timers.containsKey(event.getPlayer().getUniqueId())) {
			
			timers.get(event.getPlayer().getUniqueId()).cancel();
			timers.put(event.getPlayer().getUniqueId(), null);
			timers.remove(event.getPlayer().getUniqueId());
			
		}
		
		
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		if (GameUtils.getOnGround(event.getPlayer().getUniqueId())) return;
		
		timers.put(event.getPlayer().getUniqueId(), new BukkitRunnable() {
			
			public void run() {
				
				GameUtils.setOnGround(event.getPlayer().getUniqueId(), true);;
				timers.put(event.getPlayer().getUniqueId(), null);
				timers.remove(event.getPlayer().getUniqueId());
				
			}
			
		});
		
		timers.get(event.getPlayer().getUniqueId()).runTaskLater(Main.uhc, 600);
		
	}
 
}

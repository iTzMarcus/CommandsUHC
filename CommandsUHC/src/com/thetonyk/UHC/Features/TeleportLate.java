package com.thetonyk.UHC.Features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.StartEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.TeleportUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class TeleportLate implements Listener {
	
	List<UUID> players = new ArrayList<UUID>();
	BukkitRunnable start = null;
	Map<UUID, BukkitRunnable> timers = new HashMap<UUID, BukkitRunnable>();
	
	@EventHandler
	public void onStart(StartEvent event) {
		
		players.clear();
		List<UUID> teleport = new ArrayList<UUID>();
		
		for (UUID player : GameUtils.getAlives()) {
			
			if (Bukkit.getPlayer(player) == null) continue;
			
			if (GameUtils.getPlayers().containsKey(player) && !GameUtils.getTeleported(player)) {
				
				teleport.add(player);
				
			}
			
			players.add(player);
			
		}
		
		List<Map.Entry<String, ?>> uuids = new ArrayList<Map.Entry<String, ?>>();
		
		for (UUID player : teleport) {
		
			Map.Entry<String, UUID> uuid = new Map.Entry<String, UUID>() {
			
				UUID uuid = Bukkit.getPlayer(player).getUniqueId();

				@Override
				public String getKey() {
					
					return "uuid";
					
				}

				@Override
				public UUID getValue() {
					
					return this.uuid;
					
				}

				@Override
				public UUID setValue(UUID uuid) {
				
					this.uuid = uuid;
					return this.uuid;
					
				}
			
			};
					
			uuids.add(uuid);
			
		}
		
		TeleportUtils.loadSpawnsAndTeleport(uuids);
		
		start = new BukkitRunnable() {
			
			public void run() {
				
				for (UUID uuid : new ArrayList<UUID>(players)) {
					
					GameUtils.setOnGround(uuid, true);
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
		
		if (GameUtils.getStatus() != Status.PLAY || !GameUtils.getPlayers().containsKey(event.getPlayer().getUniqueId())) return;
		
		if (!GameUtils.getTeleported(event.getPlayer().getUniqueId())) {
			
			List<Map.Entry<String, ?>> uuids = new ArrayList<Map.Entry<String, ?>>();
			
			event.getPlayer().setWhitelisted(true);
			if (GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY) GameUtils.addPlayer(event.getPlayer().getUniqueId());
			
			Map.Entry<String, UUID> uuid = new Map.Entry<String, UUID>() {
				
				UUID uuid = event.getPlayer().getUniqueId();

				@Override
				public String getKey() {
					
					return "uuid";
					
				}

				@Override
				public UUID getValue() {
					
					return this.uuid;
					
				}

				@Override
				public UUID setValue(UUID uuid) {
				
					this.uuid = uuid;
					return this.uuid;
					
				}
			
			};
			
			uuids.add(uuid);
			TeleportUtils.loadSpawnsAndTeleport(uuids);
			
			new BukkitRunnable() {
				
				public void run() {
					
					Bukkit.broadcastMessage(Main.PREFIX + "Teleporting '§6" + event.getPlayer().getName() + "§7'...");
					
				}
				
			}.runTaskLater(Main.uhc, 1);
			
		}
		
		if (!GameUtils.getOnGround(event.getPlayer().getUniqueId())) {
		
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
 
}

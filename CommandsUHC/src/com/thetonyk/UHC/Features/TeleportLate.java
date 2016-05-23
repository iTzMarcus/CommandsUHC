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
import com.thetonyk.UHC.Utils.TeleportUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class TeleportLate implements Listener {
	
	List<UUID> players = new ArrayList<UUID>();
	BukkitRunnable start = null;
	Map<UUID, BukkitRunnable> timers = new HashMap<UUID, BukkitRunnable>();
	
	@EventHandler
	public void onStart(StartEvent event) {
		
		players.clear();
		List<UUID> alives = GameUtils.getAlives();
		
		for (UUID player : alives) {
			
			if (Bukkit.getPlayer(player) == null) continue;
			
			players.add(player);
			
		}
		
		List<Map.Entry<String, ?>> uuids = new ArrayList<Map.Entry<String, ?>>();
		
		for (UUID player : alives) {
			
			if (Bukkit.getPlayer(player) == null) continue;
			
			if (GameUtils.getTeleported(player)) continue;
		
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
					
					if (Bukkit.getPlayer(uuid) == null) continue;
					
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
		
		UUID uuid = event.getPlayer().getUniqueId();
		Status status = GameUtils.getStatus();
		
		if (status != Status.PLAY) return;
		
		if (GameUtils.getOnGround(uuid)) return;
		
		if (start != null && players.contains(uuid)) players.remove(uuid);
		
		if (timers.containsKey(uuid)) {
			
			timers.get(uuid).cancel();
			timers.remove(uuid);
			
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Status status = GameUtils.getStatus();
		
		if (status != Status.PLAY || GameUtils.getSpectate(uuid) || GameUtils.getDeath(uuid) ||!player.isWhitelisted()) return;
		
		if (!GameUtils.getTeleported(uuid)) {
			
			player.setWhitelisted(true);
			
			if (status == Status.TELEPORT || status == Status.PLAY) GameUtils.addPlayer(uuid);
			
			Map.Entry<String, UUID> entry = new Map.Entry<String, UUID>() {
				
				UUID uuid = player.getUniqueId();

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
			
			List<Map.Entry<String, ?>> uuids = new ArrayList<Map.Entry<String, ?>>();
			uuids.add(entry);
			TeleportUtils.loadSpawnsAndTeleport(uuids);
			
			Bukkit.broadcastMessage(Main.PREFIX + "Teleporting '§6" + player.getName() + "§7'...");
			
		}
		
		if (!GameUtils.getOnGround(uuid)) {
		
			timers.put(uuid, new BukkitRunnable() {
				
				public void run() {
					
					GameUtils.setOnGround(uuid, true);;
					timers.remove(uuid);
					
				}
				
			});
			
			timers.get(uuid).runTaskLater(Main.uhc, 600);
		
		}
		
	}
 
}

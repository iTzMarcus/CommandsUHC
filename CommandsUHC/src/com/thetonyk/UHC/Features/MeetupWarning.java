package com.thetonyk.UHC.Features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class MeetupWarning implements Listener {
	
	public static Map<UUID, BukkitRunnable> runnables = new HashMap<UUID, BukkitRunnable>();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		
		World world = event.getTo().getWorld();
		UUID uuid = event.getPlayer().getUniqueId();
		double size = world.getWorldBorder().getSize();
		double posRadius = size / 2;
		double negRadius = size - (size * 1.5);
		double x = event.getTo().getX();
		double z = event.getTo().getZ();
		
		if (size == WorldUtils.getSize(world.getName()) || size <= 100) return;
		
		if (runnables.containsKey(uuid)) return;
		
		if (x < posRadius - 30 && x > negRadius + 30 && z < posRadius - 30  && z > negRadius + 30) return;
		
		List<Double> compare = new ArrayList<Double>();
		
		compare.add(posRadius - x);
		compare.add(x - negRadius);
		compare.add(posRadius - z);
		compare.add(z - negRadius);
			
		double smallest = Collections.min(compare);
		final int distance = smallest >= 0 ? (int) Math.floor(smallest) : 0;
		
		DisplayUtils.sendActionBar(event.getPlayer(), "§8⫸ §6World Border is at §c" + (int) Math.floor(distance) + " §6blocks of you! §8⫷");
		
		runnables.put(uuid, new BukkitRunnable() {
			
			public void run() {
				
				runnables.remove(uuid);
					
				new BukkitRunnable() {
					
					public void run() {
						
						if (Bukkit.getPlayer(uuid) == null) return;
						
						DisplayUtils.sendActionBar(event.getPlayer(), "§8⫸ §6World Border is at §c" + (int) Math.floor(distance) + " §6blocks of you! §8⫷");
						
					}
					
				}.runTaskLater(Main.uhc, 1);
				
			}
			
		});
		
		runnables.get(event.getPlayer().getUniqueId()).runTaskLater(Main.uhc, 4);
		
	}
	
}

package com.thetonyk.UHC.Features;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
		
		if (event.getTo().getWorld().getWorldBorder().getSize() == WorldUtils.getSize(event.getTo().getWorld().getName()) || event.getTo().getWorld().getWorldBorder().getSize() == 100) return;
		
		if (runnables.containsKey(event.getPlayer().getUniqueId())) return;
		
		if (event.getTo().getX() < (event.getTo().getWorld().getWorldBorder().getSize() / 2) - 30 
			&& event.getTo().getX() > (event.getTo().getWorld().getWorldBorder().getSize() - (event.getTo().getWorld().getWorldBorder().getSize() * 1.5)) + 30 
			&& event.getTo().getZ() < (event.getTo().getWorld().getWorldBorder().getSize() / 2) - 30  
			&& event.getTo().getZ() > (event.getTo().getWorld().getWorldBorder().getSize() - (event.getTo().getWorld().getWorldBorder().getSize() * 1.5)) + 30) return;
		
		double x_pos = (event.getTo().getWorld().getWorldBorder().getSize() / 2) - event.getTo().getX();
		double x_neg = event.getTo().getX() - (event.getTo().getWorld().getWorldBorder().getSize() - (event.getTo().getWorld().getWorldBorder().getSize() * 1.5));
		double z_pos = (event.getTo().getWorld().getWorldBorder().getSize() / 2) - event.getTo().getZ();
		double z_neg = event.getTo().getZ() - (event.getTo().getWorld().getWorldBorder().getSize() - (event.getTo().getWorld().getWorldBorder().getSize() * 1.5));
		
		double temp = 0;
		
		if (x_pos <= x_neg && x_pos <= z_pos && x_pos <= z_neg) temp = x_pos;
		if (x_neg <= x_pos && x_neg <= z_pos && x_neg <= z_neg)	temp = x_neg;
		if (z_pos <= x_neg && z_pos <= x_pos && z_pos <= z_neg) temp = z_pos;
		if (z_neg <= x_neg && z_neg <= z_pos && z_neg <= x_pos) temp = z_neg;
			
		final double distance = temp >= 0 ? temp : 0;
		
		runnables.put(event.getPlayer().getUniqueId(), new BukkitRunnable() {
			
			int i = 0;
			
			public void run() {
				
				if (!event.getPlayer().isOnline()) {
					
					cancel();
					runnables.remove(event.getPlayer().getUniqueId());
					return;
					
				}
				
				DisplayUtils.sendActionBar(event.getPlayer(), "§7World Border is at §c" + (int) Math.floor(distance) + " blocks §7of you!");
				
				if (i >= 1) {
					
					cancel();
					runnables.remove(event.getPlayer().getUniqueId());
					return;
					
				}
				
				i++;
				
			}
			
		});
		
		runnables.get(event.getPlayer().getUniqueId()).runTaskTimer(Main.uhc, 0, 5);
		
	}
	
}

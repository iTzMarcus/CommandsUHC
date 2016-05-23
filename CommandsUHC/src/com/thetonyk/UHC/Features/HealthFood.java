package com.thetonyk.UHC.Features;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

public class HealthFood implements Listener {

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		
		Player player = event.getPlayer();
		float saturation = player.getSaturation();
		
		new BukkitRunnable() {
			
			public void run() {
				
				double newSaturation = player.getSaturation() * 2.5D;
				player.setSaturation((float) (saturation + newSaturation));
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onHungerLevelChange(FoodLevelChangeEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (event.getFoodLevel() < player.getFoodLevel()) {
			
			event.setCancelled(new Random().nextInt(100) < 66);
			
		}
		
	}
	
}

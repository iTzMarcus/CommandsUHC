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
		
		float saturation = event.getPlayer().getSaturation();
		
		new BukkitRunnable() {
			
			public void run() {
				
				event.getPlayer().setSaturation((float) (saturation + (event.getPlayer().getSaturation()) * 2.5D));
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onHungerLevelChange(FoodLevelChangeEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		
		if (event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel()) {
			
			event.setCancelled(new Random().nextInt(100) < 66);
			
		}
		
	}
	
}

package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HealthFood implements Listener {

	@EventHandler
	public void onHungerLevelChange(FoodLevelChangeEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		
		float saturation = ((Player) event.getEntity()).getSaturation();
		
		((Player) event.getEntity()).setSaturation(saturation * 2.5f);
		
	}
	
}

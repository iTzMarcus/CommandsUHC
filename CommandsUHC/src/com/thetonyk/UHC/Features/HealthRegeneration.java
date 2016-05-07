package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class HealthRegeneration implements Listener {

	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
			
		if (event.getRegainReason() != RegainReason.REGEN && event.getRegainReason() != RegainReason.SATIATED) return;
				
		event.setCancelled(true);
		
	}
	
}

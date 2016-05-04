package com.thetonyk.UHC.Features;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.DisplayUtils;

public class HealthShoot implements Listener {

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		if (!(event.getDamager() instanceof Arrow)) return;
		if (!(((Arrow) event.getDamager()).getShooter() instanceof Player)) return;
				
		new BukkitRunnable() {
			
			public void run() {
				
				double health = ((Player) event.getEntity()).getHealth();
				String display = "§4";
				
				for (int i = 0; i < Math.floor((((Player) event.getEntity()).getHealth() / 2)); i++) {
					
					display += "❤";
					health -= 2;
					
				}
				
				if (Math.floor(health) > 0) display += "§c❤";
				
				display += "§f";
				
				for (int i = 0; i < Math.floor((((Player) event.getEntity()).getMaxHealth() / 2) - (((Player) event.getEntity()).getHealth() / 2)); i++) {
					
					display += "❤";
					
				}
				
				NumberFormat format = new DecimalFormat("##.#");
				display += " §7⫸ §6" + format.format(((((Player) event.getEntity()).getHealth()) / 2) * 10) + "%";
				DisplayUtils.sendActionBar((Player) (((Arrow) event.getDamager()).getShooter()), display);
				
			}
			
		}.runTaskLater(Main.uhc, 1);
	
	}
		
}

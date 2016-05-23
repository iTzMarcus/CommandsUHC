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
		
		Player victim = (Player) event.getEntity();
		Arrow arrow = (Arrow) event.getDamager();
		
		if (!(arrow.getShooter() instanceof Player)) return;
		
		Player attacker = (Player) arrow.getShooter();
				
		new BukkitRunnable() {
			
			public void run() {
				
				double health = victim.getHealth();
				double maxHealth = victim.getMaxHealth();
				double hearts = Math.floor(health / 2);
				String display = "§4";
				
				for (int i = 0; i < hearts; i++) {
					
					display += "❤";
					health -= 2;
					
				}
				
				if (hearts > 0) display += "§c❤";
				
				display += "§f";
				
				for (int i = 0; i < Math.floor((maxHealth / 2) - (health / 2)); i++) {
					
					display += "❤";
					
				}
				
				NumberFormat format = new DecimalFormat("##.#");
				display += " §7⫸ §6" + format.format((health / 2) * 10) + "%";
				DisplayUtils.sendActionBar(attacker, display);
				
			}
			
		}.runTaskLater(Main.uhc, 1);
	
	}
		
}

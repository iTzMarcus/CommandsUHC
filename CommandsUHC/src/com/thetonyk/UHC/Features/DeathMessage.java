package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class DeathMessage implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY || GameUtils.getDeath(event.getEntity().getUniqueId())) {
			
			event.setDeathMessage(null);
			return;
			
		}
		
		String victim = PlayerUtils.getRank(event.getEntity().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getEntity().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getEntity().getName()) : "§7") + event.getEntity().getName() + "§7";
		String killer = event.getEntity().getKiller() == null ? null : PlayerUtils.getRank(event.getEntity().getKiller().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getEntity().getKiller().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getEntity().getKiller().getName()) : "§7") + event.getEntity().getKiller().getName() + "§7";		
		String message = Main.PREFIX + "§7" + event.getDeathMessage().substring(0, event.getDeathMessage().contains("using") ? event.getDeathMessage().indexOf("using") : event.getDeathMessage().length()).replaceAll(event.getEntity().getName(), victim).replaceAll(event.getEntity().getKiller() != null ? event.getEntity().getKiller().getName() : "", event.getEntity().getKiller() != null ? killer : "");
		
		event.setDeathMessage(message);
		
		new BukkitRunnable() {
			
			public void run() {
				
				Bukkit.broadcastMessage(Main.PREFIX + "There are §a" + GameUtils.getAlives() + " §7players alive.");
			
			}
			
		}.runTaskLater(Main.uhc, 1);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (PlayerUtils.getNosoundState(player) == 1) continue;
			
			player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
			
		}
		
		GameUtils.setDeath(event.getEntity().getUniqueId(), true);
		
	}
	
}

package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class DeathMessage implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		if (!GameUtils.getWorld().equalsIgnoreCase(event.getEntity().getWorld().getName())) return;
		
		String victim = PlayerUtils.getRank(event.getEntity().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getEntity().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getEntity().getName()) : "§7") + event.getEntity().getName() + "§6";
		String killer = event.getEntity().getKiller() == null ? null : PlayerUtils.getRank(event.getEntity().getKiller().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getEntity().getKiller().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getEntity().getKiller().getName()) : "§7") + event.getEntity().getKiller().getName() + "§6";		
		
		String message = Main.PREFIX + "§6" + event.getDeathMessage().substring(0, event.getDeathMessage().contains("using") ? event.getDeathMessage().indexOf("using") : event.getDeathMessage().length());
		message.replaceAll(event.getEntity().getName(), victim);
		if (killer != null) message.replaceAll(event.getEntity().getKiller().getName(), killer);
		
		event.setDeathMessage(message);
		
		new BukkitRunnable() {
			
			public void run() {
				
				Bukkit.broadcastMessage(Main.PREFIX + "There are §a" + Bukkit.getWorld(GameUtils.getWorld()).getPlayers().size() + " §7players alive.");
			
			}
			
		}.runTaskLater(Main.uhc, 1);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
			
		}
		
	}
	
}

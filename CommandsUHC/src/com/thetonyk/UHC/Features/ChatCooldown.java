package com.thetonyk.UHC.Features;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

public class ChatCooldown implements Listener {

	List<UUID> cooldown = new ArrayList<UUID>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat (AsyncPlayerChatEvent event) {
		
		if (cooldown.contains(event.getPlayer().getUniqueId())) {
			
			event.setCancelled(true);
			return;
			
		}
		
		cooldown.add(event.getPlayer().getUniqueId());
		
		new BukkitRunnable() {
			
			public void run() {
				
				cooldown.remove(event.getPlayer().getUniqueId());
				
			}
			
		}.runTaskLater(Main.uhc, 40);
		
	}
	
}

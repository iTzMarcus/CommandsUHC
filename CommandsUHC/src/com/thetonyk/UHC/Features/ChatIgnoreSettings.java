package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.thetonyk.UHC.Utils.PlayerUtils;

public class ChatIgnoreSettings implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onChat (AsyncPlayerChatEvent event) {
		
		for (Player receiver : event.getRecipients()) {
			
			if (!PlayerUtils.getIgnoredPlayers(receiver.getUniqueId()).contains(event.getPlayer().getUniqueId())) return;
			
			event.getRecipients().remove(receiver);
			
		}
		
	}
	
}

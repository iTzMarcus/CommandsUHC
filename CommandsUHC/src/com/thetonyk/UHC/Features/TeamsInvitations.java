package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.thetonyk.UHC.Inventories.InviteInventory;

public class TeamsInvitations implements Listener {

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equals("lobby")) return;
			
		if (!(event.getRightClicked() instanceof Player)) return;
				
		event.getPlayer().openInventory(InviteInventory.getInvite((Player) event.getRightClicked()));
		
	}
	
}

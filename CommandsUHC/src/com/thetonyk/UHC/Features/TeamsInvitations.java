package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.thetonyk.UHC.Inventories.InviteInventory;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class TeamsInvitations implements Listener {

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equals("lobby")) return;
			
		if (!(event.getRightClicked() instanceof Player)) return;
		
		if (TeamsUtils.getTeam(event.getPlayer().getUniqueId()) != null && TeamsUtils.getTeam(event.getRightClicked().getUniqueId()) != null && TeamsUtils.getTeam(event.getPlayer().getUniqueId()).equalsIgnoreCase(TeamsUtils.getTeam(event.getRightClicked().getUniqueId()))) return;
				
		event.getPlayer().openInventory(InviteInventory.getInvite((Player) event.getRightClicked()));
		
	}
	
}

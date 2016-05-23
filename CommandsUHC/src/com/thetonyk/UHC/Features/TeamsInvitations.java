package com.thetonyk.UHC.Features;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

import com.thetonyk.UHC.Inventories.InviteInventory;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class TeamsInvitations implements Listener {

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		
		if (!(event.getRightClicked() instanceof Player)) return;
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		World world = player.getWorld();
		Player clicked = (Player) event.getRightClicked();
		
		if (!world.getName().equals("lobby")) return;
		
		if (TeamsUtils.getTeam(uuid) != null && TeamsUtils.getTeam(clicked.getUniqueId()) != null && TeamsUtils.getTeam(uuid).equalsIgnoreCase(TeamsUtils.getTeam(clicked.getUniqueId()))) return;
				
		Inventory invitation = InviteInventory.getInvite(clicked);
		
		player.openInventory(invitation);
		
	}
	
}

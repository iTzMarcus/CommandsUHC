package com.thetonyk.UHC.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.thetonyk.UHC.Inventories.TeamsInventory;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (event.getInventory().getTitle().equals("§8⫸ §4Rules")) {
			
			event.setCancelled(true);
			return;
			
		}		
		
		if (event.getInventory().getTitle().equals("§8⫸ §4Teams")) {
			
			if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§7Next §8⫸")) {
				
				event.setCancelled(true);
				Bukkit.getPlayer(event.getWhoClicked().getName()).openInventory(TeamsInventory.getTeams(2));
				return;
				
			} else if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§8⫷ §7Previous")) {
				
				event.setCancelled(true);
				Bukkit.getPlayer(event.getWhoClicked().getName()).openInventory(TeamsInventory.getTeams(1));
				return;
				
			} else if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§8⫸ §cClose")) {
				
				event.setCancelled(true);
				event.getWhoClicked().closeInventory();;
				return;
				
			} else {
				
				event.setCancelled(true);
				return;
				
			}
			
		}
		
		if (event.getInventory().getTitle().startsWith("§8⫸ §4Invite ")) {
			
			if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8⫸ §7Invite §6")) {
				
				event.setCancelled(true);
				
				if (event.getWhoClicked() instanceof Player) {
					
					String invite = event.getInventory().getTitle().substring(13);
					((Player) event.getWhoClicked()).performCommand("team invite " + invite);
					
				}
				
				event.getWhoClicked().closeInventory();
				return;
				
			} else if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8⫸ §cCancel")) {
				
				event.setCancelled(true);
				event.getWhoClicked().closeInventory();
				return;
				
			} else {
				
				event.setCancelled(true);
				return;
				
			}
			
		}
		
		if (event.getWhoClicked().getWorld().getName().equals("lobby") && event.getAction() == InventoryAction.HOTBAR_SWAP) {

			event.setCancelled(true);
			return;
				
		}
				
		if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§b§lThe Rules §7(Right-Click)")) {
					
			event.setCancelled(true);
			return;
		
		}
			
	}

}

package com.thetonyk.UHC.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.UHC.Utils.ItemsUtils;

public class InviteInventory implements Listener {
	
	public static Inventory getInvite(Player invite) {
		
		Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "§8⫸ §4Invite " + invite.getName());
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		ItemStack accept = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Invite §6" + invite.getName(), 1, 5);
		ItemStack cancel = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §cCancel", 1, 14);
		
		inventory.setItem(0, separator);
		inventory.setItem(1, accept);
		inventory.setItem(2, separator);
		inventory.setItem(3, cancel);
		inventory.setItem(4, separator);
		
		return inventory;
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!event.getInventory().getTitle().startsWith("§8⫸ §4Invite ")) return;
		
		event.setCancelled(true);
		
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Invite §6")) {
					
			String invite = event.getInventory().getTitle().substring(13);
			player.performCommand("team invite " + invite);
			player.closeInventory();
			return;
				
		}
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §cCancel")) {
				
			player.closeInventory();
			return;
				
		}
		
	}

}

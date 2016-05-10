package com.thetonyk.UHC.Inventories;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.UHC.Utils.ItemsUtils;

public class RulesInventory implements Listener {
	
	public static Inventory getRules() {
		
		Inventory inventory = Bukkit.createInventory(null, 9, "§8⫸ §4Rules");
		
		ArrayList<String> lore = new ArrayList<String>();
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		
		lore.add(" ");
		lore.add("§8⫸ §7Not available during stress test.");
		lore.add("§8⫸ §7Ask a staff member.");
		lore.add(" ");
		ItemStack health = ItemsUtils.createItem(Material.GOLDEN_APPLE, "§8⫸ §6Health & Food §8⫷", 1, 0, lore);
		lore.clear();
		
		lore.add(" ");
		lore.add("§8⫸ §7Not available during stress test.");
		lore.add("§8⫸ §7Ask a staff member.");
		lore.add(" ");
		ItemStack pvp = ItemsUtils.createItem(Material.DIAMOND_SWORD, "§8⫸ §6PVP/iPVP §8⫷", 1, 0, lore);
		pvp = ItemsUtils.hideFlags(pvp);
		lore.clear();
		
		lore.add(" ");
		lore.add("§8⫸ §7Not available during stress test.");
		lore.add("§8⫸ §7Ask a staff member.");
		lore.add(" ");
		ItemStack mining = ItemsUtils.createItem(Material.DIAMOND_PICKAXE, "§8⫸ §6Mining §8⫷", 1, 0, lore);
		mining = ItemsUtils.hideFlags(mining);
		lore.clear();
		
		lore.add(" ");
		lore.add("§8⫸ §7Not available during stress test.");
		lore.add("§8⫸ §7Ask a staff member.");
		lore.add(" ");
		ItemStack other = ItemsUtils.createItem(Material.SIGN, "§8⫸ §6Others §8⫷", 1, 0, lore);
		lore.clear();
		
		
		inventory.setItem(0, separator);
		inventory.setItem(1, health);
		inventory.setItem(2, separator);
		inventory.setItem(3, pvp);
		inventory.setItem(4, separator);
		inventory.setItem(5, mining);
		inventory.setItem(6, separator);
		inventory.setItem(7, other);
		inventory.setItem(8, separator);
		
		return inventory;
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!event.getInventory().getTitle().equals("§8⫸ §4Rules")) return;
		
		event.setCancelled(true);
		
	}

}

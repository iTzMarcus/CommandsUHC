package com.thetonyk.UHC.Features;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.UHC.Game;
import com.thetonyk.UHC.Game.Status;
import com.thetonyk.UHC.Inventories.RulesInventory;
import com.thetonyk.UHC.Utils.ItemsUtils;

public class LobbyItems implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
	
		if (!event.getPlayer().getWorld().getName().equals("lobby")) return;
			
		if (event.getItem() == null || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName()) return;
				
		if (!event.getItem().getItemMeta().getDisplayName().equals("§b§lThe Rules §7(Right-Click)")) return;
		
		event.setCancelled(true);	
		event.getPlayer().openInventory(RulesInventory.getRules());
		
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equals("lobby")) return;
		
		if (event.getItemDrop() == null || !event.getItemDrop().getItemStack().hasItemMeta() || !event.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) return;
				
		if (!event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§b§lThe Rules §7(Right-Click)")) return;
			
		event.setCancelled(true);
		event.getPlayer().openInventory(RulesInventory.getRules());
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
				
		if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) return;
		
		if (!event.getCurrentItem().getItemMeta().getDisplayName().equals("§b§lThe Rules §7(Right-Click)")) return;
				
		event.setCancelled(true);
		event.getWhoClicked().openInventory(RulesInventory.getRules());
			
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT || Game.getStatus() == Status.PLAY || Game.getStatus() == Status.END) return;
			
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Click to see the rules.");
		ItemStack rules = ItemsUtils.createItem(Material.PAPER, "§b§lThe Rules §7(Right-Click)", 1, 0, lore);
		rules = ItemsUtils.addGlow(rules);
		
		event.getPlayer().getInventory().setItem(4, rules);
		
	}
	
}

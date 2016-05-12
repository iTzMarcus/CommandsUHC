package com.thetonyk.UHC.Features;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Inventories.RulesInventory;
import com.thetonyk.UHC.Inventories.TeamsInventory;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.ItemsUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class LobbyItems implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
			
		if (event.getItem() == null || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName()) return;
				
		if (event.getItem().getItemMeta().getDisplayName().equals("§a§lThe Rules §7(Right-Click)")) {
			
			event.setCancelled(true);	
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
			
			if (GameUtils.getStatus() == Status.NONE) {
				
				event.getPlayer().sendMessage(Main.PREFIX + "The game is not ready.");
				return;
				
			}
			
			event.getPlayer().openInventory(RulesInventory.getRules());
			return;
			
		}
		
		if (event.getItem().getItemMeta().getDisplayName().equals("§6§lTeams List §7(Right-Click)")) {
			
			event.setCancelled(true);	
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
			if (TeamsUtils.getTeamsLeft() == 75) {
				
				event.getPlayer().sendMessage(Main.PREFIX + "There are no teams.");
				return;
				
			}
			
			event.getPlayer().openInventory(TeamsInventory.getTeams(1));
			return;
			
		}
		
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		if (event.getItemDrop() == null || !event.getItemDrop().getItemStack().hasItemMeta() || !event.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) return;
				
		if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§a§lThe Rules §7(Right-Click)")) {
			
			event.setCancelled(true);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
			
			if (GameUtils.getStatus() == Status.NONE) {
				
				event.getPlayer().sendMessage(Main.PREFIX + "The game is not ready.");
				return;
				
			}
			
			event.getPlayer().openInventory(RulesInventory.getRules());
			return;
			
		}
		
		if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§6§lTeams List §7(Right-Click)")) {
			
			event.setCancelled(true);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
			if (TeamsUtils.getTeamsLeft() == 75) {
				
				event.getPlayer().sendMessage(Main.PREFIX + "There are no teams.");
				return;
				
			}
			
			event.getPlayer().openInventory(TeamsInventory.getTeams(1));
			return;
			
		}
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
				
		if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) return;
		
		if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§a§lThe Rules §7(Right-Click)")) {
				
			event.setCancelled(true);
			((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 1, 1);
			
			if (GameUtils.getStatus() == Status.NONE) {
				
				event.getWhoClicked().sendMessage(Main.PREFIX + "The game is not ready.");
				return;
				
			}
			
			event.getWhoClicked().openInventory(RulesInventory.getRules());
			return;
			
		}
		
		if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§6§lTeams List §7(Right-Click)")) {
			
			event.setCancelled(true);
			((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 1, 1);
			if (TeamsUtils.getTeamsLeft() == 75) {
				
				event.getWhoClicked().sendMessage(Main.PREFIX + "There are no teams.");
				return;
				
			}
			
			event.getWhoClicked().openInventory(TeamsInventory.getTeams(1));
			return;
			
		}
			
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if ((GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY || GameUtils.getStatus() == Status.END) && !GameUtils.getDeath(event.getPlayer().getUniqueId())) return;
			
		giveItems(event.getPlayer());
		
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		giveItems(event.getPlayer());
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onRespawn(PlayerRespawnEvent event) {
		
		if (!event.getRespawnLocation().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		giveItems(event.getPlayer());
		
	}
	
	private void giveItems (Player player) {
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Click to see the rules.");
		ItemStack rules = ItemsUtils.createItem(Material.PAPER, "§a§lThe Rules §7(Right-Click)", 1, 0, lore);
		rules = ItemsUtils.addGlow(rules);
		
		player.getInventory().setItem(4, rules);
		
		lore = new ArrayList<String>();
		lore.add("§7Click to see teams.");
		ItemStack teams = ItemsUtils.createItem(Material.NAME_TAG, "§6§lTeams List §7(Right-Click)", 1, 0, lore);
		teams = ItemsUtils.addGlow(teams);
		
		player.getInventory().setItem(8, teams);
		
	}
	
}

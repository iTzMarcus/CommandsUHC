package com.thetonyk.UHC.Inventories;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.ItemsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;

public class PlayerInventory implements Listener {
	
	private static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();
	
	public PlayerInventory() {
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Map.Entry<UUID, Inventory> inventory : inventories.entrySet()) {
					
					if (inventory.getValue().getViewers().size() < 1) {
						
						inventories.remove(inventory.getKey());
						continue;
						
					}
					
					update(inventory.getKey());
					
				}
				
			}
			
		}.runTaskTimer(Main.uhc, 1, 1);
		
	}
	
	public static Inventory getInventory(UUID uuid) {
		
		if (!inventories.containsKey(uuid)) {
			
			Inventory inventory = Bukkit.createInventory(null, 54, "§7Inventory ⫸ §4" + PlayerUtils.getName(PlayerUtils.getId(uuid)));
			inventories.put(uuid, inventory);
			
		}
		
		return inventories.get(uuid);
		
	}
	
	private static void update(UUID uuid) {
		
		if (!inventories.containsKey(uuid)) return;
		
		Inventory inventory = inventories.get(uuid);
		
		if (inventory == null || Bukkit.getPlayer(uuid) == null) return;
		
		Player player = Bukkit.getPlayer(uuid);
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		ItemStack[] content = player.getInventory().getContents();
		
		//Separator
		//Health (Air under water)
		//Food
		//Effects
		//Separator
		//Team/Teammates
		//Separator
		//Ores/Mined ores/XP
		//Separator
		
		
		for (int i = 9; i < content.length; i++) {
			
			inventory.setItem(i, content[i]);
			
		}
		
		inventory.setItem(36,separator);
		inventory.setItem(37, player.getItemInHand());
		inventory.setItem(37, ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7⫷ §6Item in hand", 1, 7));
		inventory.setItem(38, player.getItemOnCursor());
		inventory.setItem(39, ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7⫷ §6Item on cursor", 1, 7));
		inventory.setItem(40, player.getInventory().getHelmet());
		inventory.setItem(41, player.getInventory().getChestplate());
		inventory.setItem(42, player.getInventory().getLeggings());
		inventory.setItem(43, player.getInventory().getBoots());
		inventory.setItem(44, separator);
		
		for (int i = 0; i <= 8; i++) {
			
			inventory.setItem(i + 45, content[i]);
			
		}
		
		for (HumanEntity viewer : inventory.getViewers()) {
			
			if (!(viewer instanceof Player)) continue;
			
			((Player) viewer).updateInventory();
			
		}
		
	}

}

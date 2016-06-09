package com.thetonyk.UHC.Inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.UHC.Features.Options.EndOption;
import com.thetonyk.UHC.Features.Options.GoldenHeadOption;
import com.thetonyk.UHC.Features.Options.NetherOption;
import com.thetonyk.UHC.GUI.NumberGUI;
import com.thetonyk.UHC.GUI.NumberGUI.NumberCallback;
import com.thetonyk.UHC.Utils.ItemsUtils;

public class ConfigInventory implements Listener {

	private static Inventory inventory;
	
	public ConfigInventory() {
		
		inventory = Bukkit.createInventory(null, 18, "§8⫸ §4Config");
		update();
		
	}
	
	public static Inventory getInventory() {
		
		return inventory;
		
	}
	
	private void update() {
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		
		for (int i = 0; i < inventory.getSize(); i++) {
			
			inventory.setItem(i, separator);
			
		}
		
		List<String> lore = new ArrayList<String>();
		
		ItemStack nether = ItemsUtils.createItem(Material.NETHERRACK, "§8⫸ §7Nether: " + (NetherOption.enabled() ? "§aEnabled" : "§cDisabled"), 1, 0);
		ItemStack end = ItemsUtils.createItem(Material.ENDER_STONE, "§8⫸ §7The End: " + (EndOption.enabled() ? "§aEnabled" : "§cDisabled"), 1, 0);
		
		lore.add(" ");
		lore.add("§8⫸ §7Regeneration amount: §a" + GoldenHeadOption.getRegen());
		lore.add("§8⫸ §aRight-click §7to change regen amount.");
		lore.add(" ");

		ItemStack goldenHead = ItemsUtils.createItem(Material.SKULL_ITEM, "§8⫸ §7Golden Head: " + (GoldenHeadOption.enabled() ? "§aEnabled" : "§cDisabled"), 1, 3, lore);
		lore.clear();
		
		inventory.setItem(0, nether);
		inventory.setItem(1, end);
		inventory.setItem(2, goldenHead);
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (!event.getInventory().equals(inventory)) return;
		
		event.setCancelled(true);
		
		ItemStack item = event.getCurrentItem();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
		
		if (event.getClick() == null) return;
		
		ClickType click = event.getClick();
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Nether: ")) {
			
			if (click != ClickType.LEFT) return;
			
			if (NetherOption.enabled()) NetherOption.disable();
			else NetherOption.enable();
			
			update();
			
		}
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7The End: ")) {
			
			if (click != ClickType.LEFT) return;
			
			if (EndOption.enabled()) EndOption.disable();
			else EndOption.enable();
			
			update();
			
		}

		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Golden Head: ")) {
			
			if (click == ClickType.RIGHT) {
				
				NumberGUI gui = new NumberGUI("Set regen amount", GoldenHeadOption.getRegen() / 2, 2, 1, 4, 100, 0, new NumberCallback<Integer>() {

					@Override
					public void onConfirm(int newSize) {
						
						GoldenHeadOption.setRegen(newSize * 2);
						update();
						player.openInventory(inventory);
						
					}

					@Override
					public void onCancel() {
						
						player.openInventory(inventory);
						
					}

					@Override
					public void onDisconnect() {}
					
				});
				
				player.openInventory(gui.getInventory());
				return;
				
			}
			
			if (GoldenHeadOption.enabled()) GoldenHeadOption.disable();
			else GoldenHeadOption.enable();
			
			update();
			
		}
		
	}
	
}

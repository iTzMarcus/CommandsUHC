package com.thetonyk.UHC.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.ItemsUtils;

public class NumberGUI implements Listener {
	
	private Inventory inventory;
	private int currently;
	private int bigAdd;
	private int add;
	private int reset;
	private int max;
	private int min;
	private NumberCallback<Integer> callback;
	
	public NumberGUI(String title, int currently, int bigAdd, int add, int reset, int max, int min, NumberCallback<Integer> callback) {
		
		this.inventory = Bukkit.createInventory(null, 18, "§8⫸ §4" + title);
		this.currently = currently;
		this.bigAdd = bigAdd;
		this.add = add;
		this.reset = reset;
		this.max = max;
		this.min = min;
		this.callback = callback;
		update();
		
		Bukkit.getPluginManager().registerEvents(this, Main.uhc);
		
	}
	
	public Inventory getInventory() {
		
		return this.inventory;
		
	}
	
	private void update() {
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		
		for (int i = 0; i < this.inventory.getSize(); i++) {
			
			this.inventory.setItem(i, separator);
			
		}
		
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add("§8⫸ §7Currently: §a" + this.currently);
		lore.add(" ");
		
		ItemStack bigAdd = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Add §a+" + this.bigAdd, 10, 5, lore);
		ItemStack add = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Add §a+" + this.add, 1, 5, lore);
		ItemStack reset = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Reset", 1, 4, lore);
		ItemStack remove = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Remove §c-" + this.add, 1, 14, lore);
		ItemStack bigRemove = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Remove §c-" + this.bigAdd, 10, 14, lore);
		
		ItemStack cancel = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §cCancel", 1, 14);
		ItemStack confirm = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §aConfirm", 1, 5);
		
		this.inventory.setItem(2, bigAdd);
		this.inventory.setItem(3, add);
		this.inventory.setItem(4, reset);
		this.inventory.setItem(5, remove);
		this.inventory.setItem(6, bigRemove);
		this.inventory.setItem(9, cancel);
		this.inventory.setItem(17, confirm);
		
	}
	
	@Override
	protected void finalize() {
		
		cancel();
		
	}
	
	private void cancel() {
		
		HandlerList.unregisterAll(this);
		
		for (HumanEntity viewer : new ArrayList<HumanEntity>(this.inventory.getViewers())) {
			
			viewer.closeInventory();
			
		}
		
	}
	
	private void add(int number) {
		
		if (this.currently + number > this.max) return;
		
		this.currently += number;
		
	}
	
	private void remove(int number) {
		
		if (this.currently - number < this.min) return;
		
		this.currently -= number;
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if (!inventory.getViewers().contains(player)) return;
		
		new BukkitRunnable() {
			
			public void run() {
				
				if (inventory.getViewers().size() < 1) {
					
					callback.onDisconnect();
					cancel();
					
				}
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		
		if (!(event.getPlayer() instanceof Player)) return;
		
		Player player = (Player) event.getPlayer();
		
		if (!event.getInventory().equals(this.inventory)) return;
		
		new BukkitRunnable() {
		
			public void run() {
			
				player.openInventory(getInventory());
		
			}
		
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		if (!event.getInventory().equals(this.inventory)) return;
		
		event.setCancelled(true);
		
		ItemStack item = event.getCurrentItem();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
		
		if (item.getItemMeta().getDisplayName().endsWith("+" + this.bigAdd)) {
			
			add(this.bigAdd);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().endsWith("+" + this.add)) {
			
			add(this.add);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().endsWith("-" + String.valueOf(this.add))) {
			
			remove(this.add);
			update();
			return;
			
		}

		if (item.getItemMeta().getDisplayName().endsWith(String.valueOf("-" + this.bigAdd))) {
			
			remove(this.bigAdd);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().endsWith("Reset")) {
			
			this.currently = this.reset;
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §cCancel")) {
			
			cancel();
			this.callback.onCancel();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aConfirm")) {
			
			cancel();
			this.callback.onConfirm(this.currently);
			return;
			
		}
		
	}
	
	public static interface NumberCallback<T> {
		
		void onConfirm(int newSize);
		void onCancel();
		void onDisconnect();
		
	}

}

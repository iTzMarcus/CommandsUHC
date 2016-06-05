package com.thetonyk.UHC.Inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
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
import com.thetonyk.UHC.Utils.SignGUIUtils;
import com.thetonyk.UHC.Utils.SignGUIUtils.Callback;
import com.thetonyk.UHC.Utils.WorldUtils;

public class WorldInventory implements Listener {

	private Inventory inventory;
	private String name;
	private int size;
	private Boolean nether;
	private Boolean end;
	private long seed;
	private String lastName;
	private List<UUID> inSign = new ArrayList<UUID>();
	
	public WorldInventory() {
		
		this.inventory = Bukkit.createInventory(null, 27, "§8⫸ §4World Creation");
		this.name = null;
		this.lastName = null;
		this.size = 2000;
		this.nether = false;
		this.end = false;
		this.seed = 0;
		update();
		
		Bukkit.getPluginManager().registerEvents(this, Main.uhc);
		
	}
	
	public Inventory getInventory() {
		
		return inventory;
		
	}
	
	private void update() {
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		
		for (int i = 0; i < this.inventory.getSize(); i++) {
			
			this.inventory.setItem(i, separator);
			
		}
		
		List<String> lore = new ArrayList<String>();
		Boolean valid = false;
		
		if (this.name != null && this.size >= 100) valid = true;
		
		if (this.name == null && this.lastName != null) {
			
			lore.add(" ");
			lore.add("§8⫸ §7Last entered name: §c" + this.lastName);
			if (this.lastName.length() < 3) lore.add("§8⫸ §7This name was too short.");
			if (WorldUtils.exist(this.lastName) || this.lastName.equalsIgnoreCase("lobby")) lore.add("§8⫸ §7This name is already used.");
			if (this.lastName.endsWith("_nether") || this.lastName.endsWith("_end")) lore.add("§8⫸ §7You can't create Nether or End by yourself.");
			lore.add(" ");
			
		}
		
		ItemStack name = ItemsUtils.createItem(Material.NAME_TAG, "§8⫸ §7Name: §6" + (this.name == null ? "§cNone" : this.name), 1, 0, lore);
		lore.clear();
		ItemStack size = ItemsUtils.createItem(Material.EMPTY_MAP, "§8⫸ §7Size: §a" + this.size, 1, 0);
		ItemStack nether = ItemsUtils.createItem(Material.NETHERRACK, "§8⫸ §7Nether: " + (this.nether ? "§aEnabled" : "§cDisabled"), 1, 0);
		ItemStack end = ItemsUtils.createItem(Material.ENDER_STONE, "§8⫸ §7The End: " + (this.end ? "§aEnabled" : "§cDisabled"), 1, 0);
		ItemStack seed = ItemsUtils.createItem(Material.GRASS, "§8⫸ §7Seed: §a" + (this.seed == 0 ? "§cRandom" : this.seed), 1, 0);
		ItemStack cancel = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §cCancel", 1, 14);

		if (!valid) {
			
			lore.add(" ");
			if (this.name == null) lore.add("§8⫸ §7The name is incorrect.");
			if (this.size < 100)  lore.add("§8⫸ §7The size is too small.");
			lore.add(" ");
			
		}
		
		ItemStack create = ItemsUtils.createItem(Material.STAINED_CLAY, (valid ? "§8⫸ §aCreate world" : "§8⫸ §7§oCreate world"), 1, 5, lore);
		lore.clear();
		
		this.inventory.setItem(11, name);
		this.inventory.setItem(12, size);
		this.inventory.setItem(13, nether);
		this.inventory.setItem(14, end);
		this.inventory.setItem(15, seed);
		this.inventory.setItem(18, cancel);
		this.inventory.setItem(26, create);
		
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
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		new BukkitRunnable() {
			
			public void run() {
				
				if (inSign.contains(player.getUniqueId())) inSign.remove(player.getUniqueId());
				
				if (inventory.getViewers().size() < 1) cancel();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		
		if (!(event.getPlayer() instanceof Player)) return;
		
		Player player = (Player) event.getPlayer();
		
		if (!event.getInventory().equals(this.inventory)) return;
		
		if (inSign.contains(player.getUniqueId())) return;
		
		new BukkitRunnable() {
		
			public void run() {
			
				player.openInventory(getInventory());
		
			}
		
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (!event.getInventory().equals(this.inventory)) return;
		
		event.setCancelled(true);
		
		ItemStack item = event.getCurrentItem();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Name: §6")) {
			
			this.inSign.add(player.getUniqueId());
			
			String[] text = new String[this.name == null && this.lastName == null ? 0 : 1];
			if (text.length > 0) text[0] = this.name == null ? this.lastName : this.name;
			
			new SignGUIUtils(player, text, new Callback<String[]>() {

				@Override
				public void onConfirm(String[] lines) {
					
					new BukkitRunnable() {
						
						public void run() {
							
							player.openInventory(getInventory());
							
							new BukkitRunnable() {
								
								public void run() {
									
									inSign.remove(player.getUniqueId());
								
								}
								
							}.runTaskLater(Main.uhc, 1);
						
						}
						
					}.runTaskLater(Main.uhc, 1);
					
					if (lines[0].length() + lines[1].length() + lines[2].length() + lines[3].length() < 1) return;
					
					String text = null;
					
					for (String line : lines) {
						
						if (line.length() < 1) continue;
						
						text = line;
						break;
						
					}
					
					if (text.length() < 3 || WorldUtils.exist(text) || text.equalsIgnoreCase("lobby") || text.endsWith("_nether") || text.endsWith("_end")) {
						
						name = null;
						lastName = text;
						update();
						return;
						
					}
					
					name = text;
					lastName = null;
					update();
					
				}

				@Override
				public void onDisconnect() {
					
					cancel();
					
				}
				
			});
			
			return;
			
		}
		
		
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Nether: ")) {
			
			this.nether = !this.nether;
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7The End: ")) {
			
			this.end = !this.end;
			update();
			return;
			
		}
		
		
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §cCancel")) {
			
			cancel();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aCreate world")) {
			
			cancel();
			
			if (this.seed == 0) this.seed = new Random().nextLong();
			
			Bukkit.broadcastMessage(Main.PREFIX + "Creation of world '§6" + this.name + "§7'...");
			Bukkit.broadcastMessage("§8⫸ §7Size: §a" + this.size);
			Bukkit.broadcastMessage("§8⫸ §7Seed: §a" + this.seed);
			Bukkit.broadcastMessage("§8⫸ §7Nether: " + (nether ? "§aEnabled" : "§cDisabled"));
			Bukkit.broadcastMessage("§8⫸ §7End: " + (end ? "§aEnabled" : "§cDisabled"));
			
			WorldUtils.createWorld(this.name, Environment.NORMAL, this.seed, WorldType.NORMAL, this.size);
			
			if (nether) WorldUtils.createWorld(this.name + "_nether", Environment.NETHER, this.seed, WorldType.NORMAL, this.size);	
			
			if (end) WorldUtils.createWorld(this.name + "_end", Environment.THE_END, this.seed, WorldType.NORMAL, this.size);	
			
			player.sendMessage(Main.PREFIX + "The world '§6" + this.name + "§7' has been created.");
			return;
			
		}
		
	}
	
}

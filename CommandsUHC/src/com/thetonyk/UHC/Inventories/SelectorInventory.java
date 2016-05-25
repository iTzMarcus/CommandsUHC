package com.thetonyk.UHC.Inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.StartEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.ItemsUtils;

public class SelectorInventory implements Listener {
	
	private static Map<UUID, Inventory> viewers = new HashMap<UUID, Inventory>();
	private static Map<UUID, Integer> page = new HashMap<UUID, Integer>();
	private static Map<UUID, Filter> filters = new HashMap<UUID, Filter>();
	
	public static Inventory getSelector(UUID uuid) {
		
		if (!viewers.containsKey(uuid)) viewers.put(uuid, Bukkit.createInventory(null, 54, "§8⫸ §4Player Selector"));
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
		return viewers.get(uuid);
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!event.getInventory().getName().equalsIgnoreCase("§8⫸ §4Player Selector")) return;
		
		event.setCancelled(true);
		
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		InventoryAction action = event.getAction();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
		
		int pageNumber = page.containsKey(player.getUniqueId()) ? page.get(player.getUniqueId()) : 0;
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§7Next §8⫸")) {
			
			page.put(player.getUniqueId(), pageNumber + 1);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫷ §7Previous")) {
			
			page.put(player.getUniqueId(), pageNumber - 1);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aAll")) {
			
			filters.put(player.getUniqueId(), Filter.ALL);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aCaves")) {
			
			filters.put(player.getUniqueId(), Filter.CAVES);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aNear middle")) {
			
			filters.put(player.getUniqueId(), Filter.MIDDLE);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aNether")) {
			
			filters.put(player.getUniqueId(), Filter.NETHER);
			update();
			return;
			
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §aThe End")) {
			
			filters.put(player.getUniqueId(), Filter.END);
			update();
			return;
			
		}
		
		if (action != InventoryAction.PICKUP_ALL && action != InventoryAction.PICKUP_HALF) return;
		
		player.closeInventory();
		
		Player clicked = Bukkit.getPlayer(item.getItemMeta().getDisplayName().substring(6, item.getItemMeta().getDisplayName().length() - 4));
		
		if (clicked == null) return;
		
		if (action == InventoryAction.PICKUP_ALL) {
			
			player.teleport(clicked);
			return;
			
		}
		
		if (action == InventoryAction.PICKUP_HALF) {
			
			player.openInventory(PlayerInventory.getInventory(clicked.getUniqueId()));
			return;
			
		}
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onPlayerChangeGameMode(PlayerGameModeChangeEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler
	public void onStart(StartEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				update();
				
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	public static void update() {
		
		List<UUID> alives = GameUtils.getAlives();
		
		for (UUID viewer : viewers.keySet()) {
			
			if (Bukkit.getPlayer(viewer) == null && !Bukkit.getPlayer(viewer).getOpenInventory().getTopInventory().equals(viewers.get(viewer))) {
				
				viewers.remove(viewer);
				
				if (page.containsKey(viewer)) page.remove(viewer);
				if (filters.containsKey(viewer)) filters.remove(viewer);
				
				continue;
					
			}
			
			if (!page.containsKey(viewer)) page.put(viewer, 0);
			if (!filters.containsKey(viewer)) filters.put(viewer, Filter.ALL);
			
			Map<UUID, String> players = new HashMap<UUID, String>();
			World world = Bukkit.getWorld(GameUtils.getWorld());
			
			for (UUID alive : alives) {
				
				Player player = Bukkit.getPlayer(alive);
				
				if (player == null) continue;
				
				if (filters.get(viewer) == Filter.CAVES && player.getWorld().getEnvironment() == Environment.NORMAL && player.getLocation().getY() > 35) continue;
				
				if (filters.get(viewer) == Filter.MIDDLE && (world == null || !player.getWorld().equals(world) || player.getLocation().distance(world.getWorldBorder().getCenter()) > 150)) continue;
				
				if (filters.get(viewer) == Filter.NETHER && player.getWorld().getEnvironment() != Environment.NETHER) continue;
				
				if (filters.get(viewer) == Filter.END && player.getWorld().getEnvironment() != Environment.THE_END) continue;
				
				players.put(alive, player.getName());
				
			}
			
			if (page.get(viewer) > Math.ceil(players.size() / 36) || page.get(viewer) < 0) page.put(viewer, 0);
			
			new BukkitRunnable() {
			
				public void run() {
				
					int playersNumber = 36 * page.get(viewer);
					
					if (players.size() > 0) {
					
						for (int i = 0; i <= 35; i++) {
							
							if (players.size() <= i + playersNumber) break;
							
							String name = players.get(players.keySet().toArray()[i + playersNumber]);
							List<String> lore = new ArrayList<String>();
							
							ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
							SkullMeta headMeta = (SkullMeta) head.getItemMeta();
							headMeta.setOwner(name);
							headMeta.setDisplayName("§8⫸ §a" + name + " §8⫷");
							lore.add(" ");
							lore.add("§8⫸ §6Left-click §7to teleport");
							lore.add("§8⫸ §6Right-click §7to see inventory");
							lore.add(" ");
							headMeta.setLore(lore);
							head.setItemMeta(headMeta);
							viewers.get(viewer).setItem(i, head);
							
						}
					
					}
					
					ItemStack all = ItemsUtils.createItem(Material.COMPASS, "§8⫸ §aAll", 1, 0, Arrays.asList("§7List all alives players."));
					ItemStack caves = ItemsUtils.createItem(Material.IRON_PICKAXE, "§8⫸ §aCaves", 1, 0, Arrays.asList("§7List all players below y=35."));
					ItemStack middle = ItemsUtils.createItem(Material.DIAMOND_SWORD, "§8⫸ §aNear middle", 1, 0, Arrays.asList("§7List all players near the middle of the map."));
					ItemStack nether = ItemsUtils.createItem(Material.NETHERRACK, "§8⫸ §aNether", 1, 0, Arrays.asList("§7List all in the Nether."));
					ItemStack end = ItemsUtils.createItem(Material.ENDER_STONE, "§8⫸ §aThe End", 1, 0, Arrays.asList("§7List all in The End."));
					
					switch (filters.get(viewer)) {
					
						case ALL:
							all = ItemsUtils.addGlow(all);
							break;
						case CAVES:
							caves = ItemsUtils.addGlow(caves);
							break;
						case MIDDLE:
							middle = ItemsUtils.addGlow(middle);
							break;
						case NETHER:
							nether = ItemsUtils.addGlow(nether);
							break;
						case END:
							end = ItemsUtils.addGlow(end);
							break;
						default:
							break;
					
					}
					
					viewers.get(viewer).setItem(47, all);
					viewers.get(viewer).setItem(48, caves);
					viewers.get(viewer).setItem(49, middle);
					viewers.get(viewer).setItem(50, nether);
					viewers.get(viewer).setItem(51, end);
					
					if (page.get(viewer) > 0) viewers.get(viewer).setItem(45, ItemsUtils.getSkull("§8⫷ §7Previous", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIwZjZlOGFmNDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ=="));
					if (page.get(viewer) < Math.ceil(players.size() / 36)) viewers.get(viewer).setItem(53, ItemsUtils.getSkull("§7Next §8⫸", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ=="));
			
				}
					
			}.runTaskAsynchronously(Main.uhc);
			
		}
		
	}
	
	private enum Filter {
		
		ALL(), NETHER(), END(), CAVES(), MIDDLE();
		
		private Filter() {
			
			
			
		}
		
	}

}

package com.thetonyk.UHC.Features;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

public class LobbyProtection implements Listener {

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		
		if (!event.toWeatherState()) return;
		
		if (!event.getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		event.setCancelled(true);
		event.getWorld().setStorm(false);
		event.getWorld().setWeatherDuration(0);
		
	}
	
	@EventHandler
	public void onThunderChange(ThunderChangeEvent event) {
		
		if (!event.toThunderState()) return;
		
		if (!event.getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		event.setCancelled(true);
		event.getWorld().setThundering(false);
		event.getWorld().setThunderDuration(0);
		
	}
	
	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent event) {
		
		if (!event.getBlock().getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		if (event.getBlock().getType() == Material.REDSTONE_LAMP_ON) event.setNewCurrent(event.getOldCurrent());
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
	
		if (!event.getBlock().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
		event.setCancelled(true);
			
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		
		if (!event.getBlock().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
	
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		
		if (event.getAction() == Action.PHYSICAL) {
				
			event.setCancelled(true);
			return;
			
		}
			
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			switch(event.getClickedBlock().getType()) {
			
				case ANVIL:
				case BEACON:
				case BREWING_STAND:
				case CHEST:
				case WORKBENCH:
				case DISPENSER:
				case DROPPER:
				case ENCHANTMENT_TABLE:
				case ENDER_CHEST:
				case FURNACE:
				case BURNING_FURNACE:
				case HOPPER:
				case ITEM_FRAME:
				case LEVER:
				case BED_BLOCK:
				case TRAPPED_CHEST:
					event.setCancelled(true);
					return;
				default:
					break;
			
			}
				
		}
		
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
				
		switch (event.getRightClicked().getType()) {
		
			case ITEM_FRAME:
				event.setCancelled(true);
				return;
			default:
				break;
		
		}
		
	}
	
	@EventHandler
	public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
			
		if (!event.getEntity().getWorld().getName().equalsIgnoreCase("lobby")) return;

		event.setCancelled(true);
		event.setFoodLevel(20);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
	
		if (!event.getTo().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getTo().getY() > 0) return;
		
		event.getPlayer().teleport(event.getTo().getWorld().getSpawnLocation().add(0.5, 0, 0.5));
		
	}
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
		event.setCancelled(true);
		event.getBlockClicked().getState().update(true, true);
		
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
		event.setCancelled(true);
		event.getBlockClicked().getState().update(true, true);
		
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent event) {
	
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		event.setCancelled(true);
		event.getHook().remove();
		
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		
		if (!event.getFrom().getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onShears(PlayerShearEntityEvent event) {
		
		if (!event.getEntity().getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!event.getWhoClicked().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		if (event.getAction() != InventoryAction.HOTBAR_SWAP) return;

		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		
		if (!event.getEntity().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onCombust(EntityCombustEvent event) {
		
		if (!event.getEntity().getWorld().getName().equalsIgnoreCase("lobby")) return;
		
		new BukkitRunnable() {
			
			public void run() {
				
				event.getEntity().setFireTicks(0);
				
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}
	
}

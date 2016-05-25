package com.thetonyk.UHC.Features;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class TeleportProtection implements Listener {
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		
		World world = event.getWorld();
		
		if (!event.toWeatherState()) return;
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		world.setStorm(false);
		world.setWeatherDuration(0);
		
	}
	
	@EventHandler
	public void onThunderChange(ThunderChangeEvent event) {
		
		World world = event.getWorld();
		
		if (!event.toThunderState()) return;
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		world.setThundering(false);
		world.setThunderDuration(0);
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
			
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		Action action = event.getAction();
	
		if (GameUtils.getStatus() != Status.TELEPORT) return;
		
		if (action == Action.PHYSICAL) {
				
			event.setCancelled(true);
			return;
			
		}
		
		if (event.getClickedBlock() == null) return;
		
		Material type = event.getClickedBlock().getType();
			
		switch(type) {
		
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
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		
		EntityType type = event.getRightClicked().getType();
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
				
		switch (type) {
		
			case ITEM_FRAME:
			case VILLAGER:
			case MINECART_CHEST:
			case MINECART_FURNACE:
			case MINECART_HOPPER:
			case ARMOR_STAND:
				event.setCancelled(true);
				return;
			default:
				break;
		
		}
		
	}
	
	@EventHandler
	public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;

		event.setCancelled(true);
		event.setFoodLevel(20);
		player.setSaturation(20f);
		player.setExhaustion(0f);
	}
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		
		Block block = event.getBlockClicked();
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		block.getState().update(true, true);
		
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event) {
		
		Block block = event.getBlockClicked();
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		block.getState().update(true, true);
		
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent event) {
	
		if (GameUtils.getStatus() != Status.TELEPORT) return;
		
		event.setCancelled(true);
		event.getHook().remove();
		
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onShears(PlayerShearEntityEvent event) {
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		InventoryAction action = event.getAction();
		
		if (GameUtils.getStatus() != Status.TELEPORT) return;
		
		if (action != InventoryAction.HOTBAR_SWAP) return;

		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		
		Status status = GameUtils.getStatus();
		UUID uuid = event.getEntity().getUniqueId();
		
		if ((status != Status.TELEPORT && status != Status.PLAY) || DisplayTimers.time < 45 || !GameUtils.getOnGround(uuid))
		
		event.setCancelled(true);
		
	}
	
}

package com.thetonyk.UHC.Listener;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Game;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Game.Status;
import com.thetonyk.UHC.Inventories.InviteInventory;
import com.thetonyk.UHC.Inventories.RulesInventory;
import com.thetonyk.UHC.Utils.DisplayUtils;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT) {
			
			event.setCancelled(true);
			return;
	
		}
	
		if (event.getBlock().getWorld().getName().equals("lobby") && !event.getPlayer().hasPermission("global.build")) {
			
			event.setCancelled(true);
			return;
			
		}
			
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT) {
			
			event.setCancelled(true);
			return;
			
		}
	
		if (event.getBlock().getWorld().getName().equals("lobby") && !event.getPlayer().hasPermission("global.build")) {
			
			event.setCancelled(true);
			return;
			
		}
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT) {
			
			event.setCancelled(true);
			return;
			
		}
	
		if (event.getPlayer().getWorld().getName().equals("lobby")) {
			
			if (event.getAction() == Action.PHYSICAL) {
				
				event.setCancelled(true);
				return;
				
			}
			
			if (event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().equals("§b§lThe Rules §7(Right-Click)")) {
				
				event.setCancelled(true);	
				event.getPlayer().openInventory(RulesInventory.getRules());
				return;
					
			}
			
		}
		
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT) {
			
			event.setCancelled(true);
			return;
			
		}
		
		if (event.getPlayer().getWorld().getName().equals("lobby")) {
			
			if (event.getRightClicked() instanceof Player) {
				
				event.getPlayer().openInventory(InviteInventory.getInvite((Player) event.getRightClicked()));
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if (event.getEntity() instanceof Player || event.getEntity() instanceof Minecart ||event.getEntity() instanceof ArmorStand || event.getEntity() instanceof Painting || event.getEntity() instanceof ItemFrame) {
			
			if (Game.getStatus() == Status.TELEPORT) {
				
				event.setCancelled(true);
				return;
				
			}
			
			if (world.getName().equals("lobby")) {
				
				event.setCancelled(true);
				return;
				
			}
			
		}

	}
	
	@EventHandler
	public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT) {
			
			event.setCancelled(true);
			return;
			
		}
			
		if (event.getRightClicked().getWorld().getName().equals("lobby") && !event.getPlayer().hasPermission("global.build")) {
			
			event.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		
		if (Game.getStatus() == Status.TELEPORT) {
			
			event.setCancelled(true);
			event.setFoodLevel(20);
			return;
			
		}
			
		if (event.getEntity().getWorld().getName().equals("lobby")) {

			event.setCancelled(true);
			event.setFoodLevel(20);
						
		}
		
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		if (event.getPlayer().getWorld().getName().equals("lobby") && event.getItemDrop() != null && event.getItemDrop().getItemStack().hasItemMeta() && event.getItemDrop().getItemStack().getItemMeta().hasDisplayName() && event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§b§lThe Rules §7(Right-Click)")) {
			
			event.setCancelled(true);
			return;
			
		}
		
	}
	
	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		
		if (event.getEntity() instanceof Player) {
			
			if (event.getRegainReason() == RegainReason.REGEN || event.getRegainReason() == RegainReason.SATIATED) {
				
				event.setCancelled(true);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
			
			if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
				
				new BukkitRunnable() {
					public void run() {
						
						double health = ((Player) event.getEntity()).getHealth();
						String display = "§4";
						int count = 0;
						
						for (int i = 0; i <= health; i++) {
							
							display = display + "❤";
							health = health - 1;
							count++;
							
						}
						
						if (health > 0) {
							
							display = display + "§c❤";
							count++;
							
						}
						
						display = display + "§f";
						
						while (count < 10) {
							
							display = display + "❤";
							count++;
							
						}
						
						display = display +  "§7⫸ §a" + ((((Player) event.getEntity()).getHealth()) / 2) * 10;
						
						DisplayUtils.sendActionBar((Player) (((Arrow) event.getDamager()).getShooter()), display);
						
					}
				}.runTaskLater(Main.uhc, 1);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		
		if (event.getTo().getWorld().getName().equalsIgnoreCase("lobby") && event.getTo().getY() <= 0) {
			
			event.getPlayer().teleport(event.getTo().getWorld().getSpawnLocation().add(0.5, 0, 0.5));
			
		}
		
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby") && event.getPlayer().hasPermission("global.fly")) {
			
			event.getPlayer().setAllowFlight(true);
			return;
			
		}
		
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
		
		event.getPlayer().setAllowFlight(false);
		event.getPlayer().setFlying(false);
		return;
		
	}

}

package com.thetonyk.UHC.Features;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;

public class SpecInfo implements Listener {
	
	private static void send(String message) {
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			if (!GameUtils.getSpectate(online.getUniqueId())) continue;
			
			online.sendMessage("§8⫸ " + message);
			
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent event) {
		
		if (event instanceof EntityDamageByEntityEvent) return;
		
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		DamageCause type = event.getCause();
		double damages = event.getFinalDamage();
		String damage = "Damage";
		
		if (damages <= 0) return;
		
		switch (type) {
		
			case BLOCK_EXPLOSION:
				damage = "Explosion";
				break;
			case CONTACT:
				damage = "Cactus";
				break;
			case FALLING_BLOCK:
				damage = "Anvil";
				break;
			case FIRE_TICK:
				damage = "Fire";
				break;
			case MAGIC:
				damage = "Potion";
				break;
			case STARVATION:
				damage = "Food";
				break;
			case SUICIDE:
				return;
			default:
				damage = formatName(type.toString());
				break;
		
		}
		
		String finalDamage = damage;
		
		new BukkitRunnable() {
		
			public void run() {
			
				send("§6§oPVE §8| §7" + player.getName() + " §8| §a" + finalDamage + " §8| §7" + (int) (damages / 2) * 10 + "%");
			
			}
			
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		double damages = event.getFinalDamage();
		Entity attacker = event.getDamager();
		Format format = new DecimalFormat("##.#");
		String distance = format.format(attacker.getLocation().distance(player.getLocation()));
		
		if (damages <= 0) return;
		
		if (attacker instanceof Player && GameUtils.getSpectate(attacker.getUniqueId())) return;
		
		new BukkitRunnable() {
			
			public void run() {
		
				if (attacker instanceof Projectile) {
					
					Projectile projectile = (Projectile) attacker;
					String projDistance = format.format(((Entity) projectile.getShooter()).getLocation().distance(player.getLocation()));
					
					String weapon = "Weapon";
					
					switch (projectile.getType()) {
					
						case FISHING_HOOK:
							weapon = "Rod";
						case ARROW:
						case SNOWBALL:
						case EGG:
							weapon = formatName(projectile.getType().toString());
							break;
						default:
							return;
					
					}
					
					if (projectile.getShooter() instanceof Player) {
						
						Player killer = (Player) projectile.getShooter();
						
						send("§6§oPVP §8| §a" + killer.getName() + " §7⫸ §c" + player.getName() + " §8| §a" + weapon + " §8| §7" + (int) (damages / 2) * 10 + "% §8(§a" + projDistance + "m§8)");
						return;
						
					}
					
					if (projectile.getShooter() instanceof LivingEntity) {
						
						LivingEntity killer = (LivingEntity) projectile.getShooter();
						
						String mob = "Mob";
						
						switch (killer.getType()) {
						
							case ZOMBIE:
								Zombie zombie = (Zombie) killer;
								mob = zombie.isBaby() ? "Baby Zombie" : "Zombie";
								break;
							case SPLASH_POTION:
								mob = "Potion";
								break;
							case SMALL_FIREBALL:
								mob = "Fireball";
								break;
							case SKELETON:
								Skeleton skeleton = (Skeleton) killer;
								mob = skeleton.getSkeletonType() == SkeletonType.WITHER ? "Wither Skeleton" : "Skeleton";
								break;
							case PIG_ZOMBIE:
								mob = "Zombie Pigman";
								break;
							default:
								mob = formatName(killer.getType().toString());
								break;
						
						}
						
						send("§6§oPVE §8| §a" + mob + " §7⫸ §c" + player.getName() + " §8| §a" + weapon + " §8| §7" + (int) (damages / 2) * 10 + "% §8(§a" + projDistance + "m§8)");
						return;
						
					}
					
					send("§6§oPVE §8| §7" + player.getName() + " §8| §a" + weapon + " §8| §7" + (int) (damages / 2) * 10 + "% §8(§a" + projDistance + "m§8)");
					return;
					
				}
				
				if (attacker instanceof Player) {
					
					Player killer = (Player) attacker;
					
					send("§6§oPVP §8| §a" + killer.getName() + " §7⫸ §c" + player.getName() + " §8| §aMelee §8| §7" + (int) (damages / 2) * 10 + "% §8(§a" + distance + "m§8)");
					
				}
				
				String mob = "Mob";
				
				switch (attacker.getType()) {
				
					case ZOMBIE:
						Zombie zombie = (Zombie) attacker;
						mob = zombie.isBaby() ? "Baby Zombie" : "Zombie";
						break;
					case SPLASH_POTION:
						mob = "Potion";
						break;
					case SMALL_FIREBALL:
						mob = "Fireball";
						break;
					case SKELETON:
						Skeleton skeleton = (Skeleton) attacker;
						mob = skeleton.getSkeletonType() == SkeletonType.WITHER ? "Wither Skeleton" : "Skeleton";
						break;
					case PIG_ZOMBIE:
						mob = "Zombie Pigman";
						break;
					default:
						mob = formatName(attacker.getType().toString());
						break;
				
				}
				
				send("§6§oPVE §8| §a" + mob + " §7⫸ §c" + player.getName() + " §8| §7" + (int) (damages / 2) * 10 + "%");
				
			}
		
		}.runTaskLater(Main.uhc, 1);
		
	}
	
	List<Location> ores = new ArrayList<Location>();
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material type = block.getType();
		Location location = block.getLocation();
		
		if (GameUtils.getSpectate(player.getUniqueId())) return;
		
		if (type != Material.GOLD_ORE && type != Material.DIAMOND_ORE) return;
		
		if (ores.contains(location)) return;
		
		List<Block> vein = new ArrayList<Block>();
		List<Block> check = new ArrayList<Block>();
		
		vein.add(block);
		check.add(block);
		
		while (!check.isEmpty()) {
			
			Block checked = check.get(0);
			check.remove(0);
			
			for (BlockFace face : BlockFace.values()) {
				
				Block potential = checked.getRelative(face);
				
				if (potential.getType() != type) continue;
				
				if (vein.contains(potential)) continue;
				
				vein.add(potential);
				check.add(potential);
				
			}
			
		}
		
		for (Block loc : vein) {
			
			ores.add(loc.getLocation());
			
		}
		
		send("§6§oOres §8| §7" + player.getName() + " §8| §a" + formatName(type.toString().substring(0, type.toString().length() - 4)) + " §8| §7" + vein.size());
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent event) {
		
		Block block = event.getBlock();
		Material type = block.getType();
		Location location = block.getLocation();
		
		if (ores.contains(location)) return;
		
		if (type != Material.DIAMOND_ORE && type != Material.GOLD_ORE) return;
		
		ores.add(location);
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onSplashPotion(PotionSplashEvent event) {
		
		if (!(event.getPotion().getShooter() instanceof Player)) return;
		
		Player player = (Player) event.getPotion().getShooter();
		Potion potion = Potion.fromItemStack(event.getPotion().getItem());
		
		for (PotionEffect effect : potion.getEffects()) {
			
			send("§6§oPotion §8| §7" + player.getName() + " §8| §a" + getPotionName(effect.getType()) + " " + (effect.getAmplifier() + 1) + (effect.getDuration() > 0 ? " §8| §7" + effect.getDuration() / 20 : ""));
			
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPortal(PlayerPortalEvent event) {
		
		Player player = event.getPlayer();
		
		String oldEnvironment = getEnvironment(event.getFrom());
		String newEnvironment = getEnvironment(event.getTo());
		
		if (oldEnvironment == null || newEnvironment == null) return;
		
		send("§6§oPortal §8| §7" + player.getName() + " §8| §c" + oldEnvironment + " §7⫸ §a" + newEnvironment);
		
	}
	
	private static String getEnvironment(Location location) {
		
		String world = null;
		
		if (location == null) return world;
		
		switch (location.getWorld().getEnvironment()) {
		
			case NORMAL:
				world = "§aOverworld";
				break;
			case NETHER:
				world = "§cNether";
				break;
			case THE_END:
				world = "§9The End";
				break;
			default:
				world = "§eUnknown";
				break;
			
		}
		
		return world;
		
	}
	
	private static String formatName(String name) {
		
		String formated = "";
		String[] convert = name.split("_");
		
		for (int i = 0; i < convert.length; i++) {
			
			formated += convert[i].substring(0, 1).toUpperCase();
			formated += convert[i].substring(1, convert[i].length()).toLowerCase();
			
			if (i < (convert.length - 1)) formated += " ";
			
		}
		
		return formated;
		
	}
	
	//LeonTG77's method
	public static String getPotionName(final PotionEffectType type) {
        switch (type.getName().toLowerCase()) {
        case "speed":
            return "Speed";
        case "slow":
            return "Slowness";
        case "fast_digging":
            return "Haste";
        case "slow_digging":
            return "Mining fatigue";
        case "increase_damage":
            return "Strength";
        case "heal":
            return "Instant Health";
        case "harm":
            return "Instant Damage";
        case "jump":
            return "Jump Boost";
        case "confusion":
            return "Nausea";
        case "regeneration":
            return "Regeneration";
        case "damage_resistance":
            return "Resistance";
        case "fire_resistance":
            return "Fire Resistance";
        case "water_breathing":
            return "Water breathing";
        case "invisibility":
            return "Invisibility";
        case "blindness":
            return "Blindness";
        case "night_vision":
            return "Night Vision";
        case "hunger":
            return "Hunger";
        case "weakness":
            return "Weakness";
        case "poison":
            return "Poison";
        case "wither":
            return "Wither";
        case "health_boost":
            return "Health Boost";
        case "absorption":
            return "Absorption";
        case "saturation":
            return "Saturation";
        default:
            return "???";
        }
    }

}

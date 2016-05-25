package com.thetonyk.UHC.Inventories;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.ItemsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

import net.minecraft.server.v1_8_R3.EntityPlayer;

public class PlayerInventory implements Listener {
	
	private static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();
	public static Map<String, String> prefix = new HashMap<String, String>(); //Send a SQL request every tick to get the team prefix is definitely not a good idea 
	
	public PlayerInventory() {
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Map.Entry<UUID, Inventory> inventory : new HashMap<UUID, Inventory>(inventories).entrySet()) {
					
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
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		
		ItemStack itemInHand = player.getItemInHand();
		ItemStack itemOnCursor = player.getItemOnCursor();
		ItemStack helmet = player.getInventory().getHelmet();
		ItemStack chestplate = player.getInventory().getChestplate();
		ItemStack leggings = player.getInventory().getLeggings();
		ItemStack boots = player.getInventory().getBoots();
		ItemStack[] content = player.getInventory().getContents();
		
		Format format = new DecimalFormat("##.##");
		
		double health = player.getHealth();
		double maxHealth = player.getMaxHealth();
		double absorptionHealth = nmsPlayer.getAbsorptionHearts();
		int air = player.getRemainingAir();
		int maxAir = player.getMaximumAir();
		int food = player.getFoodLevel();
		float saturation = player.getSaturation();
		float exhaustion = player.getExhaustion();
		Collection<PotionEffect> effects = player.getActivePotionEffects();
		String playerTeam = TeamsUtils.getTeam(uuid);
		
		List<String> teammates = new ArrayList<String>();
		
		if (playerTeam != null) {
			
			if (!prefix.containsKey(playerTeam)) {
				
				prefix.put(playerTeam, TeamsUtils.getTeamPrefix(uuid));
				
			}
			
			for (UUID mate : TeamsUtils.getTeamMembers(playerTeam)) {
				
				teammates.add(PlayerUtils.getRank(mate).getPrefix() + prefix.get(playerTeam) + PlayerUtils.getName(PlayerUtils.getId(mate)));
				
			}
			
		}
		
		String teamPrefix = playerTeam == null ? "§7" : prefix.get(playerTeam);
		
		new BukkitRunnable() {
			
			public void run() {
		
				ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
				
				List<String> lore = new ArrayList<String>();
				
				ItemStack healthBanner = new ItemStack(Material.BANNER);
				BannerMeta healthMeta = (BannerMeta) healthBanner.getItemMeta();
				healthMeta.setBaseColor(DyeColor.RED);
				healthMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
				healthMeta.addPattern(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL));
				healthMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE));
				healthMeta.addPattern(new Pattern(DyeColor.RED, PatternType.TRIANGLE_TOP));
				healthMeta.setDisplayName("§8⫸ §6Health & Food §8⫷");
				lore.add(" ");
				lore.add("§8⫸ §7Health: §a" + (health / 2) * 10 + "%");
				lore.add("§8⫸ §7Max Health: §a" + (maxHealth / 2) * 10 + "%");
				lore.add("§8⫸ §7Absorption: §a" + (absorptionHealth / 2) * 10 + "%");
				lore.add(" ");
				lore.add("§8⫸ §7Remaining Air: " + (air < maxAir ? "§a" + (int) air / 20 + "s" : "§cNone"));
				lore.add(" ");
				lore.add("§8⫸ §7Food level: §a" + food);
				lore.add("§8⫸ §7Saturation: §a" + saturation);
				lore.add("§8⫸ §7Exhaustion: §a" + format.format(exhaustion));
				lore.add(" ");
				healthMeta.setLore(lore);
				healthMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				healthBanner.setItemMeta(healthMeta);
				
				lore.clear();
				
				ItemStack potion = new ItemStack(Material.POTION);
				ItemMeta potionMeta = potion.getItemMeta();
				potionMeta.setDisplayName("§8⫸ §6Potion effects §8⫷");
				lore.add(" ");
				
				if (effects.size() < 1) {
					
					lore.add("§8⫸ §cNone");
					lore.add(" ");
					
				} else {
			
					for (PotionEffect effect : effects) {
						
						lore.add("§8⫸ §6" + getPotionName(effect.getType()) + "§7:");
						lore.add("§8⫸   §7Level: §a" + (effect.getAmplifier() + 1));
						lore.add("§8⫸   §7Duration: §a" + effect.getDuration() / 20 + "s");
						lore.add("");
						
					}
				
				}

				potionMeta.setLore(lore);
				potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				potion.setItemMeta(potionMeta);
				
				lore.clear();
				
				ItemStack team = new ItemStack(Material.NAME_TAG);
				ItemMeta teamMeta = team.getItemMeta();
				teamMeta.setDisplayName("§8⫸ §6Team §8⫷");
				lore.add(" ");
				lore.add("§8⫸ §7Team: " + (playerTeam == null ? "§cNone" : teamPrefix + playerTeam));
				
				if (playerTeam != null && !teammates.isEmpty()) {
					
					lore.add(" ");
					lore.add("§8⫸ §6Teammates§7:");
					
					for (String mate : teammates) {
						
						lore.add("§8⫸   " + mate);
						
					}
					
				}
				
				lore.add(" ");
				teamMeta.setLore(lore);
				team.setItemMeta(teamMeta);
				
				lore.clear();
				
				ItemStack kills = new ItemStack(Material.SIGN);
				ItemMeta killsMeta = kills.getItemMeta();
				
				
				inventory.setItem(0, separator);
				inventory.setItem(1, healthBanner);
				inventory.setItem(2, potion);
				inventory.setItem(3, separator);
				inventory.setItem(4, team);
				//Kills
				inventory.setItem(6, separator);
				//Ores/Mined ores/XP
				inventory.setItem(8, separator);
				
				
				for (int i = 9; i < content.length; i++) {
					
					inventory.setItem(i, content[i]);
					
				}
				
				inventory.setItem(35,separator);
				inventory.setItem(36, itemInHand);
				inventory.setItem(37, ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7⫷ §6Item in hand", 1, 7));
				inventory.setItem(38, itemOnCursor);
				inventory.setItem(39, ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7⫷ §6Item on cursor", 1, 7));
				inventory.setItem(40, helmet);
				inventory.setItem(41, chestplate);
				inventory.setItem(42, leggings);
				inventory.setItem(43, boots);
				inventory.setItem(44, separator);
				
				for (int i = 0; i <= 8; i++) {
					
					inventory.setItem(i + 45, content[i]);
					
				}
				
				new BukkitRunnable() {
				
					public void run() {
					
						for (HumanEntity viewer : inventory.getViewers()) {
							
							if (!(viewer instanceof Player)) continue;
							
							((Player) viewer).updateInventory();
							
						}
					
					}
				
				}.runTask(Main.uhc);
				
			}
		
		}.runTaskAsynchronously(Main.uhc);
		
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

package com.thetonyk.UHC.Features;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.ItemsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.PlayerUtils.Rank;
import com.thetonyk.UHC.Utils.WorldUtils;

public class SpecPlayer implements Listener {
	
	public static void enable(UUID uuid) {
		
		GameUtils.setSpectate(uuid, true);
		
		if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;
		
		Player spectator = Bukkit.getPlayer(uuid);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (!GameUtils.getDeath(player.getUniqueId())) player.hidePlayer(spectator);
			else player.showPlayer(spectator);
			
			if (!GameUtils.getDeath(player.getUniqueId()) || GameUtils.getSpectate(player.getUniqueId())) spectator.showPlayer(player);
			else spectator.hidePlayer(player);
			
		}
		
		PlayerUtils.clearInventory(spectator);
		PlayerUtils.clearEffects(spectator);
		PlayerUtils.clearXp(spectator);
		spectator.setGameMode(GameMode.SPECTATOR);
		spectator.setPlayerListName("§7§o" + spectator.getName());
		
		
		
	}
	
	public static void disable(UUID uuid) {
		
		GameUtils.setSpectate(uuid, false);
		
		if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;
		
		Player spectator = Bukkit.getPlayer(uuid);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.hidePlayer(spectator);
			
			if (!GameUtils.getDeath(player.getUniqueId()) || GameUtils.getSpectate(player.getUniqueId())) spectator.showPlayer(player);
			else spectator.hidePlayer(player);
			
		}
		
		PlayerUtils.clearInventory(spectator);
		PlayerUtils.clearEffects(spectator);
		spectator.setGameMode(GameMode.ADVENTURE);
		spectator.setPlayerListName(null);
		spectator.teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
		setItems(spectator);
		
	}
	
	private static void setItems(Player spectator) {
		
		ItemStack teleportMiddle = new ItemStack(Material.NETHER_STAR);
		ItemMeta teleportMiddleMeta = teleportMiddle.getItemMeta();
		teleportMiddleMeta.setDisplayName("§6Teleport to the middle §7(Click on it)");
		teleportMiddle.setItemMeta(teleportMiddleMeta);
		
		spectator.getInventory().setItem(2, teleportMiddle);
		
		ItemStack nightVision = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta nightVisionMeta = nightVision.getItemMeta();
		nightVisionMeta.setDisplayName("§6Night Vision §7(Click on it)");
		nightVision.setItemMeta(nightVisionMeta);
		if (spectator.hasPotionEffect(PotionEffectType.NIGHT_VISION)) nightVision = ItemsUtils.addGlow(nightVision);
		
		spectator.getInventory().setItem(6, nightVision);
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent event) {
		
		if (!GameUtils.getSpectate(event.getPlayer().getUniqueId())) return;
		
		if (PlayerUtils.getRank(event.getPlayer().getName()) == Rank.ADMIN || PlayerUtils.getRank(event.getPlayer().getName()) == Rank.HOST || PlayerUtils.getRank(event.getPlayer().getName()) == Rank.STAFF) return;
		
		event.setCancelled(true);
		event.getPlayer().sendMessage(Main.PREFIX + "You can't speak in chat when you are spectator.");
		
	}
	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent event) {
		
		if (!GameUtils.getSpectate(event.getPlayer().getUniqueId())) return;
		
		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
		
		if (GameUtils.getAlives().isEmpty()) {
			
			event.getPlayer().sendMessage(Main.PREFIX + "There are no players alives.");
			return;
			
		}
		
		Player teleport = null;
		
		while (teleport == null) {
			
			teleport = Bukkit.getPlayer(GameUtils.getAlives().get(new Random().nextInt(GameUtils.getAlives().size())));
			
		}
		
		event.getPlayer().teleport(teleport);
		
	}
	
	@EventHandler
	public void onRightClickEntity(PlayerInteractEntityEvent event) {
		
		
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) return;
		
		if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§6Night Vision §7(Click on it)")) {
			
			event.setCancelled(true);
			
			if (event.getWhoClicked().hasPotionEffect(PotionEffectType.NIGHT_VISION)) event.getWhoClicked().removePotionEffect(PotionEffectType.NIGHT_VISION);
			else event.getWhoClicked().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
			
			return;
			
		}
		
		if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§6Teleport to the middle §7(Click on it)")) {
			
			event.setCancelled(true);
			
			Location center = Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getCenter();
			center.setY(WorldUtils.getHighestY((int) Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getCenter().getX(), (int) Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getCenter().getZ(), Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getCenter().getWorld()));
			event.getWhoClicked().teleport(center);
			
			return;
			
		}
		
	}

}

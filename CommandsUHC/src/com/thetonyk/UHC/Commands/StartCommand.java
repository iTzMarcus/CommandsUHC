package com.thetonyk.UHC.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.StartEvent;
import com.thetonyk.UHC.Features.DisplaySidebar;
import com.thetonyk.UHC.Features.DisplayTimers;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeleportUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class StartCommand implements CommandExecutor {
	
	private Boolean start = false;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.start")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		Status status = GameUtils.getStatus();
		String gameWorld = GameUtils.getWorld();
		
		if (gameWorld == null) {
			
			sender.sendMessage(Main.PREFIX + "You need to setup the game first.");
			return true;
			
		}
		
		switch (status) {
		
			case NONE:
			case OPEN:
				sender.sendMessage(Main.PREFIX + "The game is not ready.");
				return true;
			case READY:
				sender.sendMessage(Main.PREFIX + "You need to teleport players first.");
				return true;
			case PLAY:
			case END:
				sender.sendMessage(Main.PREFIX + "Game has already started.");
				return true;
			default:
				break;
		
		}
		
		if (start) {
			
			sender.sendMessage(Main.PREFIX + "You have already started the game.");
			return true;
			
		}
		
		start = true;
		Map<UUID, Boolean> nosounds = new HashMap<UUID, Boolean>();
		final World world = Bukkit.getWorld(gameWorld);
		
		if (world == null) {
			
			sender.sendMessage(Main.PREFIX + "The game world is not loaded.");
			return true;
			
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayUtils.sendTitle(player, "§45", "", 0, 20, 0);
			
			nosounds.put(player.getUniqueId(), PlayerUtils.getNosoundState(player) == 1 ? true : false);
			
			if (nosounds.get(player.getUniqueId())) continue;
			
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendTitle(player, "§c4", "", 0, 20, 0);
					
					if (!nosounds.containsKey(player.getUniqueId())) nosounds.put(player.getUniqueId(), PlayerUtils.getNosoundState(player) == 1 ? true : false);
					
					if (nosounds.get(player.getUniqueId())) continue;
					
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
				}
				
			}
			
		}.runTaskLater(Main.uhc, 20);
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendTitle(player, "§63", "", 0, 20, 0);
					
					if (!nosounds.containsKey(player.getUniqueId())) nosounds.put(player.getUniqueId(), PlayerUtils.getNosoundState(player) == 1 ? true : false);
					
					if (nosounds.get(player.getUniqueId())) continue;
					
					player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
					
				}
				
			}
			
		}.runTaskLater(Main.uhc, 40);
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendTitle(player, "§22", "", 0, 20, 0);
					
					if (!nosounds.containsKey(player.getUniqueId())) nosounds.put(player.getUniqueId(), PlayerUtils.getNosoundState(player) == 1 ? true : false);
					
					if (nosounds.get(player.getUniqueId())) continue;
					
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
				}
				
			}
			
		}.runTaskLater(Main.uhc, 60);
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendTitle(player, "§a1", "", 0, 20, 0);
					
					if (!nosounds.containsKey(player.getUniqueId())) nosounds.put(player.getUniqueId(), PlayerUtils.getNosoundState(player) == 1 ? true : false);
					
					if (nosounds.get(player.getUniqueId())) continue;
					
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
				}
				
			}
			
		}.runTaskLater(Main.uhc, 80);
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (int i = 0; i < 150; i++) {
					
					Bukkit.broadcastMessage("");
					
				}
				
				Bukkit.broadcastMessage(Main.PREFIX + "The game is starting now.");
				Bukkit.broadcastMessage(Main.PREFIX + "Timers:");
				Bukkit.broadcastMessage("§8⫸ §7Final Heal: §a45 seconds§7.");
				Bukkit.broadcastMessage("§8⫸ §7PVP: §a" + (int) Math.floor(DisplayTimers.pvpTime / 60) + " minutes§7.");
				Bukkit.broadcastMessage("§8⫸ §7Meetup: §a" + (int) Math.floor(DisplayTimers.meetupTime / 60) + " minutes§7.");
				Bukkit.broadcastMessage(Main.PREFIX + "Good luck & Have Fun!");
				
				world.setTime(0);
				world.setStorm(false);
				world.setThundering(false);
				world.setGameRuleValue("doDaylightCycle", "true");
				world.setDifficulty(Difficulty.HARD);
				world.setSpawnFlags(false, true);
				world.setPVP(false);
				WorldUtils.butcher(world);
				GameUtils.setStatus(Status.PLAY);
				Bukkit.getPluginManager().callEvent(new StartEvent());
				DisplayTimers.startTimer();
				start = false;
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if (GameUtils.getSpectate(player.getUniqueId())) continue;
					
					DisplayUtils.sendTitle(player, "", "§7Go§a! §7Go§a! §7Go§a!", 5, 30, 5);
					player.closeInventory();
					player.setGameMode(GameMode.SURVIVAL);
					PlayerUtils.clearEffects(player);
					PlayerUtils.clearInventory(player);
					PlayerUtils.clearXp(player);
					PlayerUtils.feed(player);
					PlayerUtils.heal(player);
					DisplaySidebar.update(player);
					
					if (!nosounds.containsKey(player.getUniqueId())) nosounds.put(player.getUniqueId(), PlayerUtils.getNosoundState(player) == 1 ? true : false);
					
					if (nosounds.get(player.getUniqueId())) continue;
					
					player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
					
				}
				
				TeleportUtils.removeSpawns(GameUtils.getLocations());
				
			}
			
		}.runTaskLater(Main.uhc, 100);
			
		return true;
		
	}

}

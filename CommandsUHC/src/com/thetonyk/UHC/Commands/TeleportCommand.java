package com.thetonyk.UHC.Commands;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.TeleportEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.TeleportUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class TeleportCommand implements CommandExecutor, Listener {
	
	private Boolean teleport = false;
	public static Map<UUID, Location> locations = null;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.teleport")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (GameUtils.getWorld() == null) {
			
			sender.sendMessage(Main.PREFIX + "You need to setup the game first.");
			return true;
			
		}
		
		if (GameUtils.getStatus() == Status.NONE) {
			
			sender.sendMessage(Main.PREFIX + "The game is not ready.");
			return true;
			
		}
		
		if (GameUtils.getTeleported()) {
			
			sender.sendMessage(Main.PREFIX + "You have already teleported players.");
			return true;
			
		}
		
		if (teleport) {
			
			sender.sendMessage(Main.PREFIX + "You have already started the teleportation.");
			return true;
			
		}
		
		locations = TeleportUtils.getSpawns(Bukkit.getWorld(GameUtils.getWorld()), WorldUtils.getSize(GameUtils.getWorld()));
		
		GameUtils.setStatus(Status.TELEPORT);
		teleport = true;
		
		TeleportUtils.loadSpawns(locations);

		new BukkitRunnable() {
			
			public void run() {
		
				Bukkit.broadcastMessage(Main.PREFIX + "Started teleportation of players...");
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
				}
		
			}
		
		}.runTaskLater(Main.uhc, 20);
			
		return true;
		
	}
	
	@EventHandler
	public void onTeleportFinish (TeleportEvent event) {
		
		GameUtils.setTeleported(true);
		teleport = false;
		
		Bukkit.broadcastMessage(Main.PREFIX + "All players have been teleported.");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
			
		}
		
	}

}

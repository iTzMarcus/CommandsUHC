package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.TeleportEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.ScatterUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class TeleportCommand implements CommandExecutor, Listener {
	
	private Boolean teleport = false;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.teleport")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (GameUtils.getWorld() == null) {
			
			sender.sendMessage(Main.PREFIX + "You need to setup the game first.");
			return true;
			
		}
		
		if (GameUtils.getStatus() != Status.READY) {
			
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
		
		GameUtils.setStatus(Status.TELEPORT);
		teleport = true;
		
		ScatterUtils.loadSpawns(ScatterUtils.getSpawns(Bukkit.getWorld(GameUtils.getWorld()), WorldUtils.getSize(GameUtils.getWorld())));

		Bukkit.broadcastMessage(Main.PREFIX + "Started teleportation of players...");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
			
		}
			
		return true;
		
	}
	
	@EventHandler
	public void onTeleportFinish (TeleportEvent event) {
		
		GameUtils.setTeleported(true);
		teleport = false;
		
	}

}

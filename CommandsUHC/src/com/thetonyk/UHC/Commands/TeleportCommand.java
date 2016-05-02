package com.thetonyk.UHC.Commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.ScatterUtils;

public class TeleportCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Map<String, Location> locations = ScatterUtils.getSpawns(Bukkit.getWorld("test"), 1000);
		
		ScatterUtils.loadSpawns(locations);
		
		new BukkitRunnable() {
			
			public void run() {
				
				ScatterUtils.teleportPlayers(locations);
				
			}
			
		}.runTaskLater(Main.uhc, 20);
			
		return true;
		
	}

}

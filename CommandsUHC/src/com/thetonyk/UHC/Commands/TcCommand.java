package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class TcCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.team")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (TeamsUtils.getTeam(Bukkit.getPlayer(sender.getName()).getUniqueId()) == null) {
			
			sender.sendMessage(Main.PREFIX + "You are not in a team.");
			return true;
			
		}
		
		Player player = Bukkit.getPlayer(sender.getName());
		
		String world = "Overworld";
		String color = "§7";
		
		switch (player.getWorld().getEnvironment().toString()) {
		
		case "NORMAL":
			world = "Overworld";
			color = "§a";
			break;
		case "NETHER":
			world = "Nether";
			color = "§c";
			break;
		case "THE_END":
			world = "The End";
			color = "§9";
			break;
		default:
			world = "Unknown";
			break;
			
		}
		
		TeamsUtils.sendMessage(TeamsUtils.getTeam(Bukkit.getPlayer(sender.getName()).getUniqueId()), "§6Team §8| §7" + sender.getName() + " §8⫸ §7x: §6" + player.getLocation().getBlockX() + " §7y: §6" + player.getLocation().getBlockY() + " §7z: §6" + player.getLocation().getBlockZ() + " §8| " + color + world);
		return true;
		
	}

}


package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.PlayerUtils;

public class ClearCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("uhc.clear")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " [player] [xp]");
			return true;
			
		}
		
		Boolean xp = false;
		
		if (args.length > 1) {
			
			if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
				
				sender.sendMessage(Main.PREFIX + "The XP can only be '§6true§7' or '§6false§7'.");
				return true;
				
			}
			
			xp = Boolean.parseBoolean(args[1]);
			
		}
		
		if (args[0].equalsIgnoreCase("*")) {
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				PlayerUtils.clearInventory(player);
				if (xp) PlayerUtils.clearXp(player);
				
			}
			
			Bukkit.broadcastMessage(Main.PREFIX + "The inventories of all players are been cleared.");
			return true;
			
		}
		
		Player player = Bukkit.getPlayer(sender.getName());
		
		if (args.length > 0) {
		
			player = Bukkit.getPlayer(args[0]);
			
			if (player == null) {
				
				sender.sendMessage(Main.PREFIX + "The player '§6" + args[0] + "§7' is not online.");
				return true;
				
			}
			
		}
		
		if (xp) PlayerUtils.clearXp(player);
		PlayerUtils.clearInventory(player);
		
		if (sender.getName() != player.getName()) sender.sendMessage(Main.PREFIX + "The inventory of player '§6" + player.getName() + "§7' was cleared.");
		
		player.sendMessage(Main.PREFIX + "Your inventory was cleared.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.clear")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (Player player : Bukkit.getOnlinePlayers()) {
				
				complete.add(player.getName());
				
			}
			
		} else if (args.length == 2) {
			
			complete.add("true");
			complete.add("false");
			
		}
		
		List<String> tabCompletions = new ArrayList<String>();
		
		if (args[args.length - 1].isEmpty()) {
			
			for (String type : complete) {
				
				tabCompletions.add(type);
				
			}
			
		} else {
			
			for (String type : complete) {
				
				if (type.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) tabCompletions.add(type);
				
			}
			
		}
		
		return tabCompletions;
		
	}

}

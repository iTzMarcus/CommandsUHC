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

public class HealCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("uhc.heal")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " [player]");
			return true;
			
		}
		
		if (args.length > 0 && args[0].equalsIgnoreCase("*")) {
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				PlayerUtils.heal(player);
				
			}
			
			Bukkit.broadcastMessage(Main.PREFIX + "All players have been healed.");
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
		
		PlayerUtils.heal(player);
		
		if (sender.getName() != player.getName()) sender.sendMessage(Main.PREFIX + "The player '§6" + player.getName() + "§7' has been healed.");
		
		player.sendMessage(Main.PREFIX + "You have been healed.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.heal")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (Player player : Bukkit.getOnlinePlayers()) {
				
				complete.add(player.getName());
				
			}
			
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

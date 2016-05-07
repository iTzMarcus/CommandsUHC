package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.WorldUtils;

public class PVPCommand implements CommandExecutor, TabCompleter{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.pvp")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <world> <true|false>");
			return true;
			
		}
			
		if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
			
			sender.sendMessage(Main.PREFIX + "The PVP can only be '§6true§7' or '§6false§7'.");
			return true;
			
		}
		
		Boolean pvp = Boolean.parseBoolean(args[1]);
		
		if (!WorldUtils.exist(args[0])) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + args[0] + "§7' doesn't exist.");
			return true;
			
		}
		
		if (Bukkit.getWorld(args[0]) == null) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + args[1] + "§7' is not loaded.");
			return true;
			
		}
		
		Bukkit.getWorld(args[0]).setPVP(pvp);
		sender.sendMessage(Main.PREFIX + "The PVP has been " + (pvp ? "enabled" : "disabled") + "§7in the world '§6" + Bukkit.getWorld(args[0]).getName() + "'.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.pvp")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (World world : Bukkit.getWorlds()) {
				
				complete.add(world.getName());
				
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

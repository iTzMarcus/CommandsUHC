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
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <world> <on|off>");
			return true;
			
		}
			
		if (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off")) {
			
			sender.sendMessage(Main.PREFIX + "The PVP can only be '§6on§7' or '§6off§7'.");
			return true;
			
		}
		
		Boolean pvp = args[1].equalsIgnoreCase("on") ? true : false;
		String name = args[0];
		World world = Bukkit.getWorld(name);
		
		if (!WorldUtils.exist(name)) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + name + "§7' doesn't exist.");
			return true;
			
		}
		
		if (world == null) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + name + "§7' is not loaded.");
			return true;
			
		}
		
		world.setPVP(pvp);
		sender.sendMessage(Main.PREFIX + "The PVP has been " + (pvp ? "enabled" : "disabled") + " §7in the world '§6" + name + "§7'.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.pvp")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (World world : Bukkit.getWorlds()) {
				
				if (world.getName().equalsIgnoreCase("lobby")) continue;
				
				complete.add(world.getName());
				
			}
			
		} else if (args.length == 2) {
			
			complete.add("on");
			complete.add("off");
			
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

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

public class ButcherCommand implements CommandExecutor, TabCompleter {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("global.butcher")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <world>");
			return true;
			
		}
		
		String name = args[0];
		World world = Bukkit.getWorld(name);
		
		if (!WorldUtils.exist(name) && !name.equalsIgnoreCase("lobby")) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + name + "§7' doesn't exist.");
			return true;
			
		}
						
		if (world == null) {
				
			sender.sendMessage(Main.PREFIX + "The world '§6" + name + "§7' is not loaded.");
			return true;
			
		}
		
		int count = WorldUtils.butcher(world);
		
		Bukkit.broadcastMessage(Main.PREFIX + "§a" + count + "§7 entites killed.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("global.butcher")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {
			
			for (World world : Bukkit.getWorlds()) {
				
				if (world.getName().equalsIgnoreCase("lobby")) continue;
				
				complete.add(world.getName());
				
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


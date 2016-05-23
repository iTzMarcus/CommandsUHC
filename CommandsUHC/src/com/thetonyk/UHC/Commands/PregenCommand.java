package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class PregenCommand implements CommandExecutor, TabCompleter{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.pregen")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <world|pause|cancel>");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("pause")) {
			
			Bukkit.broadcastMessage(Main.PREFIX + "Pausing pregeneration of world.");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator fill pause");
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				DisplayUtils.sendActionBar(player, "");
				
			}
			
			return true;
			
		}
		else if (args[0].equalsIgnoreCase("cancel")) {
			
			Bukkit.broadcastMessage(Main.PREFIX + "Cancelling pregeneration of world.");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator fill cancel");
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				DisplayUtils.sendActionBar(player, "");
				
			}
			
			return true;
			
		}
		
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
		
		int radius = WorldUtils.getSize(name) / 2;
		int x = world.getWorldBorder().getCenter().getBlockX();
		int z = world.getWorldBorder().getCenter().getBlockZ();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator " + name + " set " + radius + " " + radius + " " + x + " " + z);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator " + name + " fill 500");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator fill confirm");
	
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.pregen")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (World world : Bukkit.getWorlds()) {
				
				if (world.getName().equalsIgnoreCase("lobby")) continue;
				
				complete.add(world.getName());
				
			}
			
			complete.add("pause");
			complete.add("cancel");
			
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

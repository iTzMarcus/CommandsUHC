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
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class BorderCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.border")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		String gameWorld = GameUtils.getWorld();
		
		if (args.length == 0 || !sender.hasPermission("uhc.setborder")) {
			
			if (gameWorld == null || Bukkit.getWorld(gameWorld) == null) {
				
				sender.sendMessage(Main.PREFIX + "The game is not ready.");
				return true;
				
			}
			
			int size = (int) Bukkit.getWorld(gameWorld).getWorldBorder().getSize();
			sender.sendMessage(Main.PREFIX + "Border size: §6" + size + "§7x§6" + size + "§7.");
			return true;
			
		}
		
		String name = args[0];
		World world = Bukkit.getWorld(name);
		
		if (!WorldUtils.exist(name)) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + name + "§7' doesn't exist.");
			return true;
			
		}
		
		if (args.length == 1) {
			
			int size = world == null ? (int) WorldUtils.getSize(name) : (int) world.getWorldBorder().getSize();
			sender.sendMessage(Main.PREFIX + "Size of world '§6" + name + "§7': §a" + size + "§7x§a" + size + "§7.");
			return true;
			
		}
		
		int size;
		
		try {
			
			size = Integer.parseInt(args[1]);
			
		} catch (Exception exception) {
			
			sender.sendMessage(Main.PREFIX + "Please enter a valid size of world.");
			return true;
			
		}
		
		if (size > 10000 || size < 100) {
			
			sender.sendMessage(Main.PREFIX + "Please enter a valid size of world.");
			return true;
			
		}
		
		WorldUtils.setSize(name, size);
		
		if (world != null) world.getWorldBorder().setSize(size);
		
		Bukkit.broadcastMessage(Main.PREFIX + "Size of world '§6" + name + "§7' set to: §a" + size + "§7x§a" + size + "§7.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.setborder")) return null;
		
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

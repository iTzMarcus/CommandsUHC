package com.thetonyk.UHC.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.MessengerListener;
import com.thetonyk.UHC.Utils.DatabaseUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class BorderCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.border")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length == 0) {
			
			//Incoming border game size
			return true;
			
		}
		
		if (!sender.hasPermission("uhc.setborder")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (!WorldUtils.exist(args[0])) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + args[0] + "§7' doesn't exist.");
			return true;
			
		}
		
		if (args.length == 1) {
			
			int radius;
			
			try {
				
				ResultSet worldDB = DatabaseUtils.sqlQuery("SELECT * FROM uhc_worlds WHERE name='" + args[0] + "' AND server = '" + MessengerListener.lastServer + "';");
				
				worldDB.next();
				
				radius = worldDB.getInt("size");
				
				worldDB.close();
				
			} catch (SQLException exception) {
				
				Bukkit.getLogger().severe("[BorderCommand] Error to fetch informations of world " + args[0] + " in DB.");
				sender.sendMessage(Main.PREFIX + "Error to fetch data of '§6" + args[1] + "§7'.");
				return true;
				
			}
			
			sender.sendMessage(Main.PREFIX + "Size of world '§6" + args[0] + "§7': §a" + radius + "§7x§a" + radius + "§7.");
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
		
		DatabaseUtils.sqlInsert("UPDATE uhc_worlds SET size = '" + args[1] + "' WHERE name = '" + args[0] + "' AND server = '" + MessengerListener.lastServer + "';");
			
		if (Bukkit.getWorld(args[0]) != null) {
			
			Bukkit.getWorld(args[0]).getWorldBorder().setSize(size);
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "Size of world '§6" + args[0] + "§7' set to: §a" + size + "§7x§a" + size + "§7.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.border")) return null;
		
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

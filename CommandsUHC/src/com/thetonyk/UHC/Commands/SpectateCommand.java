package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Features.SpecPlayer;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;

public class SpectateCommand implements CommandExecutor, TabCompleter {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.spectate") && !sender.hasPermission("uhc.spectate.all")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage of /" + label + ":");
			sender.sendMessage("§8⫸ §6/" + label + " on|off" + (sender.hasPermission("uhc.spectate.all") ? " <player>" : "") + "§8- §7Enable the spec mode.");
			sender.sendMessage("§8⫸ §6/" + label + " info" + (sender.hasPermission("uhc.spectate.all") ? " <player>" : "") + "§8- §7Toggle the spec info.");
			sender.sendMessage("§8⫸ §6/" + label + " list §8- §7List whitelisted players.");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
			
			Boolean state = args[0].equalsIgnoreCase("on") ? true : false;
			UUID player = Bukkit.getPlayer(sender.getName()).getUniqueId();
			
			if (args.length > 1 && sender.hasPermission("uhc.spectate.all")) {
				
				player = PlayerUtils.getUUID(args[1]);
				
				if (player == null) {
					
					sender.sendMessage(Main.PREFIX + "This player is not know on this server.");
					return true;
					
				}
				
			}
			
			if (args[0].equalsIgnoreCase("on") && GameUtils.getSpectate(player)) {
				
				sender.sendMessage(Main.PREFIX + "The spec mode is already §a" + args[0].toLowerCase() + "§7.");
				return true;
				
			}
			
			if (args[0].equalsIgnoreCase("off") && !GameUtils.getSpectate(player)) {
				
				sender.sendMessage(Main.PREFIX + "The spec mode is already §a" + args[0].toLowerCase() + "§7.");
				return true;
				
			}

			if (state) SpecPlayer.enable(player);
			else SpecPlayer.disable(player);
			
			if (Bukkit.getPlayer(sender.getName()).getUniqueId() != player) sender.sendMessage(Main.PREFIX + "The spec mode of player '§6" + PlayerUtils.getName(PlayerUtils.getId(player)) + "§7' is now §a" + args[0].toLowerCase() + "§7.");
			
			if (Bukkit.getPlayer(player) != null) Bukkit.getPlayer(player).sendMessage(Main.PREFIX + "Your spec mode is now §a" + args[0].toLowerCase() + "§7.");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("info")) {
			
			Boolean state = true; //Info state
			UUID player = Bukkit.getPlayer(sender.getName()).getUniqueId();
			
			if (args.length > 1 && sender.hasPermission("uhc.spectate.all")) {
				
				player = PlayerUtils.getUUID(args[1]);
				
				if (player == null) {
					
					sender.sendMessage(Main.PREFIX + "This player is not know on this server.");
					return true;
					
				}
				
			}
			
			if (!GameUtils.getSpectate(player)) {
				
				sender.sendMessage("The spec mode of this player is not enabled");
				return true;
				
			}

			//if (state) Enable info
			//else Disable info
			
			if (Bukkit.getPlayer(sender.getName()).getUniqueId() != player) sender.sendMessage(Main.PREFIX + "The spec info of player '§6" + PlayerUtils.getName(PlayerUtils.getId(player)) + "§7' is now §a" + args[0].toLowerCase() + "§7.");
			
			if (Bukkit.getPlayer(player) != null) Bukkit.getPlayer(player).sendMessage(Main.PREFIX + "Your spec info is now §a" + (state ? "on" : "off") + "§7.");
			return true;
			
		}
			
		if (args[0].equalsIgnoreCase("list")) {
			
			sender.sendMessage(Main.PREFIX + "List of spectators:");
			
			int i = 0;
			
			for (UUID player : GameUtils.getPlayers().keySet()) {
				
				if (!GameUtils.getSpectate(player)) continue;
				
				sender.sendMessage("§8⫸ §7'" + (Bukkit.getPlayer(player) == null ? "§c" : "§a") + "" + PlayerUtils.getName(PlayerUtils.getId(player)) + "§7'");
				i++;
				
			}
			
			if (i < 1) {
				
				sender.sendMessage(Main.PREFIX + "There are no spectators.");
				return true;
				
			}
			
			sender.sendMessage(Main.PREFIX + "§6" + i + "§7 spectators listed.");
			return true;
			
		}
		
		sender.sendMessage(Main.PREFIX + "Usage of /" + label + ":");
		sender.sendMessage("§8⫸ §6/" + label + " on|off" + (sender.hasPermission("uhc.spectate.all") ? " <player>" : "") + "§8- §7Enable the spec mode.");
		sender.sendMessage("§8⫸ §6/" + label + " info" + (sender.hasPermission("uhc.spectate.all") ? " <player>" : "") + "§8- §7Toggle the spec info.");
		sender.sendMessage("§8⫸ §6/" + label + " list §8- §7List whitelisted players.");		
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.spectate")) return null;
    		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			complete.add("on");
			complete.add("off");
			complete.add("info");
			complete.add("list");
			
		} else if (args.length == 2 && sender.hasPermission("uhc.spectate.all")) {
			
			if (args[0].equalsIgnoreCase("on")) {
				
				for (UUID player : GameUtils.getPlayers().keySet()) {
					
					if (GameUtils.getSpectate(player)) continue;
					
					complete.add(PlayerUtils.getName(PlayerUtils.getId(player)));
					
				}
				
			}
			
			if (args[0].equalsIgnoreCase("off")) {
				
				for (UUID player : GameUtils.getPlayers().keySet()) {
					
					if (!GameUtils.getSpectate(player)) continue;
					
					complete.add(PlayerUtils.getName(PlayerUtils.getId(player)));
					
				}
				
			}
			
			if (args[0].equalsIgnoreCase("info")) {
				
				for (UUID player : GameUtils.getPlayers().keySet()) {
					
					if (!GameUtils.getSpectate(player)) continue;
					
					complete.add(PlayerUtils.getName(PlayerUtils.getId(player)));
					
				}
				
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


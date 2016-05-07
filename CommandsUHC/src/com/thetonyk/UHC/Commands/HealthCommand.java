package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;

public class HealthCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("uhc.health")) {
			
			sender.sendMessage(Main.NO_PERMS);
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
		
		double health = (player.getHealth() / 2) * 10;
		double maxHealth = (player.getMaxHealth() / 2) * 10;
		double absorptionHealth = (((CraftPlayer) player).getHandle().getAbsorptionHearts() / 2) * 10;
		
		sender.sendMessage(Main.PREFIX + "§6" + player.getName() + "§7: §a" + (int) health + "% §8(§7Max: §a" + (int) maxHealth + "%" + (absorptionHealth > 0 ? " §8| §7Absorption: §a" + (int) absorptionHealth + "%" : "") + "§8)");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.health")) return null;
		
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

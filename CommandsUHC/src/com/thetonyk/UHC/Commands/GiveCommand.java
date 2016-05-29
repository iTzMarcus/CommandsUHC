package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;

import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;

public class GiveCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("uhc.give")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		if (args.length < 2) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /give <player|*> <item> [number] [damage]");
			return true;
			
		}
		
		int number = 1;
		
		if (args.length > 2) {
			
			try {
				
				number = Integer.parseInt(args[2]);
				
			} catch (Exception exception) {
				
				sender.sendMessage(Main.PREFIX + "This is not a valid number.");
				return true;
				
			}
			
			if (number < 0 || number > 3200) {
				
				sender.sendMessage(Main.PREFIX + "This is not a valid number.");
				return true;
				
			}
			
		}
		
		short damage = 0;
		
		if (args.length > 3) {
			
			try {
				
				damage = Short.parseShort(args[3]);
				
			} catch (Exception exception) {
				
				sender.sendMessage(Main.PREFIX + "This is not a valid damage.");
				return true;
				
			}
			
			if (damage < 0 || damage > 100) {
				
				sender.sendMessage(Main.PREFIX + "This is not a valid damage.");
				return true;
				
			}
			
		}
		
		MinecraftKey key = new MinecraftKey(args[1]);
		ItemStack item = new ItemStack(CraftItemStack.asNewCraftStack(Item.REGISTRY.get(key)).getType(), number, damage);
		
		if (item.getType() == Material.AIR) {
			
			sender.sendMessage(Main.PREFIX + "This is not a valid item id.");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("*")) {
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				if (GameUtils.getSpectate(player.getUniqueId())) continue;
				
				Map<Integer, ItemStack> items = player.getInventory().addItem(item);
				
				if (items.isEmpty()) continue;
					
				for (ItemStack drop : items.values()) {
					
					player.getWorld().dropItem(player.getLocation(), drop);
					
				}
				
			}
			
			Bukkit.broadcastMessage(Main.PREFIX + "All players receive §6" + number + " " + args[1].toLowerCase().replaceAll("_", " ") + "§7.");
			return true;
			
		}
		
		Player player = Bukkit.getPlayer(args[0]);
			
		if (player == null) {
			
			sender.sendMessage(Main.PREFIX + "The player '§6" + args[0] + "§7' is not online.");
			return true;
			
		}
		
		Map<Integer, ItemStack> items = player.getInventory().addItem(item);
		
		if (!items.isEmpty()) {
			
			for (ItemStack drop : items.values()) {
				
				player.getWorld().dropItem(player.getLocation(), drop);
				
			}
			
		}
		
		if (sender.getName() != player.getName()) sender.sendMessage(Main.PREFIX + "'§6" + player.getName() + "§7' receive §6" + number + " " + args[1].toLowerCase().replaceAll("_", " ") + "§7.");
		
		player.sendMessage(Main.PREFIX + "You receive §6" + number + " " + args[1].toLowerCase().replaceAll("_", " ") + "§7.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.give")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (Player player : Bukkit.getOnlinePlayers()) {
				
				complete.add(player.getName());
				
			}
			
			complete.add("*");
			
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

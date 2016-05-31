package com.thetonyk.UHC.Commands;

import static net.md_5.bungee.api.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.thetonyk.UHC.Main;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class TextCommand implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.text")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		if (args.length > 0) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /text <remove|text>");
			return true;
			
		}
		
		StringBuilder text = new StringBuilder();
		
		for (int i = 1; i < args.length; i++) {
			
			text.append(args[i] + " ");
			
		}
		
		if (args[0].equalsIgnoreCase("remove")) {
			
			List<ArmorStand> around = new ArrayList<ArmorStand>();
			
			for (Entity entity : Bukkit.getPlayer(sender.getName()).getNearbyEntities(1, 1, 1)) {
				
				if (!(entity instanceof ArmorStand)) continue;
				
				ArmorStand stand = (ArmorStand) entity;
				
				if (stand.isVisible() || !stand.isCustomNameVisible() || stand.getCustomName() == null || stand.getCustomName().length() < 1) continue;
				
				around.add(stand);
				
			}
			
			if (args.length > 1) {
				
				
				
				for (ArmorStand stand : around) {
					
					if (!text.toString().equalsIgnoreCase(stand.getCustomName().replaceAll("§", "&"))) continue;
						
					sender.sendMessage(Main.PREFIX + "The text '§r" + stand.getCustomName() + "§7' has been deleted.");
					stand.remove();
					return true;
					
				}
				
				sender.sendMessage(Main.PREFIX + "There are no text with this name around you.");
				return true;
				
			}
			
			if (around.size() < 2) {
				
				ArmorStand stand = around.iterator().next();
				
				sender.sendMessage(Main.PREFIX + "The text '§r" + stand.getCustomName() + "§7' has been deleted.");
				stand.remove();
				return true;
				
			}
			
			sender.sendMessage(Main.PREFIX + "There are §a" + around.size() + "§7 texts around you.");
			sender.sendMessage(Main.PREFIX + "Choose the text to remove: §8(§7Click on it§8)");
			
			for (ArmorStand stand : around) {
				
				ComponentBuilder message = new ComponentBuilder("§8⫸ ");
				message.append(stand.getCustomName());
				message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " " + stand.getCustomName().replaceAll("§", "&")));
				message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
				Bukkit.getPlayer(sender.getName()).spigot().sendMessage(message.create());
				
			}
			
			return true;
			
		}
		
		Location location = Bukkit.getPlayer(sender.getName()).getLocation();
		location.setY(location.getY() - 1.25);
		
		ArmorStand stand = (ArmorStand) Bukkit.getPlayer(sender.getName()).getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		
		stand.setVisible(false);
		stand.setSmall(true);
		stand.setGravity(false);	
		stand.setBasePlate(false);
		stand.setArms(false);
		stand.setCustomName(text.toString());
		stand.setCustomNameVisible(true);
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.text")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			complete.add("remove");
			
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

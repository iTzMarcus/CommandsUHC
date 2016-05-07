package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.TeamsUtils;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

public class AcceptCommand implements CommandExecutor, TabCompleter {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.team")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length > 0) {
			
			if (TeamsUtils.getTeam(sender.getName()) != null) {
				
				ComponentBuilder message = Main.getPrefixComponent().append("You are already in a team, ").color(GRAY).append("leave it first").color(AQUA).italic(true);
				message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click on this text to leave your team.").color(GRAY).create()));
				message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team leave"));
				message.append(".").retain(FormatRetention.NONE).color(GRAY);
		        Bukkit.getPlayer(sender.getName()).spigot().sendMessage(message.create());
				return true;
				
			}
			
			if (!TeamsUtils.invitations.containsKey(Bukkit.getPlayer(args[0]).getName())) {
				
				sender.sendMessage(Main.PREFIX + "This player has not invited you.");
				return true;
				
			}
			
			if (!TeamsUtils.invitations.get(Bukkit.getPlayer(args[0]).getName()).contains(sender.getName())) {
				
				sender.sendMessage(Main.PREFIX + "This player has not invited you.");
				return true;
				
			}
			
			if (TeamsUtils.getTeam(args[0]) == null) {
				
				sender.sendMessage(Main.PREFIX + "This invitation was canceled.");
				return true;
				
			}
			
			sender.sendMessage(Main.PREFIX + "You joined the team of '§6" + Bukkit.getPlayer(args[0]).getName() + "§7'.");
			TeamsUtils.sendMessage(TeamsUtils.getTeam(args[0]), Main.PREFIX + "The player '§6" + sender.getName() + "§7' joined your team.");
			
			if (!TeamsUtils.invitations.containsKey(sender.getName())) TeamsUtils.invitations.put(sender.getName(), new ArrayList<String>());
			TeamsUtils.joinTeam(sender.getName(), TeamsUtils.getTeam(args[0]));
			TeamsUtils.invitations.get(args[0]).remove(sender.getName());
			return true;
			
		}	
			
		sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <player>");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.team")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (String player : TeamsUtils.invitations.keySet()) {
				
				if (TeamsUtils.invitations.get(player).contains(sender.getName())) complete.add(player);
				
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

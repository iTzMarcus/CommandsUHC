package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

import static net.md_5.bungee.api.ChatColor.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

public class InviteCommand implements CommandExecutor, TabCompleter {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.team")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length > 0) {
			
			if (GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY || GameUtils.getStatus() == Status.END) {
				
				sender.sendMessage(Main.PREFIX + "The game has already started.");
				return true;
				
			}
				
			if (Bukkit.getPlayer(args[0]) == null) {
				
				sender.sendMessage(Main.PREFIX + "The player '§6" + args[0] + "§7' is not online.");
				return true;
				
			}
			
			if (!Bukkit.getPlayer(args[0]).isOnline()) {
				
				sender.sendMessage(Main.PREFIX + "The player '§6" + Bukkit.getPlayer(args[0]) + "§7' is not online.");
				return true;
				
			}
			
			if (sender.getName().equalsIgnoreCase(Bukkit.getPlayer(args[0]).getName())) {
				
				sender.sendMessage(Main.PREFIX + "You can't invite yourslef.");
				return true;
				
			}
			
			if (TeamsUtils.getTeam(Bukkit.getPlayer(sender.getName()).getUniqueId()) == null) {
				
				if (TeamsUtils.getTeamsLeft() < 1) {
					
					sender.sendMessage(Main.PREFIX + "There are no more available teams.");
					return true;
					
				}
				
				TeamsUtils.createTeam(Bukkit.getPlayer(sender.getName()).getUniqueId());
				
			}
			
			if (!TeamsUtils.invitations.containsKey(Bukkit.getPlayer(sender.getName()).getUniqueId())) TeamsUtils.invitations.put(Bukkit.getPlayer(sender.getName()).getUniqueId(), new ArrayList<UUID>());
			
			TeamsUtils.invitations.get(Bukkit.getPlayer(sender.getName()).getUniqueId()).add(PlayerUtils.getUUID(args[0]));
			
			Bukkit.getPlayer(args[0]).sendMessage(Main.PREFIX + "You have received an invitation from '§6" + sender.getName() + "§7'.");
			
			ComponentBuilder message = Main.getPrefixComponent().append("To join his team, ").color(GRAY).append("accept the invitation").color(AQUA).italic(true);
			message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept the invitation of ").color(GRAY).append(sender.getName()).color(GREEN).append(".").color(GRAY).create()));
			message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team accept " + sender.getName()));
			message.append(".").retain(FormatRetention.NONE).color(GRAY);
			Bukkit.getPlayer(args[0]).spigot().sendMessage(message.create());
	        
	        sender.sendMessage(Main.PREFIX + "Invitation send to player '§6" + Bukkit.getPlayer(args[0]).getName() + "§7'.");
	        return true;
			
		}	
			
		sender.sendMessage(Main.PREFIX + "Usage: /invite <player>");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.team")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (Player player : Bukkit.getOnlinePlayers()) {
				
				if (player.getName().equalsIgnoreCase(sender.getName())) continue;
				
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

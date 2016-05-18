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
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.PlayerUtils;

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
			
			if (GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY || GameUtils.getStatus() == Status.END) {
				
				sender.sendMessage(Main.PREFIX + "The game has already started.");
				return true;
				
			}
			
			if (TeamsUtils.getTeam(Bukkit.getPlayer(sender.getName()).getUniqueId()) != null) {
				
				ComponentBuilder message = Main.getPrefixComponent().append("You are already in a team, ").color(GRAY).append("leave it first").color(AQUA).italic(true);
				message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click on this text to leave your team.").color(GRAY).create()));
				message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team leave"));
				message.append(".").retain(FormatRetention.NONE).color(GRAY);
		        Bukkit.getPlayer(sender.getName()).spigot().sendMessage(message.create());
				return true;
				
			}
			
			if (!TeamsUtils.invitations.containsKey(PlayerUtils.getUUID(args[0]))) {
				
				sender.sendMessage(Main.PREFIX + "This player has not invited you.");
				return true;
				
			}
			
			if (!TeamsUtils.invitations.get(PlayerUtils.getUUID(args[0])).contains(sender.getName())) {
				
				sender.sendMessage(Main.PREFIX + "This player has not invited you.");
				return true;
				
			}
			
			if (TeamsUtils.getTeam(PlayerUtils.getUUID(args[0])) == null) {
				
				sender.sendMessage(Main.PREFIX + "This invitation was canceled.");
				return true;
				
			}
			
			sender.sendMessage(Main.PREFIX + "You joined the team of '§6" + Bukkit.getPlayer(args[0]).getName() + "§7'.");
			TeamsUtils.sendMessage(TeamsUtils.getTeam(PlayerUtils.getUUID(args[0])), Main.PREFIX + "The player '§6" + sender.getName() + "§7' joined your team.");
			
			if (!TeamsUtils.invitations.containsKey(Bukkit.getPlayer(sender.getName()).getUniqueId())) TeamsUtils.invitations.put(Bukkit.getPlayer(sender.getName()).getUniqueId(), new ArrayList<UUID>());
			TeamsUtils.joinTeam(Bukkit.getPlayer(sender.getName()).getUniqueId(), TeamsUtils.getTeam(PlayerUtils.getUUID(args[0])));
			TeamsUtils.invitations.get(PlayerUtils.getUUID(args[0])).remove(Bukkit.getPlayer(sender.getName()).getUniqueId());
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

			for (UUID player : TeamsUtils.invitations.keySet()) {
				
				if (TeamsUtils.invitations.get(player).contains(Bukkit.getPlayer(sender.getName()).getUniqueId())) complete.add(PlayerUtils.getName(PlayerUtils.getId(player)));
				
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

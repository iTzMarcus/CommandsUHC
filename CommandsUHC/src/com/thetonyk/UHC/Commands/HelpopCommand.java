package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

import static net.md_5.bungee.api.ChatColor.*;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

public class HelpopCommand implements CommandExecutor {
	
	private static List<String> muteHelpop = new ArrayList<String>();
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.helpop")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <message>");
			return true;
		}
		
		if (muteHelpop.contains(sender.getName())) {
			
			sender.sendMessage(Main.PREFIX + "You have already sent a helpop in the last 30 seconds.");
			return true;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		ComponentBuilder text = new ComponentBuilder("Help ").color(GREEN).bold(true).append("⫸ ").color(DARK_GRAY).bold(false).append(message.toString()).color(GRAY);
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to send private message to ").color(GRAY).append(sender.getName()).color(GREEN).append(".").color(GRAY).create()));
		text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));
		BaseComponent[] createdText = text.create();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (player.hasPermission("uhc.helpop.see") && !sender.getName().equalsIgnoreCase(player.getName())) {
				
				player.sendMessage("§a§lHelp §8⫸ §7From: '§6" + sender.getName() + "§7'");
				player.spigot().sendMessage(createdText);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
				
			}
			
		}
		
		ComponentBuilder confirm = Main.getPrefixComponent().append("Your message has been sent. Use ").color(GRAY).append("/rules").color(GOLD);
		confirm.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/rules"));
		confirm.append(" to see the rules.").retain(FormatRetention.NONE).color(GRAY);
		Bukkit.getPlayer(sender.getName()).spigot().sendMessage(confirm.create());
		
		muteHelpop.add(sender.getName());
		
		new BukkitRunnable() {
			
			public void run() {
				
				muteHelpop.remove(sender.getName());
				
			}
			
		}.runTaskLater(Main.uhc, 600);
		
		return true;
		
	}

}


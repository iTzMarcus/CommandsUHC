package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Features.DisplayTimers;

public class TimeleftCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
		if (!sender.hasPermission("uhc.timeleft")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (DisplayTimers.timer == null) {
			
			sender.sendMessage(Main.PREFIX + "The game has not started.");
			return true;
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "Times left:");
		Bukkit.broadcastMessage("§8⫸ §7Final Heal: §a" + DisplayTimers.getFormatedTime(60 - DisplayTimers.time) + "§7.");
		Bukkit.broadcastMessage("§8⫸ §7PVP: §a" + DisplayTimers.getFormatedTime(DisplayTimers.getTimeLeftPVP()) + "§7.");
		Bukkit.broadcastMessage("§8⫸ §7Meetup: §a" + DisplayTimers.getFormatedTime(DisplayTimers.getTimeLeftMeetup()) + "§7.");
		return true;
		
	}

}

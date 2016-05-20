package com.thetonyk.UHC.Commands;

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
		
		sender.sendMessage(Main.PREFIX + "Times left:");
		sender.sendMessage("§8⫸ §7Final Heal: §a" + (60 - DisplayTimers.time > 0 ? DisplayTimers.getFormatedTime(60 - DisplayTimers.time) : "Already given") + "§7.");
		sender.sendMessage("§8⫸ §7PVP: §a" + (DisplayTimers.getTimeLeftPVP() > 0 ? DisplayTimers.getFormatedTime(DisplayTimers.getTimeLeftPVP()) : "ON") + "§7.");
		sender.sendMessage("§8⫸ §7Meetup: §a" + (DisplayTimers.getTimeLeftMeetup() > 0 ? DisplayTimers.getFormatedTime(DisplayTimers.getTimeLeftMeetup()) : "Now") + "§7.");
		return true;
		
	}

}

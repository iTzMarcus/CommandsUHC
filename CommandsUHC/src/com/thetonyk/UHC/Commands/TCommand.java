package com.thetonyk.UHC.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class TCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.team")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (TeamsUtils.getTeam(sender.getName()) == null) {
			
			sender.sendMessage(Main.PREFIX + "You are not in a team.");
			return true;
			
		}
		
		if (args.length == 0) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <message>");
			return true;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		TeamsUtils.sendMessage(TeamsUtils.getTeam(sender.getName()), "§6Team §8| §7" + sender.getName() + " §8⫸ §f" + message);
		return true;
		
	}

}


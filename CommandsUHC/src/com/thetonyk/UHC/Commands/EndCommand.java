package com.thetonyk.UHC.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class EndCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.end")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		Status status = GameUtils.getStatus();
		
		switch (status) {
		
			case NONE:
			case OPEN:
				sender.sendMessage(Main.PREFIX + "The game is not ready.");
				return true;
			case READY:
			case TELEPORT:
				sender.sendMessage(Main.PREFIX + "The game has not started.");
				return true;
			case END:
				sender.sendMessage(Main.PREFIX + "Game is already ended.");
				return true;
			default:
				break;
	
		}
		
		
		return true;
		
	}

}

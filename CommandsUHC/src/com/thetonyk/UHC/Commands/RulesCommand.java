package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Inventories.RulesInventory;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class RulesCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (GameUtils.getStatus() == Status.NONE) {
			
			sender.sendMessage(Main.PREFIX + "The game is not ready.");
			return true;
			
		}
		
		Bukkit.getPlayer(sender.getName()).openInventory(RulesInventory.getRules());
		return true;
		
	}
	
}

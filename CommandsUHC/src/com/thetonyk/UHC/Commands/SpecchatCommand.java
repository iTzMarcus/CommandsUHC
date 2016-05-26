package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;

public class SpecchatCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = Bukkit.getPlayer(sender.getName());
		
		if (!GameUtils.getSpectate(player.getUniqueId())) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <message>");
			return true;
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		String chat = "§6SpecChat §8| §7§o" + player.getName() + " §8⫸ §f" + message;
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			if (!GameUtils.getSpectate(online.getUniqueId())) continue;
			
			online.sendMessage(chat);
			
		}
		return true;
		
	}

}


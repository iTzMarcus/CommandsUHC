package com.thetonyk.UHC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.PlayerUtils;

public class NosoundCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		PlayerUtils.setNosoundState(Bukkit.getPlayer(sender.getName()));
		
		new BukkitRunnable() {
		
			public void run() {
			
				sender.sendMessage(Main.PREFIX + "All sounds are now " + (PlayerUtils.getNosoundState(Bukkit.getPlayer(sender.getName())) == 0 ? "enabled" : "disabled") + ".");
		
			}
				
		}.runTaskLater(Main.uhc, 2);
		
		return true;
		
	}
	
}

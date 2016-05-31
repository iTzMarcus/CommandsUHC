package com.thetonyk.UHC.Commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class ListCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.list")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		List<UUID> alives = GameUtils.getAlives();
		
		if (alives.size() < 1) {
			
			sender.sendMessage(Main.PREFIX + "There are no alives players.");
			return true;
			
		}
		
		for (UUID player : alives) {
			
			Player online = Bukkit.getPlayer(player);
			
			sender.sendMessage("§8⫸ " + PlayerUtils.getRank(player).getPrefix() + (TeamsUtils.getTeam(player) == null ? "§7" : TeamsUtils.getTeamPrefix(player)) + PlayerUtils.getName(PlayerUtils.getId(player)) + (online == null ? "" : " §8- §7" + ((online.getHealth() / 2) * 10) + "§4♥"));
			
		}
		
		sender.sendMessage(Main.PREFIX + "§6" + alives.size() + "§7 alives players listed.");
		return true;
		
	}
	
}

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

public class NearCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = Bukkit.getPlayer(sender.getName());
		
		if (!GameUtils.getSpectate(player.getUniqueId())) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		int distance = 150;
		
		if (args.length > 0) {
			
			try {
				
				distance = Integer.parseInt(args[0]);
				
			} catch (Exception exception) {
				
				sender.sendMessage(Main.PREFIX + "This is not a valid distance.");
				return true;
				
			}
			
		}
		
		if (distance < 1 || distance > 10000) {
			
			sender.sendMessage(Main.PREFIX + "This is not a valid distance.");
			return true;
			
		}
		
		sender.sendMessage(Main.PREFIX + "List of nearby players: §8(§a" + distance + " §7blocks§8)");

		List<UUID> alives = GameUtils.getAlives();
		int i = 0;
		
		for (Player online : player.getWorld().getPlayers()) {
			
			if (player.equals(online)) continue;
			
			if (!alives.contains(online.getUniqueId())) continue;
			
			int nearDistance = (int) player.getLocation().distance(online.getLocation());
			
			if (nearDistance > distance) continue;
			
			UUID uuid = online.getUniqueId();
			
			sender.sendMessage("§8⫸ §7" + PlayerUtils.getRank(uuid).getPrefix() + ((TeamsUtils.getTeam(uuid) != null) ? TeamsUtils.getTeamPrefix(uuid) : "§7") + online.getName() + " §8(§a" + nearDistance + " §7blocks§8)");
			i++;
			
		}
		
		if (i < 1) sender.sendMessage(Main.PREFIX + "There are no players nearby.");
		else sender.sendMessage(Main.PREFIX + "§6" + i + "§7 nearby players listed.");
		
		return true;
		
	}
	
}

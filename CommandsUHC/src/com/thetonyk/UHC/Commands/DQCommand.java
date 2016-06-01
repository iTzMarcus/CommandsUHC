package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Features.LoginPlayer;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.PlayerUtils.Rank;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class DQCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.dq")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 2) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <player> <reason>");
			return true;
		}
		
		Status status = GameUtils.getStatus();
		
		switch (status) {
		
			case NONE:
			case READY:
			case TELEPORT:
				sender.sendMessage(Main.PREFIX + "The game has not started.");
				return true;
			case END:
				sender.sendMessage(Main.PREFIX + "The game is over");
				return true;
			default:
				break;
			
		}
		
		List<UUID> alives = GameUtils.getAlives();
		UUID uuid = PlayerUtils.getUUID(args[0]);
		
		if (uuid == null || !alives.contains(uuid)) {
			
			sender.sendMessage(Main.PREFIX + "This player is not in the game.");
			return true;
			
		}
		
		Rank rank = PlayerUtils.getRank(uuid);
		
		if (rank != Rank.PLAYER && rank != Rank.FRIEND && rank != Rank.WINNER && rank != Rank.FAMOUS && PlayerUtils.getRank(Bukkit.getPlayer(sender.getName()).getUniqueId()) != Rank.ADMIN) {
			
			sender.sendMessage(Main.PREFIX + "The staff members can only be disqualifed by an Admin.");
			return true;
			
		}
		
		StringBuilder text = new StringBuilder();
		
		for (int i = 1; i < args.length; i++) {
			
			text.append(args[i]);
			
			if (args.length > i + 1) text.append(" ");
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "'§6" + PlayerUtils.getName(PlayerUtils.getId(uuid)) + "§7' was disqualified for '§6" + text.toString() + "§7'.");
		
		GameUtils.setDeath(uuid, true);
		Bukkit.getOfflinePlayer(uuid).setWhitelisted(false);
		Bukkit.broadcastMessage(Main.PREFIX + "There are §a" + GameUtils.getAlives().size() + " §7players alive.");
		
		Player player = Bukkit.getPlayer(uuid);
		
		if (player == null) return true;
	
		LoginPlayer.updateVisibility();
		player.setHealth(0);
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.dq")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (UUID player : GameUtils.getAlives()) {
				
				complete.add(PlayerUtils.getName(PlayerUtils.getId(player)));
				
			}
			
		}
		
		List<String> tabCompletions = new ArrayList<String>();
		
		if (args[args.length - 1].isEmpty()) {
			
			for (String type : complete) {
				
				tabCompletions.add(type);
				
			}
			
		} else {
			
			for (String type : complete) {
				
				if (type.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) tabCompletions.add(type);
				
			}
			
		}
		
		return tabCompletions;
		
	}
	
}

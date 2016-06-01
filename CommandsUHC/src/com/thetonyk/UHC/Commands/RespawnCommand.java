package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeleportUtils;

public class RespawnCommand implements CommandExecutor, TabCompleter { 
	
	// Very similar to /teleport command.
	// Todo: Save death location and stuff.
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.respawn")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /" + label + " <player>");
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
	
		Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null) {
			
			sender.sendMessage(Main.PREFIX + "The player '§6" + args[0] + "§7' is not online.");
			return true;
			
		}
		
		if (!GameUtils.getDeath(player.getUniqueId())) {
			
			sender.sendMessage(Main.PREFIX + "This player is not death.");
			return true;
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "Teleporting '§6" + player.getName() + "§7'...");
		
		player.setWhitelisted(true);
		GameUtils.addPlayer(player.getUniqueId());
		
		List<Map.Entry<String, ?>> uuids = new ArrayList<Map.Entry<String, ?>>();
		
		Map.Entry<String, UUID> uuid = new Map.Entry<String, UUID>() {
			
			UUID uuid = player.getUniqueId();

			@Override
			public String getKey() {
				
				return "uuid";
				
			}

			@Override
			public UUID getValue() {
				
				return this.uuid;
				
			}

			@Override
			public UUID setValue(UUID uuid) {
			
				this.uuid = uuid;
				return this.uuid;
				
			}
		
		};
				
		uuids.add(uuid);
		TeleportUtils.loadSpawnsAndTeleport(uuids);
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.respawn")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		if (args.length == 1) {

			for (UUID player : GameUtils.getPlayers().keySet()) {
				
				if (!GameUtils.getDeath(player)) continue;
				
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

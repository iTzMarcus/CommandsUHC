package com.thetonyk.UHC.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.TeleportEvent;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.TeleportUtils;

public class TeleportCommand implements CommandExecutor, TabCompleter, Listener {
	
	private Boolean teleport = false;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.teleport")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (GameUtils.getWorld() == null) {
			
			sender.sendMessage(Main.PREFIX + "You need to setup the game first.");
			return true;
			
		}
		
		if (GameUtils.getStatus() == Status.NONE) {
			
			sender.sendMessage(Main.PREFIX + "The game is not ready.");
			return true;
			
		}
		
		if (teleport) {
			
			sender.sendMessage(Main.PREFIX + "You have already started the teleportation.");
			return true;
			
		}
		
		if (args.length > 0) {
			
			List<UUID> players = new ArrayList<UUID>();
			String playersMessage = null;
			
			for (int i = 0; i < args.length; i++) {
				
				if (Bukkit.getPlayer(args[i]) == null) {
					
					sender.sendMessage(Main.PREFIX + "The player '§6" + args[0] + "§7' is not online.");
					return true;
					
				}
				
				if (i == 0) {
					
					playersMessage = "'§6" + Bukkit.getPlayer(args[i]).getName() + "§7'";
					continue;
					
				}
				
				if (i == (args.length - 1)) {
					
					playersMessage += " and '§6" + Bukkit.getPlayer(args[i]).getName() + "§7'";
					continue;
					
				}
				
				playersMessage += ", '§6" + Bukkit.getPlayer(args[i]).getName() + "§7'";
				
			}
			
			Bukkit.broadcastMessage(Main.PREFIX + "Teleporting " + playersMessage + "...");
			
			List<Map.Entry<String, ?>> uuids = new ArrayList<Map.Entry<String, ?>>();
			
			for (UUID player : players) {
				
				Bukkit.getPlayer(player).setWhitelisted(true);
				if (GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY) GameUtils.addPlayer(Bukkit.getPlayer(player).getUniqueId());
			
				Map.Entry<String, UUID> uuid = new Map.Entry<String, UUID>() {
				
					UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
	
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
				
			}
			
			TeleportUtils.loadSpawnsAndTeleport(uuids);
			return true;
			
		}
		
		if (GameUtils.getTeleported()) {
			
			sender.sendMessage(Main.PREFIX + "You have already teleported players.");
			return true;
			
		}
		
		GameUtils.setStatus(Status.TELEPORT);
		Bukkit.setWhitelist(true);
		teleport = true;
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			if (online.isWhitelisted()) continue;
			
			online.setWhitelisted(true);
			
		}
		
		GameUtils.setupPlayers();
		
		int teams = 0;
		int solo = 0;
		List<Map.Entry<String, ?>> teleport = new ArrayList<Map.Entry<String, ?>>();
		
		for (OfflinePlayer whitelisted : Bukkit.getWhitelistedPlayers()) {
			
			List<String> teamsDone = new ArrayList<String>();
			
			for (Map.Entry<String, ?> entry : teleport) {
				
				if (!entry.getKey().equalsIgnoreCase("team")) continue;
				
				teamsDone.add((String) entry.getValue());
				
			}
			
			if (TeamsUtils.getTeam(whitelisted.getName()) != null) {
				
				if (teamsDone.contains(TeamsUtils.getTeam(whitelisted.getName()))) continue;
				
				Map.Entry<String, String> team = new Map.Entry<String, String>() {
					
					String team = TeamsUtils.getTeam(whitelisted.getName());

					@Override
					public String getKey() {
						
						return "team";
						
					}

					@Override
					public String getValue() {
						
						return this.team;
						
					}

					@Override
					public String setValue(String team) {
					
						this.team = team;
						return this.team;
						
					}
					
				};
				
				teleport.add(team);
				teams++;
				continue;
				
			}
			
			Map.Entry<String, UUID> uuid = new Map.Entry<String, UUID>() {
				
				UUID uuid = whitelisted.getUniqueId();

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
			
			teleport.add(uuid);
			solo++;
			
		}

		new BukkitRunnable() {
		
			public void run() {
			
				TeleportUtils.loadSpawnsAndTeleport(teleport);
			
			}
		
		}.runTaskLater(Main.uhc, 10);
		
		Bukkit.getWorld(GameUtils.getWorld()).setStorm(false);
		Bukkit.getWorld(GameUtils.getWorld()).setThundering(false);
		Bukkit.getWorld(GameUtils.getWorld()).setTime(0);
		
		Bukkit.broadcastMessage(Main.PREFIX + "Started to teleport §a" + teams + "§7 teams and §a" + solo + "§7 solos...");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (PlayerUtils.getNosoundState(player) == 1) continue;
			
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
			
		}
			
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("uhc.teleport")) return null;
		
		List<String> complete = new ArrayList<String>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			complete.add(player.getName());
			
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
	
	@EventHandler
	public void onTeleportFinish (TeleportEvent event) {
		
		GameUtils.setTeleported(true);
		teleport = false;
		
		Bukkit.broadcastMessage(Main.PREFIX + "All players have been teleported.");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (PlayerUtils.getNosoundState(player) == 1) continue;
			
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
			
		}
		
	}

}

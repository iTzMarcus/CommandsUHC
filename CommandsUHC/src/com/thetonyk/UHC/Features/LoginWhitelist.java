package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class LoginWhitelist implements Listener {

	@EventHandler
	public void onConnect(PlayerLoginEvent event) {
		
		Status status = GameUtils.getStatus();
		Player player = event.getPlayer();
		Result result = event.getResult();
		
		if (GameUtils.reset) {
			
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage(Main.PREFIX + "Server is currently resetting the game");
			return;
			
		}
		
		PermissionsUtils.setPermissions(player);
		PermissionsUtils.updateBungeePermissions(player);
		
		if (player.isOp() || player.hasPermission("global.bypasswhitelist") || GameUtils.getSpectate(player.getUniqueId())) {
			
			event.setResult(Result.ALLOWED);
			event.allow();
			return;
			
		}
		
		if (result == Result.KICK_WHITELIST) {
				
			if (status == Status.TELEPORT || status == Status.PLAY || status == Status.END) {
				
				event.setKickMessage(Main.PREFIX + "You are not whitelisted\n" + Main.PREFIX + "§cThe UHC has already begun.");
				return;
				
			}
			
			event.setKickMessage(Main.PREFIX + "You are not whitelisted\n" + Main.PREFIX + "§cNo scheduled UHC.");
		
		}
		
		if (GameUtils.getPlayersCount() >= GameUtils.getSlots()) {
			
			event.disallow(Result.KICK_FULL, Main.PREFIX + "§cThe server is currently full.");
			return;
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onConnectMonitor(PlayerLoginEvent event) {
		
		Player player = event.getPlayer();
		Result result = event.getResult();
		
		if (result == Result.ALLOWED) return;
		
		PermissionsUtils.clearPermissions(player);
		
	}
	
}

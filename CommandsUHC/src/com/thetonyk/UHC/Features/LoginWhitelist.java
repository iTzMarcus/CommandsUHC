package com.thetonyk.UHC.Features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class LoginWhitelist implements Listener {

	@EventHandler
	public void onConnect(PlayerLoginEvent event) {
		
		PermissionsUtils.setPermissions(event.getPlayer());
		PermissionsUtils.updateBungeePermissions(event.getPlayer());
		
		if (event.getPlayer().isOp() || event.getPlayer().hasPermission("global.bypasswhitelist")) {
			
			event.allow();
			return;
			
		}
		
		if (event.getResult() == Result.KICK_WHITELIST) {
				
			if (GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY || GameUtils.getStatus() == Status.END) {
				
				event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷\n\n§cThe UHC has already begun.\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
				return;
				
			}
				
			event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷\n\n§cNo scheduled UHC.\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
		
		}
		
		if (GameUtils.getPlayersCount() >= GameUtils.getSlots()) {
			
			if (event.getPlayer().isOp() || event.getPlayer().hasPermission("global.bypasswhitelist")) {
				
				event.allow();
				return;
				
			}
			
			event.disallow(Result.KICK_FULL, "§8⫸ §7The server is currently full. §8⫷\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onConnectMonitor(PlayerLoginEvent event) {
		
		if (event.getResult() == Result.ALLOWED) return;
		
		PermissionsUtils.clearPermissions(event.getPlayer());
		
	}
	
}

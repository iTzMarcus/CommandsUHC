package com.thetonyk.UHC.Features;

import org.bukkit.entity.Player;
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
		
		Status status = GameUtils.getStatus();
		Player player = event.getPlayer();
		Result result = event.getResult();
		
		PermissionsUtils.setPermissions(player);
		PermissionsUtils.updateBungeePermissions(player);
		
		if (player.isOp() || player.hasPermission("global.bypasswhitelist")) {
			
			event.allow();
			return;
			
		}
		
		if (result == Result.KICK_WHITELIST) {
				
			if (status == Status.TELEPORT || status == Status.PLAY || status == Status.END) {
				
				event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷\n\n§cThe UHC has already begun.\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
				return;
				
			}
				
			event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷\n\n§cNo scheduled UHC.\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
		
		}
		
		if (GameUtils.getPlayersCount() >= GameUtils.getSlots()) {
			
			event.disallow(Result.KICK_FULL, "§8⫸ §7The server is currently full. §8⫷\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
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

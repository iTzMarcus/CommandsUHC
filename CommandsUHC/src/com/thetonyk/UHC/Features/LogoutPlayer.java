package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class LogoutPlayer implements Listener {
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {

		if (event.getPlayer().isInsideVehicle()) event.getPlayer().leaveVehicle();
		
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		
		PermissionsUtils.clearPermissions(event.getPlayer());
		
		event.setQuitMessage("§7[§c-§7] " + PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getPlayer().getUniqueId()) != null) ? TeamsUtils.getTeamPrefix(event.getPlayer().getUniqueId()) : "§7") + event.getPlayer().getName());
		
	}

}

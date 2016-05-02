package com.thetonyk.UHC.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class LeaveListener implements Listener{
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();

		event.setQuitMessage("§7[§c-§7] " + PlayerUtils.getRank(player.getName()).getPrefix() + ((TeamsUtils.getTeam(player.getName()) != null) ? TeamsUtils.getTeamPrefix(player.getName()) : "§7") + player.getName());
		
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		PlayerUtils.leaveUpdatePlayer(player);
		PermissionsUtils.clearPermissions(player);
		
		if (player.isInsideVehicle()) player.leaveVehicle();
		
	}

}

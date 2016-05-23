package com.thetonyk.UHC.Features;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;

public class LogoutPlayer implements Listener {
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {

		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (player.isInsideVehicle()) player.leaveVehicle();
		
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		player.setScoreboard(scoreboard);
		
		PermissionsUtils.clearPermissions(player);
		
		event.setQuitMessage("§7[§c-§7] " + PlayerUtils.getRank(uuid).getPrefix() + ((TeamsUtils.getTeam(uuid) != null) ? TeamsUtils.getTeamPrefix(uuid) : "§7") + player.getName());
		
	}

}

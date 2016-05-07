package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class LoginPlayer implements Listener {
	
	public LoginPlayer() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			PermissionsUtils.clearPermissions(player);
			PermissionsUtils.setPermissions(player);
			PermissionsUtils.updateBungeePermissions(player);
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {

		if (event.getPlayer().isDead()) event.getPlayer().spigot().respawn();
		
		event.getPlayer().setNoDamageTicks(0);
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		
		PermissionsUtils.setPermissions(event.getPlayer());
		PermissionsUtils.updateBungeePermissions(event.getPlayer());
		
		DisplayUtils.sendTitle(event.getPlayer(), "§aUHC by CommandsPVP", "§7UHC §aFFA §7⋯ Nether §aOFF §7⋯ CutClean §aON", 0, 40, 10);
		event.setJoinMessage("§7[§a+§7] " + PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getPlayer().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getPlayer().getName()) : "§7") + event.getPlayer().getName());
		
		if (GameUtils.getStatus() == Status.TELEPORT || GameUtils.getStatus() == Status.PLAY || GameUtils.getStatus() == Status.END) return;
			
		event.getPlayer().teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
		event.getPlayer().setGameMode(GameMode.ADVENTURE);
		PlayerUtils.clearInventory(event.getPlayer());
		PlayerUtils.clearXp(event.getPlayer());
		PlayerUtils.feed(event.getPlayer());
		PlayerUtils.heal(event.getPlayer());
		PlayerUtils.clearEffects(event.getPlayer());
		event.getPlayer().setMaxHealth(20.0);
		
	}

}

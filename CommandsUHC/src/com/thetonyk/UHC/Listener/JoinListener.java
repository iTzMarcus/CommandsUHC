package com.thetonyk.UHC.Listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.thetonyk.UHC.Game;
import com.thetonyk.UHC.Game.Status;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.ItemsUtils;
import com.thetonyk.UHC.Utils.PermissionsUtils;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onConnect(PlayerLoginEvent event) {
		
		PermissionsUtils.setPermissions(event.getPlayer());
		
		if (event.getResult() != Result.KICK_WHITELIST) return;
			
		if (event.getPlayer().isOp() || event.getPlayer().hasPermission("global.bypasswhitelist")) {
			
			event.allow();
			return;
			
		}
			
		if (Game.getStatus() == Status.TELEPORT || Game.getStatus() == Status.PLAY || Game.getStatus() == Status.END) {
			
			event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷\n\n§cThe UHC has already begun.\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
			return;
			
		}
			
		event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷\n\n§cNo scheduled UHC.\n\n§7The UHC Arena is available at: §acommandspvp.com §7!");
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();

		player.setNoDamageTicks(0);
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		player.getScoreboard().registerNewObjective("below", "dummy");
		player.getScoreboard().getObjective("below").setDisplaySlot(DisplaySlot.BELOW_NAME);
		player.getScoreboard().getObjective("below").setDisplayName("§4♥");
		player.getScoreboard().registerNewObjective("list", "dummy");
		player.getScoreboard().getObjective("list").setDisplaySlot(DisplaySlot.PLAYER_LIST);
		
		event.setJoinMessage("§7[§a+§7] " + PlayerUtils.getRank(player.getName()).getPrefix() + ((TeamsUtils.getTeam(player.getName()) != null) ? TeamsUtils.getTeamPrefix(player.getName()) : "§7") + player.getName());
		
		if (player.isDead()) player.spigot().respawn();
		
		PlayerUtils.updateNametag(player.getName());
		
		if (Game.getStatus() != Status.TELEPORT && Game.getStatus() != Status.PLAY || Game.getStatus() != Status.END) {
			
			World lobby = Bukkit.getWorld("lobby");
			player.teleport(lobby.getSpawnLocation().add(0.5, 0, 0.5));
			player.setGameMode(GameMode.ADVENTURE);
			PlayerUtils.clearInventory(player);
			PlayerUtils.clearXp(player);
			PlayerUtils.feed(player);
			PlayerUtils.heal(player);
			PlayerUtils.clearEffects(player);
			player.setExp(0);
			player.setTotalExperience(0);
			
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§7Click to see the rules.");
			ItemStack rules = ItemsUtils.createItem(Material.PAPER, "§b§lThe Rules §7(Right-Click)", 1, 0, lore);
			rules = ItemsUtils.addGlow(rules);
			
			player.getInventory().setItem(4, rules);
			
		}
		
		if (player.hasPermission("global.fly")) player.setAllowFlight(true);
		
		new BukkitRunnable() {
			
			public void run() {
				
				DisplayUtils.sendTitle(player, "§aUHC by CommandsPVP", "§7UHC §aFFA §7⋯ Nether §aOFF §7⋯ CutClean §aON", 0, 40, 10);
				
			}
			
		}.runTaskLater(Main.uhc, 5);
		
	}

}

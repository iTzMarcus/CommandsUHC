package com.thetonyk.UHC.Features;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyFly implements Listener {
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		
		Player player = event.getPlayer();
		GameMode gamemode = player.getGameMode();
		World world = player.getWorld();
		
		if (gamemode == GameMode.CREATIVE || gamemode == GameMode.SPECTATOR) return;
		
		if (world.getName().equalsIgnoreCase("lobby") && player.hasPermission("global.fly")) {
			
			player.setAllowFlight(true);
			return;
			
		}
		
		player.setAllowFlight(false);
		player.setFlying(false);
		
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent event) {
		
		Player player = event.getPlayer();
		GameMode gamemode = event.getNewGameMode();
		World world = player.getWorld();
		 
		if (gamemode == GameMode.CREATIVE || gamemode == GameMode.SPECTATOR) return;
		
		if (world.getName().equalsIgnoreCase("lobby") && player.hasPermission("global.fly")) {
			
			player.setAllowFlight(true);
			return;
			
		}
		
		player.setAllowFlight(false);
		player.setFlying(false);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		World world = player.getWorld();
		
		if (!player.hasPermission("global.fly")) return;
		
		if (!world.getName().equalsIgnoreCase("lobby")) return;
			
		player.setAllowFlight(true);
		
	}

}

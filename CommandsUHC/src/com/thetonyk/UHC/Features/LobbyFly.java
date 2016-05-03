package com.thetonyk.UHC.Features;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyFly implements Listener {
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
		
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby") && event.getPlayer().hasPermission("global.fly")) {
			
			event.getPlayer().setAllowFlight(true);
			return;
			
		}
		
		event.getPlayer().setAllowFlight(false);
		event.getPlayer().setFlying(false);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (!event.getPlayer().hasPermission("global.fly")) return;
			
		event.getPlayer().setAllowFlight(true);
		
	}

}

package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class DeathRespawn implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		
		if (GameUtils.getStatus() != Status.PLAY) return;
		
		event.setRespawnLocation(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
		
		
	}
	
}

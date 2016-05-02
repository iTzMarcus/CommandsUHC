package com.thetonyk.UHC.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.thetonyk.UHC.Game;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Game.Status;
import com.thetonyk.pregenerator.DisplayUtils;
import com.thetonyk.pregenerator.Events.WorldBorderFillFinishedEvent;
import com.thetonyk.pregenerator.Events.WorldBorderFillStartEvent;

public class EnvironmentListener implements Listener {

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		
		if (!event.getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onPregenBegin(WorldBorderFillStartEvent event) {
		
		Bukkit.broadcastMessage(Main.PREFIX + "Pregeneration of world '§6" + event.getWorld().getName() + "§7' started.");
		
	}
	
	@EventHandler
	public void onPregenFinished(WorldBorderFillFinishedEvent event) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator clear all");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayUtils.sendActionBar(player, "§7Pregeneration of world '§6" + event.getWorld().getName() + "§7'... (§a100.00%§7)");
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "Pregeneration of world '§6" + event.getWorld().getName() + "§7' finished.");
				
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		
		if (Game.getStatus() != Status.TELEPORT) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent event) {
		
		if (!event.getBlock().getWorld().getName().equalsIgnoreCase("lobby")) return;
			
		if (event.getBlock().getType() == Material.REDSTONE_LAMP_ON) event.setNewCurrent(event.getOldCurrent());
		
	}
	
}

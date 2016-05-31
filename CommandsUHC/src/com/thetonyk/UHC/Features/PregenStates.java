package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thetonyk.UHC.Main;
import com.thetonyk.pregenerator.DisplayUtils;
import com.thetonyk.pregenerator.Events.WorldBorderFillFinishedEvent;
import com.thetonyk.pregenerator.Events.WorldBorderFillStartEvent;

public class PregenStates implements Listener {

	@EventHandler
	public void onPregenBegin(WorldBorderFillStartEvent event) {
		
		World world = event.getWorld();
		
		Bukkit.broadcastMessage(Main.PREFIX + "Pregeneration of world '§6" + world.getName() + "§7' started.");
		
	}
	
	@EventHandler
	public void onPregenFinished(WorldBorderFillFinishedEvent event) {
		
		World world = event.getWorld();
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pregenerator clear all");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayUtils.sendActionBar(player, "§7Pregeneration of world '§6" + world.getName() + "§7'... (§a100.00%§7)");
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "Pregeneration of world '§6" + world.getName() + "§7' finished.");
		
		System.gc();
				
	}
	
}

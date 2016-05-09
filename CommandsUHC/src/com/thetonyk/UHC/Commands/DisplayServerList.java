package com.thetonyk.UHC.Commands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.thetonyk.UHC.Utils.GameUtils;

public class DisplayServerList implements Listener {
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		
		event.setMaxPlayers(GameUtils.slots);
		
	}

}

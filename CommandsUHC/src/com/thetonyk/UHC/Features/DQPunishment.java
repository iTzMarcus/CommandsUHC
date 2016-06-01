package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class DQPunishment implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		if (!channel.equals("CommandsBungee")) return;
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
		if (subchannel.equals("Ban")) {
			
			String banned = in.readUTF();
			
			Bukkit.broadcastMessage(banned + " is banned");
			
		}
		
	}
	
}

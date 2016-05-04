package com.thetonyk.UHC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class MessengerListener implements PluginMessageListener {
	
	public static String lastServer;
	
	public MessengerListener() {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("GetServer");
		
		Bukkit.getServer().sendPluginMessage(Main.uhc, "BungeeCord", out.toByteArray());
		
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		if (!channel.equals("BungeeCord")) return;
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
		if (subchannel.equals("GetServer")) {
			
			String server = in.readUTF();
			
			lastServer = server;
			
		}
		
	}
	
}

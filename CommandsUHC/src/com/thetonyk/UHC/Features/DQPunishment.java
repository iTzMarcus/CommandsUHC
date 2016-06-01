package com.thetonyk.UHC.Features;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;

public class DQPunishment implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		if (!channel.equals("CommandsBungee")) return;
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
		if (subchannel.equals("Ban")) {
			
			String banned = in.readUTF();
			UUID uuid = UUID.fromString(banned);
			
			GameUtils.setDeath(uuid, true);
			Bukkit.getOfflinePlayer(uuid).setWhitelisted(false);
			Bukkit.broadcastMessage(Main.PREFIX + "There are §a" + GameUtils.getAlives().size() + " §7players alive.");

		}
		
	}
	
}

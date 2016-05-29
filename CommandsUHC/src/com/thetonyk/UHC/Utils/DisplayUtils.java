package com.thetonyk.UHC.Utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.thetonyk.UHC.Main;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class DisplayUtils {
	
	public static void sendActionBar(Player player, String message){
		
		EntityPlayer entity = ((CraftPlayer) player).getHandle();
		
		IChatBaseComponent jsonText = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(jsonText, (byte) 2);
		
		entity.playerConnection.sendPacket(packet);
	}
	
	public static void redditHearts() {
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.uhc, ListenerPriority.NORMAL, PacketType.Play.Server.LOGIN) {
					
			@Override
			public void onPacketSending(PacketEvent event) {
				
				if (event.getPacketType() != PacketType.Play.Server.LOGIN) return;
					
				event.getPacket().getBooleans().write(0, true);
				
			}
				
		});
		
	}
	
	public static void playersCount() {
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.uhc, ListenerPriority.NORMAL, PacketType.Status.Server.OUT_SERVER_INFO) {
			
			@Override
			public void onPacketSending(PacketEvent event) {
				
				if (event.getPacketType() != PacketType.Status.Server.OUT_SERVER_INFO) return;
					
				int players = GameUtils.getPlayersCount();
				
				WrappedServerPing count = event.getPacket().getServerPings().read(0);
				count.setPlayersOnline(players);
				
				event.getPacket().getServerPings().write(0, count);
				
			}
				
		});
		
	}
	
}

package com.thetonyk.UHC.Utils;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.thetonyk.UHC.Main;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class DisplayUtils {
	
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {

		IChatBaseComponent jsonTitle = ChatSerializer.a("{'text':'" + title + "'}");
		IChatBaseComponent jsonSubtitle = ChatSerializer.a("{'text':'" + subtitle + "'}");

		PacketPlayOutTitle sendTime = new PacketPlayOutTitle(EnumTitleAction.TIMES, (IChatBaseComponent) null, fadeIn, stay, fadeOut);
		PacketPlayOutTitle sendTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, jsonTitle, fadeIn, stay, fadeOut);
		PacketPlayOutTitle sendSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, jsonSubtitle);

		((CraftPlayer)player).getHandle().playerConnection.sendPacket(sendTime);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(sendSubtitle);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(sendTitle);
		
	}
	
	public static void sendActionBar(Player player, String message){
		
		IChatBaseComponent jsonText = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(jsonText, (byte) 2);
		
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendTab(Player player) {
		
		String name = player.getName();
		int ping = ((CraftPlayer)player).getHandle().ping;
		
		double rawTps = MinecraftServer.getServer().recentTps[0];
		DecimalFormat format = new DecimalFormat("##.##");
		String tps = format.format(rawTps);
		
		IChatBaseComponent jsonHeader = ChatSerializer.a("{'text':'\n §7Welcome on the UHC, §a" + name + " §7! \n §b@CommandsPVP  §7⋯  §aTS: §bcommandspvp.com \n'}");
		IChatBaseComponent jsonFooter = ChatSerializer.a("{'text':'\n §7Players: §a" + Bukkit.getOnlinePlayers().size() + "  §7⋯  Ping: §a" + ping + "ms  §7⋯  TPS: §a" + tps + " \n'}");
		
		PacketPlayOutPlayerListHeaderFooter tabHeader = new PacketPlayOutPlayerListHeaderFooter(jsonHeader);
		
		try {
			
			Field sendTab = tabHeader.getClass().getDeclaredField("b");
			sendTab.setAccessible(true);
			sendTab.set(tabHeader, jsonFooter);
			sendTab.setAccessible(!sendTab.isAccessible());
			
		} catch (Exception e) {
			
			Bukkit.getLogger().severe("§cError to set tab header & footer to " + name);
			
		}
		
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(tabHeader);
		
	}
	
	public static void redditHearts() {
		
		final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		
		protocolManager.addPacketListener(new PacketAdapter(Main.uhc, ListenerPriority.NORMAL, PacketType.Play.Server.LOGIN) {
					
			@Override
			public void onPacketSending(PacketEvent event) {
				
				if (event.getPacketType() == PacketType.Play.Server.LOGIN) {
					
					event.getPacket().getBooleans().write(0, true);
					
				}
				
			}
				
		});
		
	}
	
}

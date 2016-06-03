package com.thetonyk.UHC.Utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Packets.PacketHandler;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;

public class SignGUIUtils {
	
	private EntityPlayer entityPlayer;
	private PacketHandler packetsListener;
	private Listener listener;
	
	public SignGUIUtils (Player player, Callback<String[]> callback) {
		
		this.entityPlayer = ((CraftPlayer) player).getHandle();
		
		IChatBaseComponent[] text = new IChatBaseComponent[4];
		text[0] = new ChatMessage("Test;");
		
		this.entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(new BlockPosition(0, 0, 0)));
		
		packetsListener = new PacketHandler() {
			
			@Override
			public Object onPacketIn(Player player, Object packet) {
				
				if (!(packet instanceof PacketPlayInUpdateSign)) super.onPacketIn(player, packet);
				
				if (!player.getUniqueId().equals(entityPlayer.getUniqueID())) return super.onPacketIn(player, packet);
				
				PacketPlayInUpdateSign nmsPacket = (PacketPlayInUpdateSign) packet;
				
				IChatBaseComponent[] lines = nmsPacket.b();
				
				String[] rawLines = new String[4];
				
				for (int i = 0; i < lines.length; i++) {
					
					String line = IChatBaseComponent.ChatSerializer.a(lines[i]);
					
					rawLines[i] = line.length() < 1 ? "" : line.substring(1, line.length() - 1);
					
				}
				
				callback.onConfirm(rawLines);
				delete();
				
				return super.onPacketIn(player, packet);
				
			}
				
		};
		
		listener = new Listener() {
			
			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				
				callback.onDisconnect();
				delete();
				
			}
			
		};
		
		Bukkit.getPluginManager().registerEvents(listener, Main.uhc);
		
	}
	
	public void delete() {
		
		packetsListener.delete();
		HandlerList.unregisterAll(listener);
		
	}
	
	public interface Callback<T> {
		
		void onConfirm(String[] lines);
		void onDisconnect();
		
	}

}

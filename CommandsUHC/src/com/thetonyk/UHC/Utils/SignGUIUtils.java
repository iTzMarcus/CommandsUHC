package com.thetonyk.UHC.Utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.thetonyk.UHC.Main;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;

public class SignGUIUtils {
	
	private EntityPlayer entityPlayer;
	private PacketAdapter listener;
	
	public SignGUIUtils (Player player, Callback<String[]> callback) {
		
		this.entityPlayer = ((CraftPlayer) player).getHandle();
		
		IChatBaseComponent[] text = new IChatBaseComponent[4];
		text[0] = new ChatMessage("Test;");
		
		this.entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(new BlockPosition(0, 0, 0)));
		
		
		listener = new PacketAdapter(Main.uhc, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
			
			@Override
			public void onPacketReceiving(PacketEvent event) {
				
				if (event.getPacketType() != PacketType.Play.Client.UPDATE_SIGN) return;
				
				if (!event.getPlayer().getUniqueId().equals(entityPlayer.getUniqueID())) return;
				
				WrappedChatComponent[] lines = event.getPacket().getChatComponentArrays().getValues().get(0);
				String[] rawLines = new String[4];
				
				for (int i = 0; i < lines.length; i++) {
					
					rawLines[i] = lines[i].getJson().substring(1, lines[i].getJson().length() - 1);
					
				}
				
				callback.onConfirm(rawLines);
				delete();
				
			}
				
		};
		
		ProtocolLibrary.getProtocolManager().addPacketListener(listener);
		
	}
	
	public void delete() {
		
		ProtocolLibrary.getProtocolManager().removePacketListener(listener);
		
	}
	
	public interface Callback<T> {
		
		void onConfirm(String[] lines);
		
	}

}

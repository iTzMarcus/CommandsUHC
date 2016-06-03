package com.thetonyk.UHC.Packets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;

import com.thetonyk.UHC.Main;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.ServerConnection;

public class PacketHandler implements Listener {
	
	Boolean stop = false;
	ChannelInboundHandlerAdapter serverHandler;
	ChannelInitializer<Channel> begin;
	ChannelInitializer<Channel> end;
	List<Channel> serverChannels = new ArrayList<Channel>();
	
	@SuppressWarnings("unchecked")
	public PacketHandler() {
		
		Bukkit.getPluginManager().registerEvents(this, Main.uhc);
		
		ServerConnection connection = MinecraftServer.getServer().getServerConnection();
		
		end = new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel channel) throws Exception {
				
				ChannelHandler handler = channel.pipeline().get("CommandsPackets");
				
				if (handler != null) {
					
					channel.pipeline().remove("CommandsPackets");
					
				}
				
				PacketInterceptor interceptor = new PacketInterceptor();
				channel.pipeline().addBefore("packet_handler", "CommandsPackets", interceptor);
				
			}
			
		};
		
		begin = new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel channel) throws Exception {
			
				channel.pipeline().addLast(end);
				
			}
			
		};
		
		serverHandler = new ChannelInboundHandlerAdapter() {
			
			@Override
			public void channelRead(ChannelHandlerContext context, Object packet) {
				
				Channel channel = (Channel) packet;
				
				channel.pipeline().addFirst(begin);
				context.fireChannelRead(packet);
				
			}
			
		};
		
		List<ChannelFuture> list = null;
		
		try {
			
			Field field = connection.getClass().getDeclaredField("g");
			field.setAccessible(true);
			list = (List<ChannelFuture>) field.get(connection);
			
			
		} catch (Exception exception) {
			
			exception.printStackTrace();
		
		}
		
		for (ChannelFuture channelFuture : list) {
			
			Channel channel = channelFuture.channel();
			
			serverChannels.add(channel);
			channel.pipeline().addFirst(serverHandler);
			
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			addPlayer(player);
			
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (stop) return;
		
		Player player = event.getPlayer();
		
		addPlayer(player);
		
	}
	
	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		
		if (!event.getPlugin().equals(Main.uhc)) return;
		
		delete();
		
	}
	
	public Object onPacketIn(Player player, Object packet) {
		
		return packet;
		
	}
	
	public Object onPacketOut(Player player, Object packet) {
		
		return packet;
		
	}
	
	public void sendPacket(Player player, Object packet) {
		
		Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
		
		channel.pipeline().writeAndFlush(packet);
		
	}
	
	private void addPlayer(Player player) {
		
		Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
		ChannelHandler handler = channel.pipeline().get("CommandsPackets");
		
		if (handler != null) {
			
			channel.pipeline().remove("CommandsPackets");
			
		}
		
		PacketInterceptor interceptor = new PacketInterceptor();
		channel.pipeline().addBefore("packet_handler", "CommandsPackets", interceptor);
		interceptor.player = player;
		
	}
	
	public void delete() {
		
		if (stop) return;
		
		stop = true;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
			
			if (channel.pipeline().get("CommandsPackets") == null) continue;
			
			channel.pipeline().remove("CommandsPackets");
			
		}
		
		HandlerList.unregisterAll(this);
		
		if (serverHandler == null) return;
		
		for (Channel channel : serverChannels) {
			
			if (channel.pipeline().get("CommandsPackets") == null) continue;
			
			channel.pipeline().remove("CommandsPackets");
			
		}
		
	}
	
	public class PacketInterceptor extends ChannelDuplexHandler {
		
		public Player player;
		
		@Override
		public void channelRead(ChannelHandlerContext context, Object packet) {
			
			packet = onPacketIn(player, packet);
			
			if (packet == null) return;
			
			try {
				
				super.channelRead(context, packet);
				
			} catch (Exception e) {
				
				Bukkit.getLogger().severe("[PacketHandler] Can't receive packet.");
				
			}
			
		}
		
		@Override
		public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) {
			
			packet = onPacketOut(player, packet);
			
			try {
				
				super.write(context, packet, promise);
				
			} catch (Exception e) {
				
				Bukkit.getLogger().severe("[PacketHandler] Can't send packet.");
				
			}
			
		}

	}

}

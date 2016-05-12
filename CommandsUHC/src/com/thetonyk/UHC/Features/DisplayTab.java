package com.thetonyk.UHC.Features;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class DisplayTab implements Listener {
	
	public DisplayTab() {
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					updateTab(player);
					
				}
				
			}
			
		}.runTaskTimer(Main.uhc, 0, 20);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		updateTab(event.getPlayer());
		
	}
	
	private void updateTab (Player player) {
		
		int ping = ((CraftPlayer)player).getHandle().ping;
		double rawTps = MinecraftServer.getServer().recentTps[0];
		DecimalFormat format = new DecimalFormat("##.##");
		String tps = format.format(rawTps);
		String pvp = DisplayTimers.getTimeLeftPVP() > 0 ? DisplayTimers.getOtherFormatedTime(DisplayTimers.getTimeLeftPVP()) : "ON";
		String meetup = DisplayTimers.getTimeLeftMeetup() > 0 ? DisplayTimers.getOtherFormatedTime(DisplayTimers.getTimeLeftMeetup()) : "Now";
		String border = GameUtils.getWorld() != null ? (Bukkit.getWorld(GameUtils.getWorld()) != null ? (int) Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getSize() + "§7x§a" + (int) Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getSize() : "Not ready") : "Not ready";
		int players = GameUtils.getPlayersCount();
		
		DisplayUtils.sendTab(player, "\n §7Welcome on the UHC, §a" + player.getName() + " §7! \n §b@CommandsPVP §7⋯ §aTS: §bcommandspvp.com \n §7Players: §a" + players + " §7⋯ Ping: §a" + ping + "ms §7⋯ TPS: §a" + tps + " \n", "\n §7PVP: §a" + pvp + " §7⋯ Meetup: §a" + meetup + " §7⋯ Border: §a" + border + " \n");
		
	}

}

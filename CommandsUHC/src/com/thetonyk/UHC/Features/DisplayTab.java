package com.thetonyk.UHC.Features;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.World;
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
				
				updateTab();
				
			}
			
		}.runTaskTimer(Main.uhc, 5, 5);
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		updateTab();
		
	}
	
	private void updateTab () {
		
		DecimalFormat format = new DecimalFormat("##.##");
		int pvpTime = DisplayTimers.getTimeLeftPVP();
		int meetupTime = DisplayTimers.getTimeLeftMeetup();
		World world = Bukkit.getWorld(GameUtils.getWorld());
		
		double rawTps = MinecraftServer.getServer().recentTps[0];
		String tps = format.format(rawTps);
		String pvp = pvpTime > 0 ? DisplayTimers.getOtherFormatedTime(pvpTime) : "ON";
		String meetup = meetupTime > 0 ? DisplayTimers.getOtherFormatedTime(meetupTime) : "Now";
		String border = world != null ? (int) world.getWorldBorder().getSize() + "§7x§a" + (int) world.getWorldBorder().getSize() : "Not ready";
		int players = GameUtils.getPlayersCount();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			int ping = ((CraftPlayer)player).getHandle().ping;
			DisplayUtils.sendTab(player, "\n §7Welcome on the UHC, §a" + player.getName() + " §7! \n §b@CommandsPVP §7⋯ §aTS: §bcommandspvp.com \n §7Players: §a" + players + " §7⋯ Ping: §a" + ping + "ms §7⋯ TPS: §a" + tps + " \n", "\n §7PVP: §a" + pvp + " §7⋯ Meetup: §a" + meetup + " §7⋯ Border: §a" + border + " \n");
		
		}
		
	}

}

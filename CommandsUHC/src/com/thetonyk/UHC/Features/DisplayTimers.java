package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;

public class DisplayTimers {
	
	public static BukkitRunnable timer = null;
	public static int time = 0;
	
	public static void startTimer() {
		
		if (timer != null && Bukkit.getScheduler().isCurrentlyRunning(timer.getTaskId())) timer.cancel();
		
		time = 0;
		int pvpTime = 120;
		int meetupTime = 180;
		
		timer = new BukkitRunnable() {
			
			public void run() {
				
				if (time < 60) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendActionBar(player, "§7Final heal §8⫸ §a" + DisplayTimers.getFormatedTime(60 - time) + " §8| §7PVP §8⫸ §a" + DisplayTimers.getFormatedTime(pvpTime - time) + " §8| §7Meetup §8⫸ §a" + DisplayTimers.getFormatedTime(meetupTime - time));
						
					}
					
				} else if (time < pvpTime) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendActionBar(player, "§7PVP §8⫸ §a" + DisplayTimers.getFormatedTime(pvpTime - time) + " §8| §7Meetup §8⫸ §a" + DisplayTimers.getFormatedTime(meetupTime - time));
						
					}
					
				} else if (time < meetupTime) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendActionBar(player, "§7Meetup §8⫸ §a" + DisplayTimers.getFormatedTime(meetupTime - time));
						
					}
					
				} else {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendActionBar(player, "§8⫸ §6Go to the middle of the map §8⫷");
						
					}
					
				}
				
				if (time == 60) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						PlayerUtils.feed(player);
						PlayerUtils.heal(player);
						player.setFireTicks(0);
						
						DisplayUtils.sendTitle(player, "§aFinal heal", "§7Do not ask for others heals.", 5, 30, 5);
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
						
					}
					
					Bukkit.broadcastMessage(Main.PREFIX + "All players were full healed.");
					
				}
				
				if (time == pvpTime) {
					
					Bukkit.getWorld(GameUtils.getWorld()).setPVP(true);
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendTitle(player, "", "§aPVP §7enabled", 5, 30, 5);
						player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 1);
						
					}
					
					Bukkit.broadcastMessage(Main.PREFIX + "The PVP is now enabled.");
					
				}
				
				if (time == meetupTime) {
					
					Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().setSize(100, 1200);
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendTitle(player, "", "§aMeetup §7is now!", 5, 30, 5);
						player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 1);
						
					}
					
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(Main.PREFIX + "The meetup is now!");
					Bukkit.broadcastMessage(Main.PREFIX + "Go to the middle of the map.");
					Bukkit.broadcastMessage(Main.PREFIX + "Border will now shrink to §6100 §7x §6100 §7over §a20 minutes§7.");
					
				}
				
				time++;
				
			}
			
		};
		
		timer.runTaskTimer(Main.uhc, 0, 20);
		
	}
	
	private static String getFormatedTime(int time) {
		
		int timer = time;
		
		int hours = (int) Math.floor(timer / 3600);
		timer -= hours * 3600;
		
		int minutes = (int) Math.floor(timer / 60);
		timer -= minutes * 60;
		
		StringBuilder stringTime = new StringBuilder();
		
		if (hours > 0) {
			
			stringTime.append(hours);
			stringTime.append("h");
			
		}
		
		if (minutes > -1) {
			
			stringTime.append(minutes);
			stringTime.append("m");
			
		}
		
		stringTime.append(timer);
		stringTime.append("s");
		
		return stringTime.toString();
		
	}

}

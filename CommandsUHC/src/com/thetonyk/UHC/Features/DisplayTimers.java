package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.FinalHealEvent;
import com.thetonyk.UHC.Events.MeetupEvent;
import com.thetonyk.UHC.Events.PVPEvent;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;

public class DisplayTimers {
	
	public static BukkitRunnable timer = null;
	public static int time = 0;
	public static int pvpTime = 120;
	public static int meetupTime = 180;
	
	public static void startTimer() {
		
		if (timer != null) {
			
			timer.cancel();
			timer = null;
			
		}
		
		time = 0;
		pvpTime = 120;
		meetupTime = 180;
		
		timer = new BukkitRunnable() {
			
			public void run() {
				
				if (time < 45) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						DisplayUtils.sendActionBar(player, "§7Final heal §8⫸ §a" + DisplayTimers.getFormatedTime(45 - time) + " §8| §7PVP §8⫸ §a" + DisplayTimers.getFormatedTime(pvpTime - time) + " §8| §7Meetup §8⫸ §a" + DisplayTimers.getFormatedTime(meetupTime - time));
						
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
				
				if (time == 45) {
					
					Bukkit.getPluginManager().callEvent(new FinalHealEvent());
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						PlayerUtils.feed(player);
						PlayerUtils.heal(player);
						player.setFireTicks(0);
						
						Bukkit.getWorld(GameUtils.getWorld()).setSpawnFlags(true, true);
						
						DisplayUtils.sendTitle(player, "§aFinal heal", "§7Do not ask for others heals.", 5, 30, 5);
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
						
					}
					
					Bukkit.broadcastMessage(Main.PREFIX + "All players were full healed.");
					
				}
				
				if (time == pvpTime) {
					
					Bukkit.getPluginManager().callEvent(new PVPEvent());
					
				}
				
				if (time == meetupTime) {
					
					Bukkit.getPluginManager().callEvent(new MeetupEvent());
					
				}
				
				time++;
				
			}
			
		};
		
		timer.runTaskTimer(Main.uhc, 0, 20);
		
	}
	
	public static String getFormatedTime (int time) {
		
		int timer = time;
		
		int hours = (int) Math.floor(timer / 3600);
		timer -= hours * 3600;
		
		int minutes = (int) Math.floor(timer / 60);
		timer -= minutes * 60;
		
		StringBuilder stringTime = new StringBuilder();
		
		if (hours > 0) {
			
			if (hours < 10) stringTime.append("0");
			stringTime.append(hours);
			stringTime.append("h");
			
		}
		
		if (minutes > 0) {
			
			if (minutes < 10) stringTime.append("0");
			stringTime.append(minutes);
			stringTime.append("m");
			
		}
		
		if (timer < 10) stringTime.append("0");
		stringTime.append(timer);
		stringTime.append("s");
		
		return stringTime.toString();
		
	}
	
	public static String getOtherFormatedTime (int time) {
		
		int timer = time;
		
		int hours = (int) Math.floor(timer / 3600);
		timer -= hours * 3600;
		
		int minutes = (int) Math.floor(timer / 60);
		timer -= minutes * 60;
		
		StringBuilder stringTime = new StringBuilder();
		
		if (hours > 0) {
			
			if (hours < 10) stringTime.append("0");
			stringTime.append(hours);
			stringTime.append(":");
			
		}
		
		if (minutes > -1) {
			
			if (minutes < 10) stringTime.append("0");
			stringTime.append(minutes);
			stringTime.append(":");
			
		} else {
			
			stringTime.append("00:");
			
		}
		
		if (timer < 10) stringTime.append("0");
		stringTime.append(timer);
		
		return stringTime.toString();
		
	}
	
	public static int getTime() {
		
		return time;
		
	}
	
	public static int getTimeLeftPVP() {
		
		return (pvpTime - time);
		
	}
	
	public static int getTimeLeftMeetup() {
		
		return (meetupTime - time);
		
	}

}

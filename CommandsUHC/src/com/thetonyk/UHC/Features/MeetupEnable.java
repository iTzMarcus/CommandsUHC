package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.MeetupEvent;
import com.thetonyk.UHC.Utils.DisplayUtils;

public class MeetupEnable implements Listener {

	@EventHandler
	public void onMeetup(MeetupEvent event) {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayUtils.sendTitle(player, "", "§aMeetup §7is now!", 5, 30, 5);
			player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 1, 1);
			
		}
		
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Main.PREFIX + "The meetup is now!");
		Bukkit.broadcastMessage(Main.PREFIX + "Go to the middle of the map.");
		
		MeetupBorder.startShrink();
		
	}
	
}

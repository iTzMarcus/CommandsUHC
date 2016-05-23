package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.MeetupEvent;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;

public class MeetupEnable implements Listener {

	@EventHandler
	public void onMeetup(MeetupEvent event) {
		
		World world = Bukkit.getWorld(GameUtils.getWorld());
		
		if (world == null) return;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayUtils.sendTitle(player, "", "§aMeetup §7is now!", 5, 30, 5);
			
			if (PlayerUtils.getNosoundState(player) == 1) continue;
			
			player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 1, 1);
			
		}
		
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Main.PREFIX + "The meetup is now!");
		Bukkit.broadcastMessage(Main.PREFIX + "Go to the middle of the map.");
		
		world.setTime(6000);
		world.setStorm(false);
		world.setThundering(false);
		world.setGameRuleValue("doDaylightCycle", "false");
		MeetupBorder.startShrink();
		
	}
	
}

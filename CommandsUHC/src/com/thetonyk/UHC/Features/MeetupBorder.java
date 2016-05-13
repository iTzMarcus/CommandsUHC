package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class MeetupBorder {

	public static void startShrink() {
		
		Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().setSize(Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().getSize());
		
		int time = (WorldUtils.getSize(GameUtils.getWorld()) - 100) / 3;
		
		Bukkit.getWorld(GameUtils.getWorld()).getWorldBorder().setSize(100, time);
		
		Bukkit.broadcastMessage(Main.PREFIX + "Border will now shrink to §6100§7x§6100 §7by §a3§7m/s.");
		
	}
	
}

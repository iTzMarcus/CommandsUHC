package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

public class MeetupBorder {

	public static void startShrink() {
		
		World world = Bukkit.getWorld(GameUtils.getWorld());
		
		if (world == null) return;
		
		int worldSize = WorldUtils.getSize(world.getName());
		int time = (worldSize - 100) / 3;
		
		world.getWorldBorder().setSize(100, time);
		
		Bukkit.broadcastMessage(Main.PREFIX + "Border will now shrink to §6100§7x§6100 §7by §a3§7m/s.");
		
	}
	
}

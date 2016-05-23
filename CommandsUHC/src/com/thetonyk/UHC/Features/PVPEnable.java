package com.thetonyk.UHC.Features;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.PVPEvent;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;

public class PVPEnable implements Listener {

	@EventHandler
	public void onPVPEnable(PVPEvent event) {
		
		World world = Bukkit.getWorld(GameUtils.getWorld());
		
		if (world == null) return;
		
		world.setPVP(true);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			DisplayUtils.sendTitle(player, "", "§aPVP §7enabled", 5, 30, 5);
			
			if (PlayerUtils.getNosoundState(player) == 1) continue;
			
			player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 1);
			
		}
		
		Bukkit.broadcastMessage(Main.PREFIX + "The PVP is now enabled.");
		
	}
	
}

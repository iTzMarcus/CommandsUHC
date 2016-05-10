package com.thetonyk.UHC.Features;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.thetonyk.UHC.Utils.GameUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class ChatSettings implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChat (AsyncPlayerChatEvent event) {
		
		Set<Player> players = new HashSet<Player>();
		players.addAll(event.getRecipients());
		
		for (Player receiver : players) {
			
			if (PlayerUtils.getChatVisibility(receiver) == 0) {
				
				event.getRecipients().remove(receiver);
				continue;
				
			}
			
			if (PlayerUtils.getMentionsState(receiver) == 0) continue;
			
			if (!event.getMessage().contains(receiver.getName())) continue;
			
			event.getRecipients().remove(receiver);
			receiver.sendMessage(PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + "§7" + event.getPlayer().getName() + " §8⫸ §f" + event.getMessage().replaceAll(receiver.getName(), "§a§l" + receiver.getName() + "§r"));
			receiver.playSound(receiver.getLocation(), Sound.ORB_PICKUP, 1, 1);
			
		}
		
		if (GameUtils.getStatus() == Status.PLAY && event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
			
			for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
				
				player.sendMessage("§6DeathChat §8|" + PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getPlayer().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getPlayer().getName()) : "§7") + event.getPlayer().getName() + " §8⫸ §f" + event.getMessage());
				
			}
			
			return;
			
		}
		
		event.setFormat(PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getPlayer().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getPlayer().getName()) : "§7") + event.getPlayer().getName() + " §8⫸ §f%2$s");
		
	}
	
}

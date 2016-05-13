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

import static net.md_5.bungee.api.ChatColor.*;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

import com.thetonyk.UHC.Utils.GameUtils.Status;
import com.thetonyk.UHC.Utils.PlayerUtils.Rank;

public class ChatSettings implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat (AsyncPlayerChatEvent event) {
		
		Set<Player> players = new HashSet<Player>();
		players.addAll(event.getRecipients());
		
		for (Player receiver : players) {
			
			if (PlayerUtils.getChatVisibility(receiver) == 1) continue;
				
			event.getRecipients().remove(receiver);
			
		}
		
		if (GameUtils.getStatus() == Status.PLAY && event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
			
			for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
				
				player.sendMessage("§6DeathChat §8|" + PlayerUtils.getRank(event.getPlayer().getName()).getPrefix() + ((TeamsUtils.getTeam(event.getPlayer().getName()) != null) ? TeamsUtils.getTeamPrefix(event.getPlayer().getName()) : "§7") + event.getPlayer().getName() + "§r §8⫸ §f" + event.getMessage());
				
			}
			
			event.setCancelled(true);
			return;
			
		}
		
		ChatColor rankColor = GRAY;
		
		switch (PlayerUtils.getRank(event.getPlayer().getName())) {
		
			case PLAYER:
				break;
			case WINNER:
				rankColor = GOLD;
				break;
			case FAMOUS:
				rankColor = AQUA;
				break;
			case BUILDER:
			case ACTIVE_BUILDER:
				rankColor = DARK_GREEN;
				break;
			case STAFF:
			case HOST:
				rankColor = RED;
				break;
			case MOD:
				rankColor = BLUE;
				break;
			case ADMIN:
				rankColor = DARK_RED;
				break;
			case FRIEND:
				rankColor = DARK_AQUA;
				break; 
			default:
				break;
				
		}
		
		for (Player receiver : event.getRecipients()) {
		
			ComponentBuilder message = new ComponentBuilder("");
			if (GameUtils.getSpectate(event.getPlayer().getUniqueId())) message.append("Spec").color(GRAY).italic(true).append(" | ").color(DARK_GRAY).italic(false);
			if (PlayerUtils.getRank(event.getPlayer().getName()) != Rank.PLAYER) message.append(PlayerUtils.getRank(event.getPlayer().getName()).getName().substring(2)).color(rankColor).append(" | ").color(DARK_GRAY);
			message.append(event.getPlayer().getName()).color(GRAY);
			if (!receiver.equals(event.getPlayer())) message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameUtils.getSpectate(receiver.getUniqueId()) ? "Teleport to " : "Send a message to ").color(GRAY).append(event.getPlayer().getName()).color(GREEN).append(".").color(GRAY).create()));
			if (!receiver.equals(event.getPlayer())) message.event(new ClickEvent(GameUtils.getSpectate(receiver.getUniqueId()) ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND, GameUtils.getSpectate(receiver.getUniqueId()) ? "/tp " + event.getPlayer().getName() : "/msg " + event.getPlayer().getName() + " "));
			message.append(" ⫸ ").retain(FormatRetention.NONE).color(DARK_GRAY);
			
			if (PlayerUtils.getMentionsState(receiver) == 1 && event.getMessage().contains(receiver.getName())) {
				
				message.append(event.getMessage().replaceAll(receiver.getName(), "§a§l" + receiver.getName() + "§r")).color(WHITE);
				receiver.playSound(receiver.getLocation(), Sound.ORB_PICKUP, 1, 1);
				
			} else message.append(event.getMessage()).color(WHITE);
			
			receiver.spigot().sendMessage(message.create());
			
		}
		
		event.setCancelled(true);
		
	}
	
}

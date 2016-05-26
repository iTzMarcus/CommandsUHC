package com.thetonyk.UHC.Features;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
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
		
		UUID uuid = event.getPlayer().getUniqueId();
		String name = event.getPlayer().getName();
		String team = TeamsUtils.getTeam(uuid);
		World world = event.getPlayer().getWorld();
		Set<Player> players = new HashSet<Player>(event.getRecipients());
		Set<Player> receivers = event.getRecipients();
		String oldMessage = event.getMessage();
		Status status = GameUtils.getStatus();
		
		for (Player receiver : players) {
			
			if (PlayerUtils.getChatVisibility(receiver) == 1) continue;
				
			receivers.remove(receiver);
			
		}
		
		if (status == Status.PLAY && world.getName().equalsIgnoreCase("lobby")) {
			
			receivers.clear();
			receivers.addAll(world.getPlayers());
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				if (!GameUtils.getSpectate(player.getUniqueId())) continue;
				
				receivers.add(player);
				
			}

		}
		
		ChatColor rankColor = GRAY;
		
		switch (PlayerUtils.getRank(uuid)) {
		
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
		
		for (Player receiver : receivers) {
		
			ComponentBuilder message = new ComponentBuilder("");
			if (GameUtils.getSpectate(uuid)) message.append("Spec").color(GRAY).italic(true).append(" | ").color(DARK_GRAY).italic(false);
			if (GameUtils.getStatus() == Status.PLAY && world.getName().equalsIgnoreCase("lobby")) message.append("DeathChat").color(GOLD).append(" | ").color(DARK_GRAY);
			if (PlayerUtils.getRank(uuid) != Rank.PLAYER) message.append(PlayerUtils.getRank(uuid).getName().substring(2)).color(rankColor).append(" | ").color(DARK_GRAY);
			message.append(name).color(GRAY);
			if (team != null) message.color(ChatColor.getByChar(TeamsUtils.getTeamPrefix(uuid).charAt(1))).bold(TeamsUtils.getTeamPrefix(uuid).contains("l")).italic(TeamsUtils.getTeamPrefix(uuid).contains("o")).underlined(TeamsUtils.getTeamPrefix(uuid).contains("n")).strikethrough(TeamsUtils.getTeamPrefix(uuid).contains("m"));
			if (!receiver.equals(event.getPlayer())) message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameUtils.getSpectate(receiver.getUniqueId()) ? "Teleport to " : "Send a message to ").color(GRAY).append(name).color(GREEN).append(".").color(GRAY).create()));
			if (!receiver.equals(event.getPlayer())) message.event(new ClickEvent(GameUtils.getSpectate(receiver.getUniqueId()) ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND, GameUtils.getSpectate(receiver.getUniqueId()) ? "/tp " + name : "/msg " + name + " "));
			message.append(" ⫸ ").retain(FormatRetention.NONE).color(DARK_GRAY);
			
			if (PlayerUtils.getMentionsState(receiver) == 1 && oldMessage.contains(receiver.getName())) {
				
				message.append(oldMessage.replaceAll(receiver.getName(), "§a§l" + receiver.getName() + "§r")).color(WHITE);
				receiver.playSound(receiver.getLocation(), Sound.ORB_PICKUP, 1, 1);
				
			} else message.append(oldMessage).color(WHITE);
			
			receiver.spigot().sendMessage(message.create());
			
		}

		event.setCancelled(true);
		
	}
	
}

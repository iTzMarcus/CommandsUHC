package com.thetonyk.UHC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Listener.MessengerListener;
import com.thetonyk.UHC.Commands.AcceptCommand;
import com.thetonyk.UHC.Commands.BorderCommand;
import com.thetonyk.UHC.Commands.ButcherCommand;
import com.thetonyk.UHC.Commands.GamemodeCommand;
import com.thetonyk.UHC.Commands.HelpopCommand;
import com.thetonyk.UHC.Commands.InviteCommand;
import com.thetonyk.UHC.Commands.PmCommand;
import com.thetonyk.UHC.Commands.PmcoordsCommand;
import com.thetonyk.UHC.Commands.PmoresCommand;
import com.thetonyk.UHC.Commands.PregenCommand;
import com.thetonyk.UHC.Commands.RankCommand;
import com.thetonyk.UHC.Commands.TCommand;
import com.thetonyk.UHC.Commands.TcCommand;
import com.thetonyk.UHC.Commands.TeamCommand;
import com.thetonyk.UHC.Commands.TeleportCommand;
import com.thetonyk.UHC.Commands.WhitelistCommand;
import com.thetonyk.UHC.Commands.WorldCommand;
import com.thetonyk.UHC.Listener.ChatListener;
import com.thetonyk.UHC.Listener.EnvironmentListener;
import com.thetonyk.UHC.Listener.InventoryListener;
import com.thetonyk.UHC.Listener.JoinListener;
import com.thetonyk.UHC.Listener.LeaveListener;
import com.thetonyk.UHC.Listener.PlayerListener;
import com.thetonyk.UHC.Utils.DisplayUtils;
import com.thetonyk.UHC.Utils.PermissionsUtils;
import com.thetonyk.UHC.Utils.PlayerUtils;
import com.thetonyk.UHC.Utils.TeamsUtils;
import com.thetonyk.UHC.Utils.WorldUtils;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Main extends JavaPlugin {
	
	public static Main uhc;
	
	public static final String NO_PERMS = "§fUnknown command.";
	public static final String PREFIX = "§a§lUHC §8⫸ §7";
	public static final ComponentBuilder PREFIX_COMPONENT = getPrefixComponent();
	
	@Override
	public void onEnable() {
		
		getLogger().info("UHC Plugin has been enabled.");
		getLogger().info("Plugin by TheTonyk for CommandsPVP");
		
		uhc = this;
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessengerListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "CommandsBungee");
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("GetServer");
		
		Bukkit.getServer().sendPluginMessage(Main.uhc, "BungeeCord", out.toByteArray());
		
		WorldUtils.loadAllWorlds();
		DisplayUtils.redditHearts();
		TeamsUtils.reload();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			PlayerUtils.updateNametag(player.getName());
			
			PermissionsUtils.clearPermissions(player);
			PermissionsUtils.setPermissions(player);
			PermissionsUtils.updateBungeePermissions(player);
			
		}
		
		this.getCommand("gamemode").setExecutor(new GamemodeCommand());
		this.getCommand("rank").setExecutor(new RankCommand());
		this.getCommand("world").setExecutor(new WorldCommand());
		this.getCommand("pregen").setExecutor(new PregenCommand());
		this.getCommand("border").setExecutor(new BorderCommand());
		this.getCommand("team").setExecutor(new TeamCommand());
		this.getCommand("teleport").setExecutor(new TeleportCommand());
		this.getCommand("invite").setExecutor(new InviteCommand());
		this.getCommand("accept").setExecutor(new AcceptCommand());
		this.getCommand("pm").setExecutor(new PmCommand());
		this.getCommand("pmcoords").setExecutor(new PmcoordsCommand());
		this.getCommand("pmores").setExecutor(new PmoresCommand());
		this.getCommand("t").setExecutor(new TCommand());
		this.getCommand("tc").setExecutor(new TcCommand());
		this.getCommand("whitelist").setExecutor(new WhitelistCommand());
		this.getCommand("butcher").setExecutor(new ButcherCommand());
		this.getCommand("helpop").setExecutor(new HelpopCommand());
		
		PluginManager manager = Bukkit.getPluginManager();
		
		manager.registerEvents(new JoinListener(), this);
		manager.registerEvents(new LeaveListener(), this);
		manager.registerEvents(new ChatListener(), this);
		manager.registerEvents(new PlayerListener(), this);
		manager.registerEvents(new InventoryListener(), this);
		manager.registerEvents(new EnvironmentListener(), this);
			
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendTab(player);
					
				}
				
			}
			
		}.runTaskTimer(Main.uhc, 0, 20);
		
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("UHC Plugin has been disabled.");
		
		uhc = null;
		
	}
	
	public static ComponentBuilder getPrefixComponent() {
		
		return new ComponentBuilder("UHC ").color(GREEN).bold(true).append("⫸ ").color(DARK_GRAY).bold(false);
		
	}
	
}

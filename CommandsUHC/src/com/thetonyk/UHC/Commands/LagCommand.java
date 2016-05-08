package com.thetonyk.UHC.Commands;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.thetonyk.UHC.Main;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class LagCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("uhc.lag")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		double rawTps = MinecraftServer.getServer().recentTps[0];
		DecimalFormat format = new DecimalFormat("##.##");
		String tps = format.format(rawTps);
		
		long ramUsage = (Runtime.getRuntime().totalMemory() / 1024 / 1024) - (Runtime.getRuntime().freeMemory() / 1024 / 1024);
		long ramMax = (Runtime.getRuntime().maxMemory() / 1024 / 1024);
		
		int entities = 0;
		int chunks = 0;
		
		for (World world : Bukkit.getWorlds()) {
			
			chunks += world.getLoadedChunks().length;
			entities += world.getEntities().size();
			
		}
		
		entities -= Bukkit.getOnlinePlayers().size();
		
		sender.sendMessage(Main.PREFIX + "About the server:");
		sender.sendMessage("§8⫸ §7Server TPS: §a" + tps + " §7(Press tab to see TPS)");
		sender.sendMessage("§8⫸ §7RAM: §a" + ramUsage + "§7MB/§c" + ramMax + "§7MB");
		sender.sendMessage("§8⫸ §7Entities: §a" + entities);
		sender.sendMessage("§8⫸ §7Loaded chunks: §a" + chunks);
		return true;
		
	}

}

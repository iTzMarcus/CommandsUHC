package com.thetonyk.UHC.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;

public class ScatterUtils {
	
	public static Map<String, Location> getSpawns(World world, int size) {
		
		//Inspired by @LeonTG77
		
		Map<String, Location> locations = new HashMap<String, Location>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			int minDistance = 150;
			
			for (int i = 0; i < 3000; i++) {
				
				if (i == 2999) {
					
					for (Player playerSend : Bukkit.getOnlinePlayers()) {
						
						if (playerSend.hasPermission("uhc.warning")) {
							
							playerSend.sendMessage(Main.PREFIX + "Error to generate a spawn.");
							
						}
						
					}
					
				}
				
				Random random = new Random();
				
				int x = random.nextInt(size) - (size / 2);
				int z = random.nextInt(size) - (size / 2);
				
				Location spawn = new Location(world, x + 0.5, 0, z + 0.5);
				
				Boolean near = false;
				
				for (Location loc : locations.values()) {
					
					if (loc.distanceSquared(spawn) < minDistance) {
						
						near = true;
						
					}
					
				}
				
				Boolean valid = true;
				
				if (world.getHighestBlockYAt(x, z) < 60) {
					
					valid = false;
					
				}
				
				Material block = new Location(world, x + 0.5, PlayerUtils.getHighestY(x, z, world), z + 0.5).getBlock().getType();
				
				switch (block) {
				
				case CACTUS:
				case LAVA:
				case STATIONARY_LAVA:
				case STATIONARY_WATER:
				case WATER:
					valid = false;
					break;
				default:
					break;
				
				}
				
				if (!near && valid) {
					
					spawn.setY(200);
					locations.put(player.getName(), spawn);
					break;
					
				} else {
					
					minDistance--;
					
				}
				
			}
			
		}
		
		return locations;
		
	}
	
	@SuppressWarnings("deprecation")
	public static void loadSpawns(Map<String, Location> locationsMap) {
		
		ArrayList<String> players = new ArrayList<String>(locationsMap.keySet());
		ArrayList<Location> locations = new ArrayList<Location>(locationsMap.values());
			
		new BukkitRunnable() {
			
			int i = 0;
			
			public void run() {
				
				Location location = locations.get(i);
				String player = players.get(i);
				
				for (Player playerOnline : Bukkit.getOnlinePlayers()) {
					DisplayUtils.sendActionBar(playerOnline, "§7Generation of spawn of '§6" + player + "§7' §8(§a" + (i + 1) + "§7/§a" + locationsMap.size() + "§8)");
				}
				
				Location loc1  = location.clone(), loc2  = location.clone(), loc3  = location.clone(), loc4 = location.clone(), loc5 = location.clone(), loc6 = location.clone();
				loc1.add(1, -1, 1);
				loc2.add(-1, -1, -1);
				loc3.add(2, 3, 2);
				loc4.add(-2, 0, -2);
				loc5.add(1, 2, 1);
				loc6.add(-1, 0, -1);
				
				for (double x = loc2.getX(); x <= loc1.getX(); x++) {
					
					for (double y = loc2.getY(); y <= loc1.getY(); y++) {
					
						for (double z = loc2.getZ(); z <= loc1.getZ(); z++) {
							
							location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setType(Material.STAINED_GLASS, true);
							location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setData((byte) 13, true);
							
						}
						
					}
				}
				
				for (double x = loc4.getX(); x <= loc3.getX(); x++) {
					
					for (double y = loc4.getY(); y <= loc3.getY(); y++) {
					
						for (double z = loc4.getZ(); z <= loc3.getZ(); z++) {
							
							location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setType(Material.BARRIER, true);
							
						}
						
					}
				}
				
				for (double x = loc6.getX(); x <= loc5.getX(); x++) {
					
					for (double y = loc6.getY(); y <= loc5.getY(); y++) {
					
						for (double z = loc6.getZ(); z <= loc5.getZ(); z++) {
							
							location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setType(Material.AIR, true);
							
						}
						
					}
				}
				
				location.getChunk().load(true);
				
				i++;
				
				if (i > (locationsMap.size() -1)) {
					
					cancel();
					
				}
				
			}
		
		}.runTaskTimer(Main.uhc, 5, 5);
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player playerOnline : Bukkit.getOnlinePlayers()) {
					DisplayUtils.sendActionBar(playerOnline, "§a" + locationsMap.size() + "§7 spawns successfully generated.");
				}
				
			}
			
		}.runTaskLater(Main.uhc, 20);
		
	}
	
	public static void teleportPlayers(Map<String, Location> locationsMap) {
		
		ArrayList<String> players = new ArrayList<String>(locationsMap.keySet());
		ArrayList<Location> locations = new ArrayList<Location>(locationsMap.values());
		
		new BukkitRunnable() {
			
			int i = 0;
			
			public void run() {
				
				Location location = locations.get(i);
				location.setY(location.getY() + 0.5);
				String name = players.get(i);
				
				for (Player playerOnline : Bukkit.getOnlinePlayers()) {
					DisplayUtils.sendActionBar(playerOnline, "§7Teleportation of '§6" + name + "§7' §8(§a" + (i + 1) + "§7/§a" + locationsMap.size() + "§8)");
				}
				
				Player player = Bukkit.getPlayer(name);
				
				if (player == null) {
					
					return;
					
				}
				
				player.teleport(location);
				
				i++;
				
				if (i > (locationsMap.size() -1)) {
					
					cancel();
					
				}
				
			}
		
		}.runTaskTimer(Main.uhc, 5, 5);
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player playerOnline : Bukkit.getOnlinePlayers()) {
					DisplayUtils.sendActionBar(playerOnline, "§a" + locationsMap.size() + "§7 players successfully teleported.");
				}
				
			}
			
		}.runTaskLater(Main.uhc, 20);
		
	}
	
}

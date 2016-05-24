package com.thetonyk.UHC.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Events.TeleportEvent;
import com.thetonyk.UHC.Features.DisplaySidebar;
import com.thetonyk.UHC.Utils.GameUtils.Status;

public class TeleportUtils {
	
	public static List<Location> getSpawns(World world, int count) {
		
		//Inspired by @LeonTG77
		
		List<Location> locations = new ArrayList<Location>();
		
		for (int i = 0; i < count; i++) {
			
			int minDistance = 150;
			
			for (int y = 0; y < 3000; y++) {
				
				if (y == 2999) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						if (!player.hasPermission("uhc.warning")) continue;
							
						player.sendMessage(Main.PREFIX + "Error to generate a spawn.");
						break;
						
					}
					
				} 
				
				Boolean near = false;
				Boolean valid = true;
				int x = (new Random().nextInt((int) world.getWorldBorder().getSize()) - (int) (world.getWorldBorder().getSize() / 2)) + (int) world.getWorldBorder().getCenter().getX();
				int z = (new Random().nextInt((int) world.getWorldBorder().getSize()) - (int) (world.getWorldBorder().getSize() / 2)) + (int) world.getWorldBorder().getCenter().getZ();
				
				Location spawn = new Location(world, x + 0.5, 0, z + 0.5);
				
				for (Location loc : locations) {
					
					if (loc.distanceSquared(spawn) >= minDistance) continue;
						
					near = true;
					
				}
				
				if (WorldUtils.getHighestY(x, z, world) < 60) valid = false;
				
				switch (new Location(world, x + 0.5, WorldUtils.getHighestY(x, z, world), z + 0.5).getBlock().getType()) {
				
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
				
				if (near || !valid) {
				
					minDistance--;
					continue;
					
				}
					
				spawn.setY(200);
				locations.add(spawn);
				break;
				
			}
			
		}
		
		return locations;
		
	}
	
	public static void loadSpawnsAndTeleport(List<Map.Entry<String, ?>> teleport) {
		
		List<Map.Entry<Map.Entry<String, ?>, Location>> locationsList = new ArrayList<Map.Entry<Map.Entry<String, ?>, Location>>();
		List<Location> spawns = getSpawns(Bukkit.getWorld(GameUtils.getWorld()), teleport.size());
		Iterator<Map.Entry<String, ?>> iterator = teleport.iterator();
		int i = 0;
		
		while (iterator.hasNext()) {
			
			Map.Entry<String, ?> entry = iterator.next();
			
			Location loc = null;
			
			if (GameUtils.getStatus() != Status.TELEPORT && entry.getKey().equalsIgnoreCase("uuid") && TeamsUtils.getTeam((UUID) entry.getValue()) != null && TeamsUtils.getTeamMembers(TeamsUtils.getTeam((UUID) entry.getValue())) != null) {
				
				for (UUID mate : TeamsUtils.getTeamMembers(TeamsUtils.getTeam(((UUID) entry.getValue())))) {
					
					if (!GameUtils.getLocations().containsKey(mate)) continue;
					
					loc = GameUtils.getLocations().get(mate);
					
				}
				
			}
			
			Location finalLoc = loc != null ? loc : spawns.get(i);
			
			Map.Entry<Map.Entry<String, ?>, Location> location = new Map.Entry<Map.Entry<String, ?>, Location>() {
				
				Location location = finalLoc;
	
				@Override
				public Map.Entry<String, ?> getKey() {
					
					return entry;
					
				}
	
				@Override
				public Location getValue() {
					
					return this.location;
					
				}
	
				@Override
				public Location setValue(Location location) {
				
					this.location = location;
					return this.location;
					
				}
				
			};
			
			locationsList.add(location);
			i++;
			
		}
		
		if (GameUtils.getStatus() == Status.TELEPORT) Bukkit.broadcastMessage(Main.PREFIX + "Generation and loading of spawns...");
		
		new BukkitRunnable() {
			
			int i = 0;
			
			@SuppressWarnings("deprecation")
			public void run() {
				
				if (i >= locationsList.size()) {
					
					cancel();
					
					new BukkitRunnable() {
						
						public void run() {
							
							new BukkitRunnable() {
								
								int i = 0;
								Map<UUID, Location> saveLocations = new HashMap<UUID, Location>();
								
								public void run() {
									
									if (i >= locationsList.size()) {
										
										cancel();
										
										if (GameUtils.getStatus() != Status.TELEPORT) {
											
											new BukkitRunnable() {
												
												public void run() {
													
													TeleportUtils.removeSpawns(saveLocations);
													
													for (Map.Entry<UUID, Location> entry : saveLocations.entrySet()) {
														
														GameUtils.addLocation(entry.getKey(), entry.getValue());
														
														Bukkit.getPlayer(entry.getKey()).closeInventory();
														Bukkit.getPlayer(entry.getKey()).setGameMode(GameMode.SURVIVAL);
														PlayerUtils.clearEffects(Bukkit.getPlayer(entry.getKey()));
														PlayerUtils.clearInventory(Bukkit.getPlayer(entry.getKey()));
														PlayerUtils.clearXp(Bukkit.getPlayer(entry.getKey()));
														PlayerUtils.feed(Bukkit.getPlayer(entry.getKey()));
														PlayerUtils.heal(Bukkit.getPlayer(entry.getKey()));
														DisplaySidebar.update(Bukkit.getPlayer(entry.getKey()));
														
													}
													
												}
												
											}.runTaskLater(Main.uhc, 20);
											
											return;
											
										}
										
										GameUtils.setLocations(saveLocations);
										
										Bukkit.getPluginManager().callEvent(new TeleportEvent());
										
										for (Player playerOnline : Bukkit.getOnlinePlayers()) {
											
											DisplayUtils.sendActionBar(playerOnline, "§a" + locationsList.size() + "§7 teams and players successfully teleported.");
											
										}
										
										return;
										
									}
									
									if (locationsList.get(i).getKey().getKey().equalsIgnoreCase("team")) {

										for (UUID mate : TeamsUtils.getTeamMembers((String) locationsList.get(i).getKey().getValue())) {
											
											saveLocations.put(mate, locationsList.get(i).getValue());
											
											if (Bukkit.getPlayer(mate) == null) continue;
											
											Bukkit.getPlayer(mate).teleport(locationsList.get(i).getValue().clone().add(0, 0.5, 0));
											GameUtils.setTeleported(Bukkit.getPlayer(mate).getUniqueId(), true);
											
										}
										
										i++;
										
										if (GameUtils.getStatus() != Status.TELEPORT) return;
										
										for (Player playerOnline : Bukkit.getOnlinePlayers()) {
											
											DisplayUtils.sendActionBar(playerOnline, "§7Teleportation of §6Team " + ((String) locationsList.get(i - 1).getKey().getValue()).substring(3) + " §8(§a" + i + "§7/§a" + locationsList.size() + "§8)");
											
										}
										
										return;
										
									}
									
									if (locationsList.get(i).getKey().getKey().equalsIgnoreCase("uuid")) {
										
										saveLocations.put((UUID) locationsList.get(i).getKey().getValue(), locationsList.get(i).getValue());
										
										if (Bukkit.getPlayer((UUID) locationsList.get(i).getKey().getValue()) != null) {
											
											Bukkit.getPlayer((UUID) locationsList.get(i).getKey().getValue()).teleport(locationsList.get(i).getValue().clone().add(0, 0.5, 0));
											GameUtils.setTeleported(Bukkit.getPlayer((UUID) locationsList.get(i).getKey().getValue()).getUniqueId(), true);
											
										}
										
										i++;
										
										if (GameUtils.getStatus() != Status.TELEPORT) return;
										
										for (Player playerOnline : Bukkit.getOnlinePlayers()) {
											
											DisplayUtils.sendActionBar(playerOnline, "§7Teleportation of '§6" + PlayerUtils.getName(PlayerUtils.getId((UUID) locationsList.get(i - 1).getKey().getValue())) + "§7' §8(§a" + i + "§7/§a" + locationsList.size() + "§8)");
											
										}
										
									}
									
								}
								
							}.runTaskTimer(Main.uhc, 5, 5);
							
							if (GameUtils.getStatus() != Status.TELEPORT) return;
							
							for (Player playerOnline : Bukkit.getOnlinePlayers()) {
								
								DisplayUtils.sendActionBar(playerOnline, "§a" + locationsList.size() + "§7 spawns successfully generated.");
								
							}
							
							Bukkit.broadcastMessage(Main.PREFIX + "Teleporting players...");
							
						}
						
					}.runTaskLater(Main.uhc, 20);
					
					return;
					
				}
				
				Location location = locationsList.get(i).getValue();
				
				location.getChunk().load(true);
			
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
				
				
				i++;
				
				if (GameUtils.getStatus() != Status.TELEPORT) return;
				
				for (Player playerOnline : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendActionBar(playerOnline, "§7Generation and loading of spawns... §8(§a" + i + "§7/§a" + locationsList.size() + "§8)");
					
				}
				
			}
			
		}.runTaskTimer(Main.uhc, 5, 5);
		
	}
	
	public static void removeSpawns(Map<UUID, Location> locations) {
		
		for (Location location : locations.values()) {
			
			Location loc1  = location.clone(), loc2  = location.clone(), loc3  = location.clone(), loc4 = location.clone();
			loc1.add(1, -1, 1);
			loc2.add(-1, -1, -1);
			loc3.add(2, 3, 2);
			loc4.add(-2, 0, -2);
			
			for (double x = loc2.getX(); x <= loc1.getX(); x++) {
				
				for (double y = loc2.getY(); y <= loc1.getY(); y++) {
				
					for (double z = loc2.getZ(); z <= loc1.getZ(); z++) {
						
						if (location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).getType().equals(Material.AIR)) continue;
						
						location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setType(Material.AIR, true);
						
					}
					
				}
			}
			
			for (double x = loc4.getX(); x <= loc3.getX(); x++) {
				
				for (double y = loc4.getY(); y <= loc3.getY(); y++) {
				
					for (double z = loc4.getZ(); z <= loc3.getZ(); z++) {
						
						if (location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).getType().equals(Material.AIR)) continue;
						
						location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setType(Material.AIR, true);
						
					}
					
				}
			}
			
		}
		
	}	
	
}

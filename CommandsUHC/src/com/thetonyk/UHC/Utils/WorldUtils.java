package com.thetonyk.UHC.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.thetonyk.UHC.MessengerListener;

public class WorldUtils {

	public static void loadWorld(String world) {
			
		if (!exist(world)) {
			
			return;
			
		}
			
		Environment environment = Environment.NORMAL;
		long seed = -89417720380802761l;
		WorldType type = WorldType.NORMAL;
		
		try {
			
			ResultSet worldDB = DatabaseUtils.sqlQuery("SELECT * FROM uhc_worlds WHERE name = '" + world + "' AND server = '" + MessengerListener.lastServer + "';");
			
			worldDB.next();
			
			environment = Environment.valueOf(worldDB.getString("environment"));
			seed = worldDB.getLong("seed");
			type = WorldType.valueOf(worldDB.getString("type"));
			
			worldDB.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[WorldUtils] Error to fetch informations of world " + world + " in DB.");
			return;
			
		}
		
		WorldCreator worldCreator = new WorldCreator(world);
		worldCreator.environment(environment);
		worldCreator.generateStructures(true);
		worldCreator.generatorSettings("{\"useMonuments\":false,\"graniteSize\":1,\"graniteCount\":0,\"graniteMinHeight\":0,\"graniteMaxHeight\":0,\"dioriteSize\":1,\"dioriteCount\":0,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":0,\"andesiteSize\":1,\"andesiteCount\":0,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":0}");
		worldCreator.seed(seed);
		worldCreator.type(type);
		
		World newWorld = worldCreator.createWorld();
		newWorld.setDifficulty(Difficulty.HARD);
		newWorld.save();
		
		return;
		
	}
	
	public static void unloadWorld(String world) {
		
		if (Bukkit.getWorld(world) == null) {
			
			Bukkit.getLogger().severe("[WorldUtils] The world " + world + " can't be unloaded.");
			return ;
			
		}
		
		Bukkit.unloadWorld(world, true);
		
	}
	
	public static void deleteWorld(String world) {
		
		if (!exist(world)) {
			
			Bukkit.getLogger().severe("[WorldUtils] The world §6" + world + " doesn't exist.");
			return;
			
		}
			
		if (Bukkit.getWorld(world) == null) {
			
			Bukkit.getLogger().severe("[WorldUtils] The world " + world + " can't be unloaded.");
			return;
			
		}

		for (Player player : Bukkit.getWorld(world).getPlayers()) player.teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
		
		WorldUtils.unloadWorld(world);
			
		if (FileUtils.delete(Bukkit.getWorld(world).getWorldFolder())) {
			
			DatabaseUtils.sqlInsert("DELETE FROM uhc_worlds WHERE name = '" + world + "' AND server = '" + MessengerListener.lastServer + "';");
			return;
			
		}
			
		Bukkit.getLogger().severe("[WorldUtils] The folder of world " + world + " can't be deleted.");
		
	}
	

	public static Boolean exist(String world) {
		
		Boolean exist = false;
		
		try {
			
			ResultSet name = DatabaseUtils.sqlQuery("SELECT * FROM uhc_worlds WHERE name = '" + world + "' AND server = '" + MessengerListener.lastServer + "';");
			
			if (name.next()) exist = true;
			
			name.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[WorldUtils] Error to check if world " + world + " exist.");
			
		}
		
		return exist;
		
	}
	
	public static void loadAllWorlds() {

		try {
			
			ResultSet worlds = DatabaseUtils.sqlQuery("SELECT * FROM uhc_worlds WHERE server = '" + MessengerListener.lastServer + "';");
			
			while (worlds.next()) {
				
				loadWorld(worlds.getString("name"));
				
			}
			
			worlds.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[WorldUtils] Error to fetch all worlds.");
			
		}
		
	}
	
	public static World createWorld(String world, Environment environment, long seed, WorldType type, int radius) {
		
		WorldCreator worldCreator = new WorldCreator(world);
		
		worldCreator.environment(environment);
		worldCreator.generateStructures(true);
		worldCreator.generatorSettings("{\"useMonuments\":false,\"graniteSize\":1,\"graniteCount\":0,\"graniteMinHeight\":0,\"graniteMaxHeight\":0,\"dioriteSize\":1,\"dioriteCount\":0,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":0,\"andesiteSize\":1,\"andesiteCount\":0,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":0}");
		worldCreator.seed(seed);
		worldCreator.type(type);
		
		World newWorld = worldCreator.createWorld();
		
		newWorld.setDifficulty(Difficulty.HARD);
		newWorld.setSpawnLocation(0, 200, 0);
		newWorld.getWorldBorder().setSize(radius);
		newWorld.getWorldBorder().setCenter(0, 0);
		newWorld.getWorldBorder().setDamageBuffer(0);
		newWorld.getWorldBorder().setDamageAmount(1);
		newWorld.getWorldBorder().setWarningDistance(15);
		newWorld.getWorldBorder().setWarningTime(1);
		newWorld.save();
		
		DatabaseUtils.sqlInsert("INSERT INTO uhc_worlds (`name`, `environment`, `seed`, `type`, `size`, `pregenned`, `server`) VALUES ('" + world + "', '" + environment.name() + "', '" + seed + "', '" + type.name() + "', '" + radius + "', 0, '" + MessengerListener.lastServer + "');");
		
		return newWorld;
		
	}
	
	public static int getHighestY (int x, int z, World world) {
		
		for (int y = 255; y >= 0; y--) {
			
			if (world.getBlockAt(x, y, z).getType() == Material.AIR) continue;
				
			return y;
			
		}
		
		return 255;
		
	}
	
}

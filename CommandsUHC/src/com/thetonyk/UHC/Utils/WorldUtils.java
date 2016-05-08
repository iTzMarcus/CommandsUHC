package com.thetonyk.UHC.Utils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
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
		
		File folder = Bukkit.getWorld(world).getWorldFolder();
		
		WorldUtils.unloadWorld(world);
			
		if (FileUtils.delete(folder)) {
			
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
				
				if (GameUtils.getWorld() != null && GameUtils.getWorld().equalsIgnoreCase(worlds.getString("name"))) continue;
				
				Bukkit.getWorld(worlds.getString("name")).setPVP(false);
				Bukkit.getWorld(worlds.getString("name")).setTime(6000);
				Bukkit.getWorld(worlds.getString("name")).save();
				
			}
			
			worlds.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[WorldUtils] Error to fetch all worlds.");
			
		}
		
		Bukkit.getWorld("lobby").setPVP(false);
		Bukkit.getWorld("lobby").setTime(6000);
		Bukkit.getWorld("lobby").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("lobby").setSpawnFlags(false, false);
		Bukkit.getWorld("lobby").setWeatherDuration(0);
		Bukkit.getWorld("lobby").setDifficulty(Difficulty.PEACEFUL);
		Bukkit.getWorld("lobby").save();
		
		
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
		newWorld.setGameRuleValue("doDaylightCycle", "false");
		newWorld.setSpawnFlags(false, true);
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
	
	public static int getSize(String world) {
		
		int radius = 0;
		
		try {
			
			ResultSet worldDB = DatabaseUtils.sqlQuery("SELECT * FROM uhc_worlds WHERE name='" + world + "' AND server = '" + MessengerListener.lastServer + "';");
			
			if (worldDB.next()) radius = worldDB.getInt("size");
			
			worldDB.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[BorderCommand] Error to fetch informations of world " + world + " in DB.");
			
		}
		
		return radius;
		
	}
	
	public static int butcher (World world) {
		
		int count = 0;
		
		for (Entity entity : world.getEntities()) {
			
			switch (entity.getType()) {
			
				case ARMOR_STAND:
				case ARROW:
				case BOAT:
				case CHICKEN:
				case COMPLEX_PART:
				case COW:
				case DROPPED_ITEM:
				case EGG:
				case ENDER_CRYSTAL:
				case ENDER_DRAGON:
				case ENDER_PEARL:
				case ENDER_SIGNAL:
				case EXPERIENCE_ORB:
				case FALLING_BLOCK:
				case FIREWORK:
				case FISHING_HOOK:
				case HORSE:
				case IRON_GOLEM:
				case ITEM_FRAME:
				case LEASH_HITCH:
				case LIGHTNING:
				case MINECART:
				case MINECART_CHEST:
				case MINECART_COMMAND:
				case MINECART_FURNACE:
				case MINECART_HOPPER:
				case MINECART_MOB_SPAWNER:
				case MINECART_TNT:
				case MUSHROOM_COW:
				case OCELOT:
				case PAINTING:
				case PIG:
				case PLAYER:
				case PRIMED_TNT:
				case RABBIT:
				case SHEEP:
				case SNOWBALL:
				case SNOWMAN:
				case SPLASH_POTION:
				case SQUID:
				case THROWN_EXP_BOTTLE:
				case UNKNOWN:
				case VILLAGER:
				case WEATHER:
				case WOLF:
					continue;
				default:
					break;
			
			}
			
			entity.remove();
			count++;
			
		}
		
		return count;
		
	}
	
}

package com.thetonyk.UHC.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.NameTagVisibility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PlayerUtils {
	
	public static void clearInventory(Player player) {
		
        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);
        player.setItemOnCursor(new ItemStack(Material.AIR));

        InventoryView openedInventory = player.getOpenInventory();
        
        if (openedInventory.getType() == InventoryType.CRAFTING)  openedInventory.getTopInventory().clear();
        
    }
	
	public static void clearXp(Player player) {
	
		player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0F);
		
	}
	
	public static void feed(Player player) {
		
        player.setSaturation(5.0F);
        player.setExhaustion(0F);
        player.setFoodLevel(20);
        
    }
	
	public static void heal(Player player) {
		
		player.setHealth(player.getMaxHealth());
		
	}
	
	public static void clearEffects(Player player) {
		
		Collection<PotionEffect> activeEffects = player.getActivePotionEffects();

        for (PotionEffect activeEffect : activeEffects) {
        	
            player.removePotionEffect(activeEffect.getType());
            
        }
		
	}
	
	public static void leaveUpdatePlayer(Player player) {
		
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			if (online.getScoreboard().getTeam(player.getName()) == null) continue;
				
			online.getScoreboard().getTeam(player.getName()).unregister();
			
		}
		
	}
	
	public enum Rank {
		
		PLAYER("", "§7Player"), WINNER("§6Winner §8| ", "§6Winner"), FAMOUS("§bFamous §8| ", "§bFamous"), BUILDER("§2Build §8| ", "§2Builder"),STAFF("§cStaff §8| ", "§cStaff"), MOD("§9Mod §8| ", "§9Moderator"), ADMIN("§4Admin §8| ", "§4Admin"), FRIEND("§3Friend §8| ", "§3Friend"), HOST("§cHost §8| ", "§cHost"), ACTIVE_BUILDER("§2Build §8| ", "§2Builder");
		
		String prefix;
		String name;
		
		private Rank(String prefix, String name) {
			
			this.prefix = prefix;
			this.name = name;
			
		}
		
		public String getPrefix() {
			
			return prefix;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
	}
	
	public static void setRank(String player, Rank rank) {
		
		DatabaseUtils.sqlInsert("UPDATE users SET rank = '" + rank + "' WHERE name = '" + player + "';");
		
		if (Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player).isOnline()) {
			
			PermissionsUtils.clearPermissions(Bukkit.getPlayer(player));
			PermissionsUtils.setPermissions(Bukkit.getPlayer(player));
			PermissionsUtils.updateBungeePermissions(Bukkit.getPlayer(player));
		
		}
		
	}
	
	public static Rank getRank(String player) {
		
		Rank rank = Rank.PLAYER;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT rank FROM users WHERE name = '" + player + "';");
			
			if (req.next()) rank = Rank.valueOf(req.getString("rank"));
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get the rank of player " + player + ".");
			
		}
		
		return rank;
		
	}
	
	public static String getRanks() {
		
		String list = "Availables ranks: §aplayer";
		
		for (Rank rank : Rank.values()) {
			
			if (!rank.name().toLowerCase().equalsIgnoreCase("player")) list = list + " §7| §a" + rank.name().toLowerCase();
			
		}
		
		list = list + "§7.";
		
		return list;
		
	}
	
	public static void updateNametag(String player) {
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			if (players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()) == null) {
				
				players.getScoreboard().registerNewTeam(Bukkit.getPlayer(player).getName());
	
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setAllowFriendlyFire(true);
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setCanSeeFriendlyInvisibles(true);
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setDisplayName(player + " team");
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
				
			}
			
			if (TeamsUtils.getTeam(Bukkit.getPlayer(player).getName()) == null) {
		
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setPrefix(getRank(Bukkit.getPlayer(player).getName()).getPrefix() + "§7");
			
			} else {
				
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setPrefix(getRank(Bukkit.getPlayer(player).getName()).getPrefix() + TeamsUtils.getTeamPrefix(Bukkit.getPlayer(player).getName()) + "§7");
				
			}
			
			players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setSuffix("§f");
			
			players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).addEntry(Bukkit.getPlayer(player).getName());
			
			if (Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()) == null) {
				
				Bukkit.getPlayer(player).getScoreboard().registerNewTeam(players.getName());
				
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setAllowFriendlyFire(true);
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setCanSeeFriendlyInvisibles(true);
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setDisplayName(players.getName() + " team");
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
				
			}
			
			if (TeamsUtils.getTeam(Bukkit.getPlayer(player).getName()) == null) {
				
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setPrefix(getRank(players.getName()).getPrefix() + "§7");
			
			} else {
				
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setPrefix(getRank(players.getName()).getPrefix() + TeamsUtils.getTeamPrefix(players.getName()) + "§7");
				
			}
			
			Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setSuffix("§f");
			
			Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).addEntry(players.getName());
		
		}
		
	}
	
	public static int getHighestY (int x, int z, World world) {
		
		for (int y = 255; y >= 0; y--) {
			
			if (world.getBlockAt(x, y, z).getType() == Material.AIR) continue;
				
			return y;
			
		}
		
		return 255;
		
	}
	
	public static int getId (String name) {
		
		int id = 0;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT id FROM users WHERE name ='" + name + "';");
			
			if (req.next()) id = req.getInt("id");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get id of player " + name + ".");
			
		}
		
		return id;
		
	}
	
	public static int getId (UUID uuid) {
		
		int id = 0;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT id FROM users WHERE uuid ='" + uuid + "';");
			
			if (req.next()) id = req.getInt("id");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get id of player with UUID " + uuid + ".");
			
		}
		
		return id;
		
	}
	
	public static int getChatVisibility (Player player) {
		
		int chatVisibility = 0;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT chat FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) chatVisibility = req.getInt("chat");
			
			req.close();

		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get chat setting of player " + player.getName() + ".");
			
		}
		
		return chatVisibility;
		
	}
	
	public static int getMentionsState (Player player) {
		
		int mentionsState = 0;
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT mentions FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) mentionsState = req.getInt("mentions");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get mentions setting of player " + player.getName() + ".");
			
		}
		
		return mentionsState;
		
	}
	
	public static List<UUID> getIgnoredPlayers (UUID player) {
		
		String ignoredPlayers = "";
		
		try {
			
			ResultSet req = DatabaseUtils.sqlQuery("SELECT ignored FROM settings WHERE id = " + PlayerUtils.getId(player) + ";");
			
			if (req.next()) ignoredPlayers = req.getString("ignored");
			
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get ignored players of player with UUID " + player + ".");
			
		}
		
		List<UUID> ignored = ignoredPlayers == "" ? new ArrayList<UUID>() : new Gson().fromJson(ignoredPlayers, new TypeToken<List<UUID>>(){}.getType());
		return ignored;
		
	}

}

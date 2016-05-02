package com.thetonyk.UHC.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Utils.PlayerUtils.Rank;

public class PermissionsUtils {
	
	private static Map<String, PermissionAttachment> permissions = new HashMap<String, PermissionAttachment>();
	
	public static void setPermissions(Player player) {
		
		if (!permissions.containsKey(player.getName())) permissions.put(player.getName(), player.addAttachment(Main.uhc));
		
		PermissionAttachment permission = permissions.get(player.getName());
		Rank rank = PlayerUtils.getRank(player.getName());
		
		if (rank == Rank.ADMIN) {
			
			player.setOp(true);
			return;
			
		}
		
		permission.setPermission("uhc.border", true);
		permission.setPermission("uhc.team", true);
		permission.setPermission("uhc.helpop", true);
		
		if (rank == Rank.PLAYER || rank == Rank.WINNER) return;
		
		permission.setPermission("global.fly", true);
		
		if (rank == Rank.FAMOUS || rank == Rank.FRIEND) return;
		
		permission.setPermission("global.bypasswhitelist", true);
		
		if (rank == Rank.BUILDER) return;
		
		permission.setPermission("global.build", true);
		permission.setPermission("global.gamemode", true);
		permission.setPermission("parkour.jump", true);
		
		if (rank == Rank.ACTIVE_BUILDER) return;
		
		permission.setPermission("global.gamemode", false);
		permission.setPermission("global.build", false);
		permission.setPermission("parkour.jump", false);
		permission.setPermission("uhc.pregen", true);
		permission.setPermission("uhc.whitelist.add", true);
		
		if (rank == Rank.HOST) return;
		
		permission.setPermission("uhc.pregen", false);
		
		if (rank == Rank.MOD) return;
		
		permission.setPermission("uhc.pregen", true);
		permission.setPermission("uhc.broadcast", true);
		permission.setPermission("uhc.helpop.see", true);
		permission.setPermission("uhc.warning", true);
		
	}
	
	public static void clearPermissions(Player player) {
		
		if (!permissions.containsKey(player.getName())) return;
			
		player.removeAttachment(permissions.get(player.getName()));
		permissions.remove(player.getName());
		
	}
	
	public static void updateBungeePermissions(Player player) {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("updatePermissions");
		out.writeUTF(player.getUniqueId().toString());
		
		player.sendPluginMessage(Main.uhc, "CommandsBungee", out.toByteArray());
		
	}

}

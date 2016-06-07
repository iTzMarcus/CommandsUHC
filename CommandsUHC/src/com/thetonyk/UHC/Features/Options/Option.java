package com.thetonyk.UHC.Features.Options;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.thetonyk.UHC.Features.Exceptions.IdenticalStatesException;

public abstract class Option {
	
	private static Boolean state = false;
	protected static String name;
	protected static ItemStack icon;
	
	public static ItemStack getIcon() {
		
		ItemStack item = icon.clone();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§8⫸ §6" + name + " §8⫷");
		item.setItemMeta(meta);
		
		return item;
		
	}
	
	public static void enable() throws IdenticalStatesException {
		
		if (state) {
			
			throw new IdenticalStatesException("This feature is already enabled.");
			
		}
		
		state = true;
		onEnable();
		
	}
	
	public static void disable() throws IdenticalStatesException {
		
		if (!state) {
			
			throw new IdenticalStatesException("This feature is already disabled.");
			
		}
		
		state = false;
		onDisable();
		
	}
	
	public Boolean enabled() {
		
		return state;
		
	}
	
	public static void onEnable() {};
	public static void onDisable() {};

}

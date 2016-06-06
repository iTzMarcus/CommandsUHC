package com.thetonyk.UHC.Features;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.thetonyk.UHC.Features.Exceptions.IdenticalStatesException;

public abstract class Option {
	
	private Boolean state = false;
	private String name;
	private ItemStack icon;
	
	public Option(String name, ItemStack icon) {
		
		this.name = name;
		this.icon = icon;
		
	}
	
	public ItemStack getIcon() {
		
		ItemStack icon = this.icon.clone();
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName("§8⫸ §6" + this.name + " §8⫷");
		icon.setItemMeta(meta);
		
		return icon;
		
	}
	
	public void enable() throws IdenticalStatesException {
		
		if (this.state) {
			
			throw new IdenticalStatesException("This feature is already enabled.");
			
		}
		
		this.state = true;
		
	}
	
	public void disable() throws IdenticalStatesException {
		
		if (!this.state) {
			
			throw new IdenticalStatesException("This feature is already disabled.");
			
		}
		
		this.state = false;
		
	}
	
	public Boolean enabled() {
		
		return state;
		
	}

}

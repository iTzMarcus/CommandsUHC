package com.thetonyk.UHC.Utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class ItemsUtils {
	
	public static ItemStack createItem(Material material, String name, int number, int damage) {
		
		ItemStack item = new ItemStack(material, number, (short) damage);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		
		return item;
		
	}
	
	public static ItemStack createItem(Material material, String name, int number, int damage, List<String> lore) {
		
		ItemStack item = new ItemStack(material, number, (short) damage);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
		
	}
	
	public static ItemStack addGlow(ItemStack item) {
		
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        
        NBTTagCompound tag = null;
        
        if (!nmsStack.hasTag()) {
        	
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
            
        }
        
        if (tag == null) tag = nmsStack.getTag();
        
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
        
    }
	
	public static ItemStack hideFlags(ItemStack item) {
		
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		
		return item;
		
	}
	
	public static ItemStack getSkull(String name, String texture) {
		
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (texture.isEmpty()) {
			return head;
		}
		
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("texture", new String(texture)));
		Field profileField = null;
		
		try {
			
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
			
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
			
			e1.printStackTrace();
			
		}
		
		headMeta.setDisplayName(name);
		head.setItemMeta(headMeta);
		return head;
		
	}

}

package com.thetonyk.UHC.Utils;

import java.io.File;

import org.bukkit.Bukkit;

public class FileUtils {
	
	public static boolean delete(File file) {
		
		if (file.exists()) {
			
			File[] files = file.listFiles();
			
			for (int i = 0; i < files.length; i++) {
				
				if (files[i].isDirectory()) delete(files[i]);
				else files[i].delete();
				
			}
			
		}
		
		if (file.isDirectory()) {

			      if (file.list().length > 0) Bukkit.getLogger().severe("The " + file.getPath() + " is not empty!");
			      
		}
		
		return (file.delete());
		
	}

}

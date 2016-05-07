package com.thetonyk.UHC.Utils;

import net.minecraft.server.v1_8_R3.BiomeBase;

public class BiomesUtils {
	
	private static BiomeBase[] biomesCopy = BiomeBase.getBiomes().clone();
	
	private static void removeOceans() {
		
		BiomeBase[] biomesCopy = BiomeBase.getBiomes().clone();
		
		BiomeBase.getBiomes()[0] = biomesCopy[1];
		BiomeBase.getBiomes()[10] = biomesCopy[1];
		BiomeBase.getBiomes()[24] = biomesCopy[4];
		
	}
	
	private static void removeJungles() {
		
		BiomeBase[] biomesCopy = BiomeBase.getBiomes().clone();
		
		BiomeBase.getBiomes()[21] = biomesCopy[23];
		BiomeBase.getBiomes()[22] = biomesCopy[23];
		
	}
	
	public static void removeOceansAndJungles() {
		
		removeOceans();
		removeJungles();
		
	}
	
	public static void resetBiomes() {
		
		for (int i = 0; i < 40; i++) {
			
			BiomeBase.getBiomes()[i] = biomesCopy[i]; 
			
		}
		
	}

}

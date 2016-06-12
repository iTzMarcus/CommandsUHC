package com.thetonyk.UHC.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import com.thetonyk.UHC.Main;

public class MatchesUtils {
	
	public static void getUpcomingMatches(Callback<List<Long>> callback) {
	
		new BukkitRunnable() {
			
			public void run() {
				
				String rawJson = null;
				
				try {
					
					URL url = new URL("https://c.uhc.gg/api/v2/r/uhcmatches");
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					StringBuffer buffer = new StringBuffer();
					int read;
					char[] chars = new char[1024];
					
					while ((read = reader.read(chars)) != -1) {
						
						buffer.append(chars, 0, read);
						
					}
					
					rawJson = buffer.toString();
					
					if (reader != null) reader.close();
					
				} catch (Exception exception) {
					
					exception.printStackTrace();
					callback.onFailure();
					
				}
				
				List<Long> matches = new ArrayList<Long>();
				JSONArray json = new JSONArray(rawJson);
		
				for (int i = 0; i < json.length(); i++) {
					
					JSONObject match = json.getJSONObject(i);
					
					if (!match.getString("region").equalsIgnoreCase("EU")) continue;
					
					matches.add(match.getLong("opens"));
					
				}
				
				callback.onSuccess(matches);
				
			}
		
		}.runTaskAsynchronously(Main.uhc);
	
	}

	public interface Callback<T> {
		
		void onSuccess(T done);
		void onFailure();
		
	}
	
}

package com.thetonyk.UHC.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import com.thetonyk.UHC.Main;

public class MatchesUtils {
	
	public static void getUpcomingMatches(MatchesCallback<List<Match>> callback) {
	
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
				
				List<Match> matches = new ArrayList<Match>();
				long time = new Date().getTime() / 1000;
				JSONArray json = new JSONArray(rawJson);
		
				for (int i = 0; i < json.length(); i++) {
					
					JSONObject match = json.getJSONObject(i);
					
					if (!match.getString("region").equalsIgnoreCase("EU")) continue;
					
					if (match.getLong("opens") < time) continue;
					
					String[] scenarios = new String[match.getJSONArray("gamemodes").length()];
					
					for (int y = 0; y < scenarios.length; y++) {
						
						scenarios[y] = match.getJSONArray("gamemodes").getString(y);
						
					}
					
					matches.add(new Match(match.getLong("opens"), scenarios, match.getInt("teamSize")));
					
				}
				
				callback.onSuccess(matches);
				
			}
		
		}.runTaskAsynchronously(Main.uhc);
	
	}

	public interface MatchesCallback<T> {
		
		void onSuccess(T done);
		void onFailure();
		
	}
	
	public static class Match {
	
		private long time;
		private String[] scenarios;
		private int teamSize;
		
		public Match(long time, String[] scenarios, int teamSize) {
			
			this.time = time;
			this.scenarios = scenarios;
			this.teamSize = teamSize;
			
		}
		
		public long getTime() {
			
			return this.time;
			
		}

		public String[] getScenarios() {
			
			return this.scenarios;
			
		}
		
		public int getTeamSize() {
			
			return this.teamSize;
			
		}
		
	}
	
}

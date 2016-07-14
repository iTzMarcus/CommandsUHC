package com.thetonyk.UHC.Inventories;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.UHC.Main;
import com.thetonyk.UHC.Features.SpecInfo;
import com.thetonyk.UHC.GUI.NumberGUI;
import com.thetonyk.UHC.GUI.NumberGUI.NumberCallback;
import com.thetonyk.UHC.GUI.SignGUI;
import com.thetonyk.UHC.GUI.SignGUI.SignCallback;
import com.thetonyk.UHC.Utils.GameUtils.TeamType;
import com.thetonyk.UHC.Utils.ItemsUtils;
import com.thetonyk.UHC.Utils.MatchesUtils;
import com.thetonyk.UHC.Utils.MatchesUtils.Match;
import com.thetonyk.UHC.Utils.MatchesUtils.MatchesCallback;

public class GameInventory implements Listener {
	
	private static String TITLE_PREFIX = "§8⫸ §4Host§8: §7";
	private Inventory inventory;
	private String hour;
	private long time;
	private SimpleDateFormat format;
	private Page page;
	private TeamType teamType;
	private int teamSize;
	private int slots;
	
	public GameInventory() {
		
		this.page = Page.BASIC;
		this.inventory = Bukkit.createInventory(null, this.page.getSize(), TITLE_PREFIX + this.page.getName());
		this.hour = null;
		this.time = 0;
		this.format = new SimpleDateFormat("hh:MM");
		this.format.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.teamType = null;
		this.teamSize = 1;
		this.slots = 150;
		
		update();
		
		Bukkit.getPluginManager().registerEvents(this, Main.uhc);
		
	}
	
	public Inventory getInventory() {
		
		return inventory;
		
	}
	
	private void update() {
		
		update(null);
		
	}
	
	private void update(List<Match> uhcs) {
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7UHC by CommandsPVP", 1, 7);
		
		for (int i = 0; i < inventory.getSize(); i++) {
			
			inventory.setItem(i, separator);
			
		}
		
		List<String> lore = new ArrayList<String>();
		
		switch (this.page) {
			
			case BASIC:
				
				ItemStack team = ItemsUtils.createItem(Material.BANNER, "§8⫸ §7Teams: §6" + (this.teamType == null ? "FFA" : SpecInfo.formatName(this.teamType.toString()) + (this.teamSize > 1 ? " Team of " + this.teamSize : "")), (this.teamSize > 1 ? this.teamSize : 1), this.teamType == null ? 1 : 10);
				ItemStack slots = ItemsUtils.createItem(Material.SKULL_ITEM, "§8⫸ §7Slots: §6" + this.slots, (int) Math.floor(this.slots / 10), 3);
				
				this.inventory.setItem(11, team);
				this.inventory.setItem(12, slots);
				break;
			case TEAMS:
				
				ItemStack ffa = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ " + (this.teamType == null ? "§a" : "§c") + "FFA", 1, 4);
				ItemStack auction = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ " + (this.teamType == TeamType.AUCTION ? "§a" : "§c") + "Auction", 1, 4);
				
				if (this.teamType == null) ffa = ItemsUtils.addGlow(ffa);
				if (this.teamType == TeamType.AUCTION) auction = ItemsUtils.addGlow(auction);
				
				this.inventory.setItem(10, ffa);
				this.inventory.setItem(11, auction);
				
				int slot = 12;
				
				for (int i = 2; i <= 20; i++) {
					
					while (slot % 9 == 0 || slot % 9 == 8) {
						
						slot++;
						
					}
					
					ItemStack item = ItemsUtils.createItem(Material.BANNER, "§8⫸ §7Team of " + (this.teamSize == i ? "§a" : "§c") + i, 1, this.teamSize == i ? 10 : 1);
					if (this.teamSize == i) item = ItemsUtils.addGlow(item);
					
					this.inventory.setItem(slot, item);
					slot++;
					
				}
				
				slot = this.inventory.getSize() - 7;
				
				for (TeamType type : TeamType.values()) {
					
					if (type == TeamType.AUCTION) continue;
						
					ItemStack item = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §7Type: " + (this.teamType == type ? "§a" : "§c") + SpecInfo.formatName(type.toString()), 1, this.teamType == type ? 3 : 11);
					if (this.teamType == type) item = ItemsUtils.addGlow(item);
					
					this.inventory.setItem(slot, item);
					slot++;
				
				}
				
				break;
			case SCHEDULE:
				
				Boolean valid = true;
				
				if (time < 1 && this.hour != null) {
					
					lore.add(" ");
					lore.add("§8⫸ §7Last entered hour: §c" + hour);
					lore.add(" ");
					
					if (!isValid(this.hour)){
						
						lore.add("§8⫸ §7This hour is not valid.");
						lore.add(" ");
						valid = false;
						
					} else {
						
						long parsedTime = getTime(this.hour);
						Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
						calendar.setTimeInMillis(parsedTime);
						int minutes = calendar.get(Calendar.MINUTE);
						
						if (minutes != 0 && minutes != 15 && minutes != 30 && minutes != 45) {
							
							lore.add("§8⫸ §7You can only schedule UHC at these times:");
							lore.add("§8⫸ §6XX:00 §8| §6XX:15 §8| §6XX:30 §8| §6XX:45");
							lore.add(" ");
							valid = false;
							
						}
						
						if ((parsedTime - 1800000) < new Date().getTime()) {
							
							lore.add("§8⫸ §7UHC must be scheduled at least 30min before.");
							lore.add(" ");
							valid = false;
							
						}
						
						if (uhcs == null) {
							
							lore.add(" ");
							lore.add("§8⫸ §7Checking others UHC on Reddit...");
							lore.add(" ");
							valid = false;
							
						}
						else {
							
							for (Match uhc : uhcs) {
								
								if (parsedTime != uhc.getTime() * 1000) continue;
								
								lore.add("§8⫸ §cAn UHC is already scheduled at this hour.");
								lore.add(" ");
								valid = false;
								break;
								
							}
							
						}
						
					}
					
					lore.add(" ");
					
				}
				
				if (valid && this.hour != null) {
					
					time = getTime(this.hour);
					lore.clear();
					
				}
				
				ItemStack hour = ItemsUtils.createItem(Material.WATCH, "§8⫸ §7Hour: §6" + (time < 1 ? "§cNone" : format.format(new Date(time))), 1, 0, lore);
				lore.clear();
				
				inventory.setItem(11, hour);
				break;
			
		}
		
		
		ItemStack cancel = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §c" + (this.page.main() == null ? "Cancel" : "Back"), 1, 14);
		ItemStack valid = ItemsUtils.createItem(Material.STAINED_CLAY, "§8⫸ §aNext", 1, 5);
		
		this.inventory.setItem(this.inventory.getSize() - 9, cancel);
		if (this.page.main() == null) this.inventory.setItem(this.inventory.getSize() - 1, valid);
		
	}
	
	@Override
	protected void finalize() {
		
		cancel();
		
	}
	
	private void cancel() {
		
		HandlerList.unregisterAll(this);
		
		for (HumanEntity viewer : new ArrayList<HumanEntity>(inventory.getViewers())) {
			
			viewer.closeInventory();
			
		}
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (!event.getInventory().equals(this.inventory)) return;
		
		event.setCancelled(true);
		
		ItemStack item = event.getCurrentItem();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
		
		switch (this.page) {
		
			case BASIC:
				
				if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Teams: §6")) {
					
					changePage(Page.TEAMS);
					return;
					
				}
				
				if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Slots: §6")) {
					
					
					NumberGUI gui = new NumberGUI("Slots", this.slots, 10, 1, 150, 200, 5, new NumberCallback<Integer>() {
						
						@Override
						public void onConfirm(int newSlots) {
							
							slots = newSlots;
							update();
							player.openInventory(inventory);
							
						}
						
						@Override
						public void onCancel() {
							
							player.openInventory(inventory);
							
						}
						
						@Override
						public void onDisconnect() {}
						
					});
					
					player.openInventory(gui.getInventory());
					return;
					
				}
			
				break;
			case TEAMS:
				
				if (item.getItemMeta().getDisplayName().endsWith("FFA")) {
					
					this.teamType = null;
					this.teamSize = 1;
					update();
					return;
					
				}
				
				if (item.getItemMeta().getDisplayName().endsWith("Auction")) {
					
					this.teamType = TeamType.AUCTION;
					this.teamSize = 0;
					update();
					return;
					
				}

				if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Team of ")) {
					
					int size = 0;
					String[] name = item.getItemMeta().getDisplayName().split(" ");
					
					try {
						
						size = Integer.parseInt(name[name.length - 1].substring(2));
						
					} catch (Exception exception) {
						
						return;
						
					}
					
					if (this.teamType == null || this.teamType == TeamType.AUCTION) this.teamType = TeamType.CHOSEN;
					
					this.teamSize = size;
					update();
					return;
					
				}
				
				if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Type: ")) {
					
					TeamType type = null;
					String[] name = item.getItemMeta().getDisplayName().split(" ");
					
					try {
						
						type = TeamType.valueOf(name[name.length - 1].substring(2).toUpperCase());
						
					} catch (Exception exception) {
						
						break;
						
					}
					
					if (this.teamSize < 2) this.teamSize = 2;
					
					this.teamType = type;
					update();
					return;
					
				}
				
				break;
			case SCHEDULE:
	
				if (item.getItemMeta().getDisplayName().startsWith("§8⫸ §7Hour: §6")) {
					
					String[] text = new String[time < 1 && this.hour == null ? 0 : 1];
					if (text.length > 0) text[0] = this.hour;
					
					new SignGUI(player, text, new SignCallback<String[]>() {
		
						@Override
						public void onConfirm(String[] lines) {
							
							new BukkitRunnable() {
								
								public void run() {
									
									player.openInventory(getInventory());
								
								}
								
							}.runTaskLater(Main.uhc, 1);
							
							if (lines[0].length() + lines[1].length() + lines[2].length() + lines[3].length() < 1) return;
							
							String text = null;
							
							for (String line : lines) {
								
								if (line.length() < 1) continue;
								
								text = line;
								break;
								
							}
							
							hour = text;
							update();
							
							MatchesUtils.getUpcomingMatches(new MatchesCallback<List<Match>>() {
		
								@Override
								public void onSuccess(List<Match> done) {
									
									update(done);
									
								}
		
								@Override
								public void onFailure() {}
								
							});
							
						}
		
						@Override
						public void onDisconnect() {}
						
					});
					
					return;
					
				}
				
				break;
		}
		
		if (item.getItemMeta().getDisplayName().equals("§8⫸ §cBack")) {
			
			changePage(this.page.main());
			
		}
		
	}
	
	private Boolean isValid(String hour) {
		
		Pattern pattern = Pattern.compile("^[0-9]{1,2}(:|h|H)[0-9]{0,2}$");
		Matcher matcher = pattern.matcher(hour);
		
		return matcher.matches();
		
	}
	
	private long getTime(String hour) {
		
		Pattern pattern = Pattern.compile("(:|h|H)");
		Matcher matcher = pattern.matcher(hour);
		
		int hours = 0;
		int minutes = 0;
		
		while (matcher.find()) {
			
			hours = Integer.parseInt(hour.substring(0, matcher.start()));
			if (hour.length() > matcher.start() + 1) minutes = Integer.parseInt(hour.substring(matcher.start() + 1));
			break;
			
		}
		
		SimpleDateFormat format = new SimpleDateFormat("dd MM HH:mm zzz");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		if (calendar.getTimeInMillis() < new Date().getTime()) calendar.set(Calendar.DAY_OF_MONTH, new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ENGLISH).get(Calendar.DAY_OF_MONTH) + 1);
		
		return calendar.getTimeInMillis();
		
	}
	
	private void changePage(Page page) {
		
		List<HumanEntity> viewers = new ArrayList<HumanEntity>(this.inventory.getViewers());
		
		this.inventory = Bukkit.createInventory(null, page.getSize(), TITLE_PREFIX + page.getName());
		this.page = page;
		
		for (final HumanEntity viewer : viewers) {
			
			viewer.closeInventory();
			
			new BukkitRunnable() {
				
				public void run() {
					
					viewer.openInventory(inventory);
					
				}
				
			}.runTaskLater(Main.uhc, 1);
			
		}
		
		update();
		
	}
	
	private enum Page {
		
		BASIC("Config", 27, null), TEAMS("Teams", 54, Page.BASIC), SCHEDULE("Schedule", 27, null);
		
		private String name;
		private int size;
		private Page main;
		
		private Page(String name, int size, Page main) {
			
			this.name = name;
			this.size = size;
			this.main = main;
			
		}
		
		public String getName() {
			
			return this.name;
			
		}
		
		public int getSize() {
			
			return this.size;
			
		}
		
		public Page main() {
			
			return this.main;
			
		}
		
	}

}

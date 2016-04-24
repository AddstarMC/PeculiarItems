package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import au.com.mineauz.peculiaritems.Data;
import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;
import au.com.mineauz.peculiaritems.PCRPlayer;
import au.com.mineauz.peculiaritems.PeculiarObject;

public abstract class PeculiarStat implements Listener{
	
	public abstract String getName();
	public abstract ChatColor getDisplayColor();
	public abstract String getDisplayName();
	public abstract boolean isCompatibleItem(String type);
	
	public String toDisplayString(int value) {
		return getDisplayColor() + getDisplayName() + ": " + value; 
	}
	
	public void incrementStat(PCRPlayer player, PeculiarObject item, int amount){
		ItemMeta meta = item.getItem().getItemMeta();
		
		int line = getStatLine(item);
		int inc = getStatLevel(item, line);
		
		inc += amount;
		
		addStat(item, inc, line);
		meta = item.getItem().getItemMeta();
		
		Data data = PeculiarItemsPlugin.getPlugin().getData();
		
		if(data.hasRank(inc) && item.isPrimaryLevelStat(this)){
			String rank = data.getRank(inc);
			
			meta.setDisplayName(ChatColor.RESET.toString() + ChatColor.GOLD + rank + " " + 
					ChatColor.RESET + ChatColor.GOLD + item.getItemName());
			if(PeculiarItemsPlugin.getPlugin().isBroadcastingRankUp()){
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getDisplayName() + "'s " + 
						item.getItemName() + " has reached a new rank: " + getDisplayColor() + rank);
			}
			else{
				player.sendMessage(ChatColor.AQUA + "Your " + 
						item.getItemName() + " has reached a new rank: " + getDisplayColor() + rank);
			}
		}
		
		item.getItem().setItemMeta(meta);
	}
	
	public void addStat(PeculiarObject item, int amount, int line){
		ItemMeta meta = item.getItem().getItemMeta();
		List<String> lore = meta.getLore();
		
		if(line != -1){
			lore.set(line, getDisplayColor() + getDisplayName() + ": " + amount);
		}
		else{
			lore.add(getDisplayColor() + getDisplayName() + ": " + amount);
		}
		
		meta.setLore(lore);
		item.getItem().setItemMeta(meta);
	}
	
	public int getStatLine(PeculiarObject item){
		int line = -1;
		List<String> lore = item.getItem().getItemMeta().getLore();
		for(String l : lore){
			if(ChatColor.stripColor(l).matches(getDisplayName() + ": " + "[0-9]+")){
				line = item.getItem().getItemMeta().getLore().indexOf(l);
				break;
			}
		}
		
		return line;
	}
	
	public int getStatLevel(PeculiarObject item, int loreLine){
		ItemMeta meta = item.getItem().getItemMeta();
		return Integer.valueOf(ChatColor.stripColor(
				meta.getLore().get(loreLine)).replace(getDisplayName() + ": ", ""));
	}
	
	public final void registerEvents(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public final void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}
}

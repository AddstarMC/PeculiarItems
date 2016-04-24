package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import au.com.mineauz.peculiaritems.Data;
import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;
import au.com.mineauz.peculiaritems.PCRPlayer;

public abstract class PeculiarStat implements Listener{
	
	public abstract String getName();
	public abstract ChatColor getDisplayColor();
	public abstract String getDisplayName();
	public abstract boolean isCompatibleItem(ItemStack item);
	
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
}

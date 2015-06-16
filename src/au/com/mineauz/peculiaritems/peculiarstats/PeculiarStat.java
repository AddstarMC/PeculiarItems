package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRPlayer;
import au.com.mineauz.peculiaritems.PeculiarObject;

public abstract class PeculiarStat {
	
	public abstract String getName();
	public abstract ChatColor getDisplayColor();
	public abstract String getDisplayName();
	public abstract boolean isCompatibleItem(String type);
	
	public List<String> getRankNames(){
		List<String> ls = new ArrayList<String>();
		ls.add("Very Peculiar");
		ls.add("Strangely Peculiar");
		ls.add("Mildly Interesting");
		ls.add("Pretty Cool");
		ls.add("Awesome");
		ls.add("Amazing");
		ls.add("Shiny");
		ls.add("Sparkling");
		ls.add("Unbelevable");
		ls.add("Super");
		ls.add("Ultimate");
		ls.add("Herobrine's Own");
		return ls;
	}
	
	public void incrementStat(PCRPlayer player, PeculiarObject item, int amount){
		ItemMeta meta = item.getItem().getItemMeta();
		
		int line = getStatLine(item);
		int inc = getStatLevel(item, line);
		
		inc += amount;
		
		addStat(item, inc, line);
		meta = item.getItem().getItemMeta();
		
		List<Integer> rankValues = Main.getPlugin().getRankValues();
		
		if(rankValues.contains(inc) && item.isPrimaryLevelStat(this)){
			List<String> ranks = getRankNames();
			int ind = rankValues.indexOf(inc);
			if(ind >= ranks.size())
				ind = ranks.size() - 1;
			
			meta.setDisplayName(ChatColor.RESET.toString() + ChatColor.GOLD + ranks.get(ind) + " " + 
					ChatColor.RESET + ChatColor.GOLD + item.getItemName());
			if(Main.getPlugin().isBroadcastingRankUp()){
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getDisplayName() + "'s " + 
						item.getItemName() + " has reached a new rank: " + getDisplayColor() + ranks.get(ind));
			}
			else{
				player.sendMessage(ChatColor.AQUA + "Your " + 
						item.getItemName() + " has reached a new rank: " + getDisplayColor() + ranks.get(ind));
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
}

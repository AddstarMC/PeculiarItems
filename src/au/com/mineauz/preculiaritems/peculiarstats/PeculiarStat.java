package au.com.mineauz.preculiaritems.peculiarstats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRUtils;

public abstract class PeculiarStat {
	
	public abstract String getName();
	public abstract ChatColor getDisplayColor();
	public abstract String getDisplayName();
	
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
		return ls;
	}
	
	public boolean hasStat(ItemStack item){
		if(PCRUtils.isPeculiarItem(item)){
			for(String lore : item.getItemMeta().getLore()){
				if(ChatColor.stripColor(lore).matches(getDisplayName() + ": " + "[0-9]+")){
					return true;
				}
			}
		}
		return false;
	}
	
	public void incrementStat(Player player, ItemStack item, int amount){
		if(PCRUtils.isPeculiarItem(item)){
			ItemMeta meta = item.getItemMeta();
			int inc = 0;
			int line = -1;
			List<String> lore = meta.getLore();
			for(String l : lore){
				if(ChatColor.stripColor(l).matches(getDisplayName() + ": " + "[0-9]+")){
					inc = Integer.valueOf(ChatColor.stripColor(l).replace(getDisplayName() + ": ", ""));
					line = meta.getLore().indexOf(l);
					break;
				}
			}
			
			inc += amount;
			
			if(line != -1){
				lore.set(line, getDisplayColor() + getDisplayName() + ": " + inc);
			}
			else{
				lore.add(getDisplayColor() + getDisplayName() + ": " + inc);
			}
			
			meta.setLore(lore);
			
			List<Integer> rankValues = Main.getRankValues();
			
			for(Integer rankv :rankValues){
				if(rankv.intValue() == inc){
					List<String> ranks = getRankNames();
					int ind = rankValues.indexOf(inc);
					if(ind >= ranks.size())
						ind = ranks.size() - 1;
					
					meta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + ranks.get(ind) + " " + PCRUtils.getItemName(item));
					if(Main.isBroadcastingRankUp()){
						Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getDisplayName() + "'s " + 
								PCRUtils.getItemName(item) + " has reached a new rank: " + getDisplayColor() + ranks.get(ind));
					}
					else{
						player.sendMessage(ChatColor.AQUA + "Your " + 
								PCRUtils.getItemName(item) + " has reached a new rank: " + getDisplayColor() + ranks.get(ind));
					}
					break;
				}
			}
			
			item.setItemMeta(meta);
		}
	}

}

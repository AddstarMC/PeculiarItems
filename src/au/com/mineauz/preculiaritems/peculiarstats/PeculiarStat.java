package au.com.mineauz.preculiaritems.peculiarstats;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.mineauz.peculiaritems.PCRUtils;

public abstract class PeculiarStat {
	
	public abstract String getName();
	public abstract ChatColor getDisplayColor();
	public abstract String getDisplayName();
	
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
	
	public void incrementStat(ItemStack item, int amount){
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
			item.setItemMeta(meta);
		}
	}

}
